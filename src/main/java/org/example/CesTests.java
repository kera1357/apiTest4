package org.example;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

public class CesTests {

  public static void run() {
    UserinterfacesApi userinterfacesApi = getUserinterfacesApi("input.xml");
    test_switchOnRuleRedBlack(userinterfacesApi);
    test_switchOffRedBlack(userinterfacesApi);
    test_removeBlackManually(userinterfacesApi);

    UserinterfacesApi userinterfacesApi2 = getUserinterfacesApi("input.xml");
    test_state_first_off_sub_on_cancel(userinterfacesApi2);

    UserinterfacesApi userinterfacesApi4 = getUserinterfacesApi("input.xml");
    test_state_first_off_sub_on_sub_off_cancel(userinterfacesApi4);

    UserinterfacesApi userinterfacesApi3 = getUserinterfacesApi("input.xml");
    test_state_first_off_sub_on_accept(userinterfacesApi3);
    //test_state_remove_green_code

    //tests with black-green rule
    UserinterfacesApi userinterfacesApi5 = getUserinterfacesApi("emptyInput.xml");
    test_add_black_green_osc(userinterfacesApi5);
    test_remove_green_code(userinterfacesApi5);
  }
  @NotNull
  private static UserinterfacesApi getUserinterfacesApi(String inputName) {
    String generatedString = RandomStringUtils.randomAlphabetic(10);
    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(generatedString);
    userinterfacesApi.openApplication(inputName);
    return userinterfacesApi;
  }
  private static void test_state_first_off_sub_on_sub_off_cancel(UserinterfacesApi userinterfacesApi) {
    //given
    test_switchOnRuleRedBlack(userinterfacesApi);
    //when
    userinterfacesApi.openApplicationNext();
    userinterfacesApi.deleteBlackCode();// BlackCode is N18.5 -> revert the rule -> First_OFF
    userinterfacesApi.addCode("N18.5");
    userinterfacesApi.switchOnRuleRedBlack(); //Sub_ON
    userinterfacesApi.switchOffRedBlack(); //sub_off
    userinterfacesApi.closeWithCancel();

    //then, rule should be still there
    userinterfacesApi.openApplicationNext();
    String currentState2 = userinterfacesApi.getCurrentState();
    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
    Object isActivated2 =  JsonPath.read(currentState2, activated);
    Assertions.assertTrue(isActivated2.toString().contains("true"));
    userinterfacesApi.closeWithCancel();
  }


  /*
  tests whether apply works
  and whether the rule is stored in DB
   */
  @SneakyThrows
  private static void test_switchOnRuleRedBlack(UserinterfacesApi userinterfacesApi) {
    String responseFromAdd = userinterfacesApi.getCurrentState();
    Assertions.assertFalse(responseFromAdd.contains("\"code\": \"I12.0\""));

    Response response = userinterfacesApi.switchOnRuleRedBlack();
    String result = userinterfacesApi.closeWithAccept();
    String removedCode = "N18.30";
    Assertions.assertFalse(result.contains(removedCode));

    //and whether the rule is stored in DB
    //String newInput = userinterfacesApi.updateWithStringUtils(result);

    userinterfacesApi.openApplicationNext();
    String currentState = userinterfacesApi.getCurrentState();
    //String currentState = userinterfacesApi.addPx();

    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
    Object isActivated =  JsonPath.read(currentState, activated);
    Assertions.assertTrue(isActivated.toString().contains("true"));
    String caseInfo = userinterfacesApi.closeWithAccept();
    System.out.println();
  }

  private static void test_switchOffRedBlack(UserinterfacesApi userinterfacesApi) {
    userinterfacesApi.openApplicationNext();
    String s = userinterfacesApi.switchOffRedBlack();
    String result = userinterfacesApi.closeWithAccept();
    String revertedRedCode = "N18.30";
    Assertions.assertTrue(result.contains(revertedRedCode));

    //is the rule still in DB? I hope not
    userinterfacesApi.openApplicationNext();
    String currentState = userinterfacesApi.getCurrentState();
    //System.out.println(currentState);
    String allActivatedFlags = "$.caseCleaningRules.dxCodeCleanings[*].activated";
    List<Boolean> activated = JsonPath.read(currentState, allActivatedFlags);
    Optional<Boolean> shouldBeEmpty = activated.stream().filter(b -> b == true).findAny();
    Assertions.assertTrue(shouldBeEmpty.isEmpty());
  }
  private static void test_removeBlackManually(UserinterfacesApi userinterfacesApi) {
    //given
    test_switchOnRuleRedBlack(userinterfacesApi);
    //when
    userinterfacesApi.openApplicationNext();
    userinterfacesApi.deleteBlackCode();//-> revert the rule
    String output = userinterfacesApi.closeWithAccept();

    //than
    String revertedRedCode = "N18.30";
    Assertions.assertTrue(output.contains(revertedRedCode));

  }
  private static void test_state_first_off_sub_on_accept(UserinterfacesApi userinterfacesApi) {
    //given
    test_switchOnRuleRedBlack(userinterfacesApi);
    //when
    userinterfacesApi.openApplicationNext();
    userinterfacesApi.deleteBlackCode();// BlackCode is N18.5 -> revert the rule -> First_OFF
    userinterfacesApi.addCode("N18.5");
    userinterfacesApi.switchOnRuleRedBlack(); //Sub_ON
    userinterfacesApi.closeWithAccept();

    //then, rule should be still there
    userinterfacesApi.openApplicationNext();
    String currentState2 = userinterfacesApi.getCurrentState();
    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
    Object isActivated2 =  JsonPath.read(currentState2, activated);
    Assertions.assertTrue(isActivated2.toString().contains("true"));
    userinterfacesApi.closeWithCancel();
  }

  private static void test_state_first_off_sub_on_cancel(UserinterfacesApi userinterfacesApi) {
    //given
    test_switchOnRuleRedBlack(userinterfacesApi);
    //when
    userinterfacesApi.openApplicationNext();
    userinterfacesApi.deleteBlackCode();// BlackCode is N18.5 -> revert the rule -> First_OFF
    userinterfacesApi.addCode("N18.5");
    userinterfacesApi.switchOnRuleRedBlack(); //Sub_ON
    userinterfacesApi.closeWithCancel();

    //then, rule should be still there
    userinterfacesApi.openApplicationNext();
    String currentState2 = userinterfacesApi.getCurrentState();
    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
    Object isActivated2 =  JsonPath.read(currentState2, activated);
    Assertions.assertTrue(isActivated2.toString().contains("true"));
    userinterfacesApi.closeWithCancel();
  }
  private static void test_add_black_green_osc(UserinterfacesApi userinterfacesApi) {
    String response = userinterfacesApi.addCodeCleaning();
    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
    Object isActivated2 =  JsonPath.read(response, activated);
    Assertions.assertTrue(isActivated2.toString().contains("true"));
    String currentState2 = userinterfacesApi.getCurrentState(); //for full init
    userinterfacesApi.closeWithAccept();
  }
  private static void test_remove_green_code(UserinterfacesApi userinterfacesApi) {
    //given
    //test_add_black_green_osc()
    userinterfacesApi.openApplicationNext();

    //when
    String s = userinterfacesApi.removeGreenCode();
    System.out.println();
    String output = userinterfacesApi.closeWithAccept();

    //then
    String removedGreenCode = "T14.90XA";
    Assertions.assertFalse(output.contains(removedGreenCode));

  }
  private static void test_state_remove_green_code(UserinterfacesApi userinterfacesApi) {
    //given
    //test_remove_green_code();
    //when
    //then
  }
}
