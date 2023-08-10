package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class TestBase {
  @NotNull
  public static UserinterfacesApi getUserinterfacesApi(String inputName) {
    String generatedString = RandomStringUtils.randomAlphabetic(10);
    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(generatedString);
    userinterfacesApi.openApplication(inputName);
    return userinterfacesApi;
  }


}
