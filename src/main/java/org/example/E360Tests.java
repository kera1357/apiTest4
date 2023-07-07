package org.example;

import com.jayway.jsonpath.JsonPath;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

public class E360Tests {

  public static void run() {

    //case with applied rules
//    UserinterfacesApi userinterfacesApi = new UserinterfacesApi("cc12");
//    userinterfacesApi.openApplication("input360.xml");
//    checkWhetherRulesAreActive(userinterfacesApi);
//    test_delet_annotation(userinterfacesApi);

    //case withOUT applied rules
//    UserinterfacesApi userinterfacesApi2 = new UserinterfacesApi("cc10");
//    userinterfacesApi2.openApplication("input360.xml");
//    test_mark_and_code(userinterfacesApi2);

    UserinterfacesApi userinterfacesApi = test_auto_coding();
    //test_delete_annotation(userinterfacesApi);

    //test_delete_annotation_depended(userinterfacesApi);

  }


  @SneakyThrows
  private static UserinterfacesApi test_auto_coding() {
    //==== given =========
    String episodeId = "cc_at52";
    Integration integration = new Integration();
    ArrayList<String> hl7Array = integration.readHl7File("caseCleaning1.hl7", episodeId);
    String path = "D:\\testProjects\\TestsWithoutIntegration\\src\\main\\resources\\tempCC.hl7";
    Files.write(Path.of(path),hl7Array);

    Terminal terminal = new Terminal();
    terminal.runImportAndAutoCoding(path,episodeId);
    System.out.println("check assertions");

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

  private static void test_delete_annotation(UserinterfacesApi userinterfacesApi) {
    //given: case with accepted cc-rules and annotation

    //when
    userinterfacesApi.openApplicationNext();
    String currentState = userinterfacesApi.getCurrentState();
    String pathToAnnoId = "$.doccoderData.annotations[1].id";
    String annoId = JsonPath.read(currentState, pathToAnnoId);
    String s = userinterfacesApi.deleteAnno(annoId);
    String currentState2 = userinterfacesApi.getCurrentState();
    userinterfacesApi.closeWithCancel();

   //then
    //anno is removed
    String pathToAllAnnotation = "$.doccoderData.annotations";
    List<String> allAnno = JsonPath.read(currentState2, pathToAllAnnotation);
    String pathToAllCodeType = "$.doccoderData.annotations[*].codeType";
    List<String> allCodeType = JsonPath.read(currentState2, pathToAllCodeType);
    List<String> diagnosisAnno = allCodeType.stream().filter(e -> e.equals("DIAGNOSIS")).toList();
    Assertions.assertTrue(diagnosisAnno.size() == 6);

    // CC rule is deleted not reverted!
    String allActivatedFlags = "$.caseCleaningRules.dxCodeCleanings[*].activated";
    List<Boolean> activated = JsonPath.read(currentState2, allActivatedFlags);
    //thera are only 2 cc-rules
    Assertions.assertTrue(activated.size() == 2);
    //in diagnosisList there is no I12.0
    String pathToDx = "$.diagnosisList[*].code";
    List<String> dxList = JsonPath.read(currentState2, pathToDx);
    Assertions.assertFalse(dxList.contains("I12.0"));

    //in noReview status there are only 2 codes: N18.30, N18.4
    String noReviewListPath = "$.doccoderData.noReviewList[*].code";
    List<String> allCodes = JsonPath.read(currentState2, noReviewListPath);
    Assertions.assertTrue(allCodes.size() == 2);
    List<String> noReviewList = List.of("N18.30", "N18.4");
    Assertions.assertTrue(allCodes.equals(noReviewList));

  }

  private static void test_delete_annotation_depended(UserinterfacesApi userinterfacesApi) {
    //given: case with accepted cc-rules and annotation

    //when
    userinterfacesApi.openApplicationNext();
    String currentState = userinterfacesApi.getCurrentState();
    String pathToAnnoId = "$.doccoderData.annotations[3].id";
    String annoId = JsonPath.read(currentState, pathToAnnoId);
    String s = userinterfacesApi.deleteAnno(annoId);
    String currentState2 = userinterfacesApi.getCurrentState();
    userinterfacesApi.closeWithCancel();

    //then
    //1 anno is removed
    String pathToAllAnnotation = "$.doccoderData.annotations";
    List<String> allAnno = JsonPath.read(currentState2, pathToAllAnnotation);
    String pathToAllCodeType = "$.doccoderData.annotations[*].codeType";
    List<String> allCodeType = JsonPath.read(currentState2, pathToAllCodeType);
    List<String> diagnosisAnno = allCodeType.stream().filter(e -> e.equals("DIAGNOSIS")).toList();
    Assertions.assertTrue(diagnosisAnno.size() == 6);
    //userinterfacesApi.closeWithAccept();

    //CC rule is deleted not reverted, no N18.4
    String allActivatedFlags = "$.caseCleaningRules.dxCodeCleanings[*].activated";
    List<Boolean> activated = JsonPath.read(currentState2, allActivatedFlags);
    //thera is only one activated rule
    Assertions.assertTrue(activated.size() == 1);
    //in diagnosisList there is no N18.4
    String pathToDx = "$.diagnosisList[*].code";
    List<String> dxList = JsonPath.read(currentState2, pathToDx);
    Assertions.assertFalse(dxList.contains("N18.4"));

    //depended rule was reverted -> N18.30 is now accepted
    Assertions.assertTrue(dxList.contains("N18.30"));


  }


  private static void test_mark_and_code(UserinterfacesApi userinterfacesApi) {
    //userinterfacesApi.openApplicationNext();
    userinterfacesApi.markAndCode();
    userinterfacesApi.closeWithAccept();

  }
}
