package org.example;

import com.jayway.jsonpath.JsonPath;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

public class AutoCoding {

  public static void run() {



    UserinterfacesApi userinterfacesApi = test_auto_coding();


  }


  @SneakyThrows
  private static UserinterfacesApi test_auto_coding() {
    //==== given =========
    String episodeId = "cc_at55";
    Integration integration = new Integration();
    ArrayList<String> hl7Array = integration.readHl7File("caseCleaning1.hl7", episodeId);
    String path = "D:\\testProjects\\TestsWithoutIntegration\\src\\main\\resources\\tempCC.hl7";
    Files.write(Path.of(path),hl7Array);

    Terminal terminal = new Terminal();
    terminal.runImportAndAutoCoding(path,episodeId);

    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(episodeId);


    WlaApi wlaApi = new WlaApi();
    String token = wlaApi.getToken();
    String wlaId = wlaApi.getNiewiemJakToNazwac(episodeId, token);
    String allCodes1 = wlaApi.getAllCodes2(wlaId, token);
    String pathToAllDx = "$..Diagnoses[*].Code";
    List<String> allDx = JsonPath.read(allCodes1, pathToAllDx);

    Converter converter = new Converter();
    String xml = converter.convertXmlToObject(allDx);

    //==== when =========
    userinterfacesApi.openApplicationWithUpdatedInput(xml);
    String currentState = userinterfacesApi.getCurrentState();
    //userinterfacesApi.closeWithCancel();
    userinterfacesApi.closeWithAccept();


    //then
    String allActivatedFlags = "$.caseCleaningRules.dxCodeCleanings[*].activated";
    List<Boolean> activated = JsonPath.read(currentState, allActivatedFlags);
    //thera are only 3 cc-rules
    Assertions.assertTrue(activated.size() == 3);
    //3 rules are applied
    List<Boolean> allActivated = activated.stream().filter(b -> b == true).toList();
    Assertions.assertTrue(allActivated.size() == 3);

    //in Review status there are only 3 codes: I10, N18.30, N18.4
    String noReviewListPath = "$.doccoderData.noReviewList[*].code";
    List<String> allCodes = JsonPath.read(currentState, noReviewListPath);
    Assertions.assertTrue(allCodes.size() == 3);
    List<String> noReviewList = List.of("I10", "N18.30", "N18.4");
    Assertions.assertTrue(allCodes.equals(noReviewList));

    //in dx there are 7 anno
    String pathToAllAnnotation = "$.doccoderData.annotations";
    List<String> allAnno = JsonPath.read(currentState, pathToAllAnnotation);
    String pathToAllCodeType = "$.doccoderData.annotations[*].codeType";
    List<String> allCodeType = JsonPath.read(currentState, pathToAllCodeType);
    List<String> diagnosisAnno = allCodeType.stream().filter(e -> e.equals("DIAGNOSIS")).toList();
    Assertions.assertTrue(diagnosisAnno.size() == 7);

    return userinterfacesApi;
  }

}
