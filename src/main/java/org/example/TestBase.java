package org.example;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

public abstract class TestBase {
  @NotNull
  public static UserinterfacesApi getUserinterfacesApi(String inputName) {
    String generatedString = RandomStringUtils.randomAlphabetic(10);
    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(generatedString);
    userinterfacesApi.openApplication(inputName);
    return userinterfacesApi;
  }
  public static void codeIsInDiagList(String resonse, String code) {
    String diagnosisListPath = "$.diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(resonse, diagnosisListPath);
    Assertions.assertTrue(diagnosisList.contains(code));
  }
  public static void codeIsNotInDiagList(String resonse, String code) {
    String diagnosisListPath = "$.diagnosisList[*].code";
    List<String> diagnosisList =  JsonPath.read(resonse, diagnosisListPath);
    Assertions.assertFalse(diagnosisList.contains(code));
  }


}
