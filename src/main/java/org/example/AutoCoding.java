package org.example;

import com.jayway.jsonpath.JsonPath;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

public class AutoCoding {

  public static void run() {
    test_auto_coding();
  }

  @SneakyThrows
  private static void test_auto_coding() {
    //==== given =========
    String episodeId = "cc_at59";
    Integration integration = new Integration();
    ArrayList<String> hl7Array = integration.readHl7File("caseCleaning1.hl7", episodeId);
    String path = "D:\\testProjects\\TestsWithoutIntegration\\src\\main\\resources\\tempCC.hl7";
    Files.write(Path.of(path),hl7Array);

    Terminal terminal = new Terminal();
    terminal.runImportAndAutoCoding(path,episodeId);

    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(episodeId);

    String xml = createInputXml(episodeId);

    //==== when =========
    userinterfacesApi.openApplicationWithUpdatedInput(xml);
    String currentState = userinterfacesApi.getCurrentState();
    userinterfacesApi.closeWithAccept();

    //then
    activatedRuleSizeShouldBe(currentState, 3);

    //in Review status there are only 3 codes: I10, N18.30, N18.4
    List<String> expected = List.of("I10", "N18.30", "N18.4");
    checkNoReviewList(currentState, expected);

    dxAnnotationSizeShouldBe(currentState, 7);
  }

  private static String createInputXml(String episodeId) {
    WlaApi wlaApi = new WlaApi();
    String token = wlaApi.getToken();
    String wlaId = wlaApi.getNiewiemJakToNazwac(episodeId, token);
    String allCodes1 = wlaApi.getAllCodes(wlaId, token);
    String pathToAllDx = "$..Diagnoses[*].Code";
    List<String> allDx = JsonPath.read(allCodes1, pathToAllDx);

    Converter converter = new Converter();
    String xml = converter.convertXmlToObject(allDx, episodeId);
    return xml;
  }

  @NotNull
  private static void activatedRuleSizeShouldBe(String currentState, int size) {
    String allActivatedFlags = "$.caseCleaningRules.dxCodeCleanings[*].activated";
    List<Boolean> activated = JsonPath.read(currentState, allActivatedFlags);
    Assertions.assertTrue(activated.size() == size);

    //3 rules are applied
    List<Boolean> allActivated = activated.stream().filter(b -> b == true).toList();
    Assertions.assertTrue(allActivated.size() == size);
  }

  private static void checkNoReviewList(String currentState, List<String> expectedCodes) {
    String noReviewListPath = "$.doccoderData.noReviewList[*].code";
    List<String> noReviewCodes = JsonPath.read(currentState, noReviewListPath);
    Assertions.assertTrue(noReviewCodes.size() == 3);
    Assertions.assertTrue(noReviewCodes.equals(expectedCodes));
  }

  private static void dxAnnotationSizeShouldBe(String currentState, int annoAmount) {
    String pathToAllCodeType = "$.doccoderData.annotations[*].codeType";
    List<String> allCodeType = JsonPath.read(currentState, pathToAllCodeType);
    List<String> diagnosisAnno = allCodeType.stream().filter(e -> e.equals("DIAGNOSIS")).toList();
    Assertions.assertTrue(diagnosisAnno.size() == annoAmount);
  }

}
