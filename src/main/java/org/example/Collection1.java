package org.example;//package withoutSelenium;
//
//import com.jayway.jsonpath.JsonPath;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.Assertions;
//
//public class Collection1 {
//
//  public static void run() {
//    UserinterfacesApi userinterfacesApi = getUserinterfacesApi("input.xml");
//    test_switchOnRuleRedBlack(userinterfacesApi);
//    test_switchOffRedBlack(userinterfacesApi);
//    test_removeBlackManually(userinterfacesApi);
//  }
//
//  private static void test_state_first_off_sub_on_accept(UserinterfacesApi userinterfacesApi) {
//    //given
//    test_switchOnRuleRedBlack(userinterfacesApi);
//    //when
//    userinterfacesApi.openApplicationNext();
//    userinterfacesApi.deleteBlackCode();// BlackCode is N18.5 -> revert the rule -> First_OFF
//    userinterfacesApi.addCode("N18.5");
//    userinterfacesApi.switchOnRuleRedBlack(); //Sub_ON
//    userinterfacesApi.closeWithAccept();
//
//    //then, rule should be still there
//    userinterfacesApi.openApplicationNext();
//    String currentState2 = userinterfacesApi.getCurrentState();
//    String activated = "$.caseCleaningRules.dxCodeCleanings[0].activated";
//    Object isActivated2 =  JsonPath.read(currentState2, activated);
//    Assertions.assertTrue(isActivated2.toString().contains("true"));
//    userinterfacesApi.closeWithCancel();
//  }
//  @NotNull
//  private static UserinterfacesApi getUserinterfacesApi(String inputName) {
//    String generatedString = RandomStringUtils.randomAlphabetic(10);
//    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(generatedString);
//    userinterfacesApi.openApplication(inputName);
//    return userinterfacesApi;
//  }
//}
