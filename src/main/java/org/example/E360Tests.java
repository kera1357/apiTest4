package org.example;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class E360Tests extends TestBase{

  public static void run() {
    //case withOUT applied rules
    UserinterfacesApi ui = new UserinterfacesApi("cc10");
    ui.openApplication("input360.xml");

    workFlow_of_actions(ui);
    when_remove_anno_with_RCode(ui);

    UserinterfacesApi ui2 = new UserinterfacesApi("cct2");
    ui2.openApplication("input360_withAnno.xml");
    when_remove_anno_with_BCode_dependedAnno(ui2);
    when_remove_code_manual_by_dependent_rules(ui2);


  }

  private static void when_remove_code_manual_by_dependent_rules(UserinterfacesApi ui) {
    //when
    ui.openApplicationNext();
    String response = ui.deleteBlackCode();
    ui.closeWithCancel();
    //then
    codeIsNotInDiagList(response,"N18.5");
    codeIsInDiagList(response, "N18.4");
    codeIsNotInDiagList(response, "I12.0");
    codeIsInDiagList(response, "I10");
  }

  private static void when_remove_anno_with_BCode_dependedAnno(UserinterfacesApi ui) {
    String currentState = ui.getCurrentState();
    String annoId = findAnnoId(currentState, String.valueOf(3));
    String responseRemoveAnno = ui.removeAnnotation(annoId);
    ui.closeWithCancel();
    codeIsNotInDiagList(responseRemoveAnno, "N18.4");
    codeIsNotInNoReviewList(responseRemoveAnno, "N18.4");

    codeIsInDiagList(responseRemoveAnno, "N18.30");
    codeIsNotInNoReviewList(responseRemoveAnno, "N18.30");
  }

  private static void when_remove_anno_with_RCode(UserinterfacesApi ui) {
    ui.openApplicationNext();
    String response = ui.markAndCode();
    String annoId = findAnnoId(response, "0");
    String s = ui.switchOnRBG();
    String responseRemoveAnno = ui.removeAnnotation(annoId);
    codeIsNotInDiagList(responseRemoveAnno, "I10");
    codeIsNotInNoReviewList(responseRemoveAnno, "I10");
    ui.closeWithCancel();
  }

  private static String findAnnoId(String response, String index) {
    String annoPath = "$.doccoderData.annotations[" + index + "].id";
    String annoId =  JsonPath.read(response, annoPath);
    return annoId;
  }



  private static void workFlow_of_actions(UserinterfacesApi ui) {
    //given: cc10 is doc without anno. "hipertension arterial" , free code N18.5
    ui.getCurrentState();

    //when
    String reply = ui.markAndCode();
    //then
    annoIsCreated(reply, "2215ca3d-0116-4200-b000-177c85ae62b5");
    codeIsInDiagList(reply, "I10");

    //when
    String applyResponse = ui.switchOnRBG();
    //then
    codeIsInNoReviewList(applyResponse,"I10");

    //when
    String replyOff = ui.switchOffRBG();
    //then
    codeIsInDiagList(replyOff, "I10");

    //when
    ui.switchOnRBG();
    String removingResponse = ui.removeCode("N18.5");
    //then
    codeIsInDiagList(removingResponse, "I10");
    codeIsTiedToAnnotation(removingResponse, "I10");
    codeIsNotInDiagList(removingResponse, "N18.5");

    ui.closeWithCancel();
  }

  private static void codeIsTiedToAnnotation(String reply, String code) {
    String ftcCodePath = "$.diagnosisList[?].ftcCode";
    Filter filter =	Filter.filter(Criteria.where("code").eq(code));
    List<Object> status = JsonPath.read(reply, ftcCodePath, filter);
    Assertions.assertTrue(status.get(0).toString().equals("true"));
  }

  private static void annoIsCreated(String reply, String anno) {
    String sentenceIdPath = "$.doccoderData.annotations[0].anchor[0].sentenceId";
    String sentenceId = JsonPath.read(reply, sentenceIdPath);
    Assertions.assertTrue(sentenceId.equals(anno));
  }

  private static void codeIsInNoReviewList(String response, String code) {
    String noReviewPath = "$.doccoderData.noReviewList[*].code";
    List<String> noReviewCodes = JsonPath.read(response, noReviewPath);
    Assertions.assertTrue(noReviewCodes.contains(code));
  }
  private static void codeIsNotInNoReviewList(String response, String code) {
    String noReviewPath = "$.doccoderData.noReviewList[*].code";
    List<String> noReviewCodes = JsonPath.read(response, noReviewPath);
    Assertions.assertFalse(noReviewCodes.contains(code));
  }


}
