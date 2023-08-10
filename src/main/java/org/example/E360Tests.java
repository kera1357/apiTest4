package org.example;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class E360Tests {

  public static void run() {
    //case withOUT applied rules
    UserinterfacesApi userinterfaceApi = new UserinterfacesApi("cc10");
    userinterfaceApi.openApplication("input360.xml");

    mark_and_code(userinterfaceApi);
    delete_annotation(userinterfaceApi);
//
//    test_delete_annotation_depended(userinterfacesApi);

  }


  private static void delete_annotation(UserinterfacesApi userinterfacesApi) {
    //given: case with accepted cc-rules and annotation

    //when
    //userinterfacesApi.openApplicationNext();
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


  private static void mark_and_code(UserinterfacesApi userinterfacesApi) {

    String reply = userinterfacesApi.markAndCode();
    userinterfacesApi.closeWithCancel();
    String sentenceIdPath = "$.doccoderData.annotations[0].anchor[0].sentenceId";
    String sentenceId = JsonPath.read(reply, sentenceIdPath);
    Assertions.assertTrue(sentenceId.equals("2215ca3d-0116-4200-b000-177c85ae62b5"));

  System.out.printf("");


  }
}
