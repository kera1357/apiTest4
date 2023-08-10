package org.example;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class CodeOrder extends TestBase {
  public static void run() {
    UserinterfacesApi userinterfacesApi = getUserinterfacesApi("input.xml");
    when_rbg_on_off_codes_linked_together(userinterfacesApi);
    UserinterfacesApi userinterfacesApi2 = getUserinterfacesApi("input2.xml");
    when_rbg_on_off_codes_apart(userinterfacesApi2);
    UserinterfacesApi userinterfacesApi3 = getUserinterfacesApi("input3.xml");
    when_rbg_on_off_codes_apart_opposite(userinterfacesApi3);

    when_rb_on_off_codes(userinterfacesApi);
    when_bg_on(userinterfacesApi);
  }

  private static void when_rbg_on_off_codes_linked_together(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.getCurrentState();
    String response = userinterfacesApi.switchOnRBG();

    String diagnosisListPath = "$..diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(response, diagnosisListPath);
    Assertions.assertTrue(diagnosisList.get(0).equals("I12.0"));
    Assertions.assertTrue(diagnosisList.get(1).equals("N18.5"));

    String responseOff = userinterfacesApi.switchOffRBG();
    userinterfacesApi.closeWithCancel();

    List<String> diagnosisListOff =  JsonPath.read(responseOff, diagnosisListPath);
    Assertions.assertTrue(diagnosisListOff.get(0).equals("I10"));
    Assertions.assertTrue(diagnosisListOff.get(1).equals("N18.5"));



  }
  private static void when_rbg_on_off_codes_apart(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.getCurrentState();
    String response = userinterfacesApi.switchOnRBG();

    String diagnosisListPath = "$..diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(response, diagnosisListPath);
    Assertions.assertTrue(diagnosisList.get(0).equals("I12.0"));
    Assertions.assertTrue(diagnosisList.get(2).equals("N18.5"));

    String responseOff = userinterfacesApi.switchOffRBG();
    userinterfacesApi.closeWithCancel();

    List<String> diagnosisListOff =  JsonPath.read(responseOff, diagnosisListPath);
    Assertions.assertTrue(diagnosisListOff.get(0).equals("I10"));
    Assertions.assertTrue(diagnosisListOff.get(2).equals("N18.5"));

  }
  private static void when_rbg_on_off_codes_apart_opposite(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.getCurrentState();
    String response = userinterfacesApi.switchOnRBG();

    String diagnosisListPath = "$..diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(response, diagnosisListPath);
    //G is added directly before B
    Assertions.assertTrue(diagnosisList.get(0).equals("I12.0"));
    Assertions.assertTrue(diagnosisList.get(1).equals("N18.5"));

    String responseOff = userinterfacesApi.switchOffRBG();
    userinterfacesApi.closeWithCancel();

    List<String> diagnosisListOff =  JsonPath.read(responseOff, diagnosisListPath);
    //no CO, just replace G with R
    Assertions.assertTrue(diagnosisListOff.get(0).equals("I10"));
    Assertions.assertTrue(diagnosisListOff.get(1).equals("N18.5"));

  }
  private static void when_rb_on_off_codes(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.openApplicationNext();
    userinterfacesApi.switchOnRuleRedBlack();
    String response = userinterfacesApi.switchOffRedBlack();
    userinterfacesApi.closeWithCancel();
    String diagnosisListPath = "$..diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(response, diagnosisListPath);
    //No code ordering -> R should be below of B
    Assertions.assertTrue(diagnosisList.get(1).equals("N18.5"));
    Assertions.assertTrue(diagnosisList.get(2).equals("N18.30"));
  }
  private static void when_bg_on(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.openApplicationNext();
    String response = userinterfacesApi.switchOnBG();
    userinterfacesApi.closeWithCancel();
    String diagnosisListPath = "$..diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(response, diagnosisListPath);
    //G is added directly before B
    Assertions.assertTrue(diagnosisList.get(3).equals("Z92.3"));
    Assertions.assertTrue(diagnosisList.get(4).equals("O35.6XX0"));
  }

}
