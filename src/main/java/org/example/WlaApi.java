package org.example;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WlaApi {

  //get token
  @SneakyThrows
  public String getToken() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    RequestBody body = RequestBody.create(mediaType, "grant_type=password&username=demo&password=Demo");
    Request request = new Request.Builder()
        .url("http://localhost:9001/token")
        .method("POST", body)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();
    String response = client.newCall(request).execute().body().string();
    String tokenPath = "$.access_token";
    String token = JsonPath.read(response, tokenPath);
    return token;
  }
  //get wla id for a particular case
  @SneakyThrows
  public String getNiewiemJakToNazwac(String caseId, String token) {

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    Request request = new Request.Builder()
        .url("http://localhost:9001/api/episodes?caseid=" + caseId)
        .method("GET", null)
        .addHeader("Authorization", "bearer " + token)
        .build();
    String response = client.newCall(request).execute().body().string();
    String idPath = "$.Episodes[0].Id";
    int wlaId = JsonPath.read(response, idPath);
    return String.valueOf(wlaId);
  }

  @SneakyThrows
  public  String getAllCodes(String wlaId, String token) {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url("http://localhost:9001/api/episodes/details/" + wlaId)
        .header("Accept", "application/json, text/plain, */*")
        .header("Accept-Language", "en-US")
        .header("Authorization", "bearer " + token)
        .header("Connection", "keep-alive")
        .header("Origin", "http://localhost:9005")
        .header("Referer", "http://localhost:9005/")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .header("Sec-Fetch-Site", "same-site")
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67")
        .header("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Microsoft Edge\";v=\"114\"")
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
     return response.body().string();
    }

  }


  //get case for given id

}
