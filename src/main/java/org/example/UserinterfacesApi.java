package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;


public class UserinterfacesApi {

  public String caseId;
  String input = "";
  String episodeId = "";
  CompletableFuture<HttpResponse<String>> result;

  public UserinterfacesApi(String episodeId) {
    this.episodeId = episodeId;
  }

  public void openApplication(String inputName) {
    Integration integration = new Integration();
    input = integration.readXmlFile(inputName);
    input = StringUtils.replace(input, "placeHolder", episodeId);
    Map<String, String> linksMap = integration.initApp(input);
    Links links = new Links(linksMap.get("resultLink"), linksMap.get("applicationLink"),
        linksMap.get("caseId"),
        linksMap.get("caseId"));
    result = integration.getResult(555555, links.getCaseId());
    caseId = links.caseId;
  }
  public void openApplicationWithUpdatedInput(String xml) {
    Integration integration = new Integration();
    this.input = xml;
    Map<String, String> linksMap = integration.initApp(this.input);
    Links links = new Links(linksMap.get("resultLink"), linksMap.get("applicationLink"),
        linksMap.get("caseId"),
        linksMap.get("caseId"));
    result = integration.getResult(555555, links.getCaseId());
    caseId = links.caseId;
  }

  public void openApplicationNext() {
    Integration integration = new Integration();
    Map<String, String> linksMap = integration.initApp(input);
    Links links = new Links(linksMap.get("resultLink"), linksMap.get("applicationLink"),
        linksMap.get("caseId"),
        linksMap.get("caseId"));
    result = integration.getResult(555555, links.getCaseId());
    caseId = links.caseId;
    String currentState = getCurrentState(); //for full init
  }

  @SneakyThrows
  public String closeWithAccept() {
    accept(caseId);
    String newResult = result.get().body();
    String newInput = updateWithStringUtils(newResult);
    return newInput;

  }

  @SneakyThrows
  public void closeWithCancel() {
    cancel(caseId);
    String body = result.get().body();
  }

  @SneakyThrows
  public Response accept(String caseId) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{}");
    Request request = new Request.Builder()
        .url("http://localhost:8089/integration/ACCEPT/cases/" + caseId)
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", "1817d3c6-a626-3b9b-a869-4f8c35968ee2")
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();
    Response response = client.newCall(request).execute();
    return response;
  }

  @SneakyThrows
  public Response cancel(String caseId) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{}");
    Request request = new Request.Builder()
        .url("http://localhost:8089/integration/CANCEL/cases/" + caseId)
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", "1817d3c6-a626-3b9b-a869-4f8c35968ee2")
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();
    return client.newCall(request).execute();
  }

  @SneakyThrows
  public Response switchOnRuleRedBlack() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"caseId\":\"" + this.caseId
        + "\",\"applyRule\":{\"activated\":true,\"comment\":\"Reglas de mayor grado de especificidad. FUNCIONA\",\"triggerCodes\":[\"N18.5\",\"N18.30\"],\"stopCodes\":[],\"addedCodes\":[],\"removedCodes\":[\"N18.30\"],\"dischargeYear\":0,\"ruleNumber\":\"\",\"interferingWarning\":\"\",\"clickable\":true}}");
    Request request = new Request.Builder()
        .url("http://localhost:8089/case/apply-rule")
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", "9c593304-4fba-3281-a188-c6083107b69a")
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();
    return client.newCall(request).execute();
  }

  @SneakyThrows
  public String switchOffRedBlack() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"caseId\":\"" + caseId
        + "\",\"applyRule\":{\"activated\":false,\"triggerCodes\":[\"N18.5\",\"N18.30\"],\"stopCodes\":[],\"addedCodes\":[],\"removedCodes\":[\"N18.30\"],\"dischargeYear\":0,\"ruleNumber\":\"#1\",\"interferingWarning\":\"\",\"clickable\":true}}");
    Request request = new Request.Builder()
        .url("http://localhost:8089/case/apply-rule")
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", "48885df6-95d0-3c15-8897-39dcb62a0f37")
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();
    return client.newCall(request).execute().body().string();
  }

  /*
        TODO: I need this in order to make initialization of caseInfo complete
        Wlasciwie to mamy na to GET request ale robimy tam w sumie missbrauch bo ladujemy
        do GET i OkHttpClient nie chce czegos takiego zrobic.
        Ale w sumie pytanie dlaczgo nie robi init CaseCleaning w wczesniejszmy kroku w openAndValidateCase()

   */
  @SneakyThrows
  public String getCurrentState() {
    System.out.println("in getCurrentState caseId is: " + this.caseId);
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"caseId\":\"" + this.caseId
        + "\",\"newCodes\":[{\"code\":\"B01.11\",\"description\":\"Encefalitis y encefalomielitis debidas a varicela\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"94901352-7678-48a2-8b55-1bc72bc41b32\",\"hasQuestions\":false,\"voidCode\":false,\"originalInput\":\"B01.11\"}]}");
    Request request = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", caseId)
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();

    return client.newCall(request).execute().body().string();
  }
  @SneakyThrows
  public String searchForLiteral(String literal) {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()

        .url("http://localhost:8089/search/osc-results/es/DIAGNOSIS?input=" + literal +"&isOsc=true&caseId=" + this.caseId + "&aimDisabled=false")
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", "d2bd0ce0-8e71-3db6-88e6-589191de662c")
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      } else {
        return response.body().string();
      }
    }
  }

  public String updateWithStringUtils(String result) {

    String newClaim = StringUtils.substringBetween(result, "<CLAIM>", "</CLAIM>");
    int start = StringUtils.indexOf(input, "<CLAIM>");
    int end = StringUtils.indexOf(input, "</CLAIM>");
    input = StringUtils.overlay(input, newClaim, start + StringUtils.length("<CLAIM>"),
        end + 4 - StringUtils.length("</CLAIM>"));
    return input;
  }

  @SneakyThrows
  public String deleteBlackCode() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    String requestContent = "{\"caseId\":\"" + caseId
        + "\",\"newCodes\":[{\"code\":\"N18.5\",\"description\":\"Enfermedad renal crónica, estadio fase 5\",\"finalCode\":false,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"affectDrgFlag\":\"0\",\"morphological\":false,\"poa\":\"Y\",\"rom\":\"2\",\"soi\":\"1\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"18aad7be-c8cb-43d0-ae7a-f70ead67d17d\",\"hasQuestions\":false,\"codeAggrId\":\"18aad7be-c8cb-43d0-ae7a-f70ead67d17d\",\"voidCode\":false}]}";
    RequestBody body = RequestBody.create(mediaType, requestContent);
    Request request = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .method("DELETE", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", caseId)
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();
    return client.newCall(request).execute().body().string();
  }
  @SneakyThrows
  public String removeGreenCode() {
    OkHttpClient client = new OkHttpClient();
    String content = "{\"caseId\":\"toReplaceCaseId\",\"newCodes\":[{\"code\":\"T14.90XA\",\"description\":\"Traumatismo, no especificado, contacto inicial\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":true,\"proof\":[],\"errors\":[],\"warnings\":[],\"affectDrgFlag\":\"3\",\"originalInput\":\"caida\",\"certainty\":{\"certainPositions\":{\"position\":[]},\"uncertainPositions\":{\"position\":[]}},\"morphological\":false,\"poa\":\"Y\",\"rom\":\"P\",\"soi\":\"P\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"977610f4-6194-48ad-ad82-c1d86325ac54\",\"hasQuestions\":false,\"voidCode\":false}]}";
    String contentWithCaseId = content.replace("toReplaceCaseId", this.caseId);

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, contentWithCaseId);

    Request request = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .delete(body)
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Content-Type", "application/json")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", "6be9d170-d4fd-34f4-aae8-46837670fb9d")
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();
    return client.newCall(request).execute().body().string();
  }

  @SneakyThrows
  public String addCode(String newCode) {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    String content = "{\"caseId\":\"" + this.caseId
        + "\",\"newCodes\":[{\"code\":\"REPLACE_CODE\",\"description\":\"Encefalitis y encefalomielitis debidas a varicela\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"94901352-7678-48a2-8b55-1bc72bc41b32\",\"hasQuestions\":false,\"voidCode\":false,\"originalInput\":\"B01.11\"}]}";
    String newContent = content.replace("REPLACE_CODE", newCode);
    RequestBody body = RequestBody.create(mediaType, newContent);
    Request request = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .method("POST", body)
        .addHeader("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .addHeader("language", "SPANISH")
        .addHeader("caseId", caseId)
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json, text/plain, */*")
        .addHeader("sec-ch-ua-platform", "\"Windows\"")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader("Sec-Fetch-Mode", "cors")
        .addHeader("Sec-Fetch-Dest", "empty")
        .build();

    return client.newCall(request).execute().body().string();
  }
//  public void newGetCurrantState() {
//    HttpClient client = HttpClient.newHttpClient();
//
//    HttpRequest request = HttpRequest.newBuilder()
//        .uri(URI.create("http://localhost:8089/case/f90623cc-fce7-3b6c-b31e-df581f64dc9a"))
//        .GET()
//
//        .setHeader("Accept", "application/json, text/plain, */*")
//        .setHeader("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
//        .setHeader("Connection", "keep-alive")
//        .setHeader("Origin", "http://localhost:9100")
//        .setHeader("Referer", "http://localhost:9100/")
//        .setHeader("Sec-Fetch-Dest", "empty")
//        .setHeader("Sec-Fetch-Mode", "cors")
//        .setHeader("Sec-Fetch-Site", "same-site")
//        .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
//        .setHeader("caseId", "f90623cc-fce7-3b6c-b31e-df581f64dc9a")
//        .setHeader("language", "SPANISH")
//        .setHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
//        .setHeader("sec-ch-ua-mobile", "?0")
//        .setHeader("sec-ch-ua-platform", "\"Windows\"")
//        .build();
//
//    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//  }

  //  @SneakyThrows
//  public String addPx() {
//    OkHttpClient client = new OkHttpClient().newBuilder()
//        .build();
//    MediaType mediaType = MediaType.parse("application/json");
//    String content = "{\"caseId\":\"toReplaceCaseId\",\"newCodes\":[{\"code\":\"B00B0ZZ\",\"description\":\"Radiografía simple de médula espinal, con contraste, hiperosmolar\",\"finalCode\":true,\"type\":\"PROCEDURE\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"morphological\":false,\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"4f3195ed-0ce0-47aa-a6d9-02243e868e46\",\"hasQuestions\":false,\"voidCode\":false,\"originalInput\":\"B00B0ZZ\",\"procedureExternalStatus\":\"C\"}]}";
//    String contentWithCaseId = content.replace("toReplaceCaseId", this.caseId);
//    RequestBody body = RequestBody.create(mediaType, contentWithCaseId);
//    Request request = new Request.Builder()
//        .url("http://localhost:8089/case/code")
//        .method("POST", body)
//        .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
//        .addHeader("language", "SPANISH")
//        .addHeader("caseId", "c71a8293-0ada-3ec2-8f97-5a51c4ed94b9")
//        .addHeader("sec-ch-ua-mobile", "?0")
//        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
//        .addHeader("Content-Type", "application/json")
//        .addHeader("Accept", "application/json, text/plain, */*")
//        .addHeader("sec-ch-ua-platform", "\"Windows\"")
//        .addHeader("Sec-Fetch-Site", "same-site")
//        .addHeader("Sec-Fetch-Mode", "cors")
//        .addHeader("Sec-Fetch-Dest", "empty")
//        .build();
//    return client.newCall(request).execute().body().string();
//  }
  @SneakyThrows
  public String oscRequest() {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(
            "http://localhost:8089/search/osc-results/es/DIAGNOSIS?input=caida&isOsc=true&caseId=" + this.caseId + "&aimDisabled=false")
        .header("sec-ch-ua",
            "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("Accept", "application/json, text/plain, */*")
        .header("language", "SPANISH")
        .header("caseId", "6be9d170-d4fd-34f4-aae8-46837670fb9d")
        .header("sec-ch-ua-mobile", "?0")
        .header("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .header("Sec-Fetch-Site", "same-site")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Dest", "empty")
        .build();

      return client.newCall(request).execute().body().string();
  }
  @SneakyThrows
  public String addCodeCleaning() {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url("http://localhost:8089/search/osc-results/es/DIAGNOSIS?input=caida&isOsc=true&caseId=" + this.caseId + "&aimDisabled=false")
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", "6be9d170-d4fd-34f4-aae8-46837670fb9d")
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();
     client.newCall(request).execute().body().string();

    OkHttpClient clientPost = new OkHttpClient();
    String content = "{\"caseId\":\"toReplaceCaseId\",\"newCodes\":[{\"code\":\"T14.90XA\",\"description\":\"Traumatismo, no especificado, contacto inicial\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":true,\"proof\":[],\"errors\":[],\"warnings\":[],\"certainty\":{\"certainPositions\":{\"position\":[]},\"uncertainPositions\":{\"position\":[]}},\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"6696def3-3364-46e0-b61a-99878e28510c\",\"hasQuestions\":false,\"voidCode\":false,\"originalInput\":\"caida\"},{\"code\":\"W19.XXXA\",\"description\":\"Caída no especificada, contacto inicial\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"certainty\":{\"certainPositions\":{\"position\":[]},\"uncertainPositions\":{\"position\":[]}},\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":1,\"ftcCode\":false,\"id\":\"928fee15-c78a-41ca-8d3b-cf738d299773\",\"hasQuestions\":false,\"voidCode\":false,\"originalInput\":\"caida\"}],\"appliedRules\":[{\"activated\":true,\"triggerCodes\":[{\"code\":\"W19.XXXA\",\"description\":\"Caída no especificada, contacto inicial\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"072f2f53-b0f4-4f2d-97cd-d6ab755c5e22\",\"hasQuestions\":false,\"voidCode\":false}],\"stopCodes\":[],\"addedCodes\":[{\"code\":\"T14.90XA\",\"description\":\"Traumatismo, no especificado, contacto inicial\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"d76c6c54-450f-4309-8d60-4935619b2462\",\"hasQuestions\":false,\"voidCode\":false}],\"removedCodes\":[]}]}";
    String contentWithCaseId = content.replace("toReplaceCaseId", this.caseId);
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, contentWithCaseId);

    Request request2 = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .post(body)
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Content-Type", "application/json")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", "201bce42-2785-37ca-8745-09d023aa16b1")
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();
   return clientPost.newCall(request2).execute().body().string();
  }

  @SneakyThrows
  public String deleteAnno(String annotationId) {
    OkHttpClient client = new OkHttpClient();

    String content = "{\"caseId\":\"toReplaceCaseId\",\"doccoderOperationData\":{\"deleteAnnotationsRequestDto\":{\"annotationIds\":[\"toReplaceByAnnoId\"]}}}";
    String temp = content.replace("toReplaceByAnnoId", annotationId);
    String contentWithCaseId = temp.replace("toReplaceCaseId", this.caseId);
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, contentWithCaseId);

    Request request = new Request.Builder()
        .url("http://localhost:8089/case/ftc/annotations/delete")
        .post(body)
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Content-Type", "application/json")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", this.caseId)
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();

    return client.newCall(request).execute().body().string();
  }

  @SneakyThrows
  public String markAndCode() {
    OkHttpClient client = new OkHttpClient();

    String content = "{\"caseId\":\"toReplaceCaseId\",\"newCodes\":[{\"code\":\"I10\",\"description\":\"Hipertensión esencial (primaria)\",\"finalCode\":true,\"type\":\"DIAGNOSIS\",\"primaryCode\":false,\"proof\":[],\"errors\":[],\"warnings\":[],\"certainty\":{\"certainPositions\":{\"position\":[]},\"uncertainPositions\":{\"position\":[]}},\"morphological\":false,\"poa\":\"Y\",\"primaryExternalCause\":0,\"externalCause\":0,\"ftcCode\":false,\"id\":\"918d49e1-9480-4368-ab97-76ae4d44f1bb\",\"hasQuestions\":false,\"voidCode\":false}],\"doccoderOperationData\":{\"manualAnnotationDto\":{\"sentences\":[{\"sentenceId\":\"2215ca3d-0116-4200-b000-177c85ae62b5\",\"sentenceStart\":0,\"sentenceEnd\":20}]}},\"appliedRules\":[]}";
    String contentWithCaseId = content.replace("toReplaceCaseId", this.caseId);
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, contentWithCaseId);

    Request request = new Request.Builder()
        .url("http://localhost:8089/case/code")
        .post(body)
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6,es;q=0.5")
        .header("Connection", "keep-alive")
        .header("Content-Type", "application/json")
        .header("Origin", "http://localhost:9100")
        .header("Referer", "http://localhost:9100/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
        .header("caseId", "8ca187c0-f0e9-3f2f-a331-9ed9da35772d")
        .header("language", "SPANISH")
        .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();
    return client.newCall(request).execute().body().string();
  }
}