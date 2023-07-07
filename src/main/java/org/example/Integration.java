package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

public class Integration {
  public static final String DOMAIN = "localhost";


   public Map<String, String> initApp(String xmlString) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://" + DOMAIN + ":8089/integration/cases"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .header("Content-Type", "text/xml")
        .POST(HttpRequest.BodyPublishers.ofString(xmlString))
        .build();

    HttpResponse<String> response = client.send(request,
        BodyHandlers.ofString());
    Map<String, String> stringStringMap = new UncheckedObjectMapper2().readValue(response.body());
    return stringStringMap;
  }
  public CompletableFuture<HttpResponse<String>> getResult(int timeOutSeconds, String caseId) {
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(150))
        .build();
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("http://localhost:8089/integration/result?caseId=" + caseId ))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .timeout(Duration.ofSeconds(timeOutSeconds))
        .build();
    return httpClient.sendAsync(request, BodyHandlers.ofString());
  }
   public String readXmlFile(String fileName) {
    InputStream fileFromResourceAsStream = getFileFromResourceAsStream(fileName);
    StringBuilder str = new StringBuilder();
    try {
      BufferedReader my_Reader = new BufferedReader(new InputStreamReader(fileFromResourceAsStream, "UTF-8"));
      String line = "";
      while((line = my_Reader.readLine()) != null)
      {
        str.append(line);
      }
      my_Reader.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not exists or insufficient rights");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("An exception occured while reading the file");
      e.printStackTrace();
    }
    return str.toString();
  }
  public ArrayList<String> readHl7File(String fileName, String episodeId) {
    InputStream fileFromResourceAsStream = getFileFromResourceAsStream(fileName);
    ArrayList<String> hl7Array = new ArrayList<>();
    try {
      BufferedReader my_Reader = new BufferedReader(new InputStreamReader(fileFromResourceAsStream, "UTF-8"));
      String line = "";
      while((line = my_Reader.readLine()) != null)
      {
        line = StringUtils.replace(line, "replaceEpisodeId", episodeId);
        hl7Array.add(line);
      }
      my_Reader.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not exists or insufficient rights");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("An exception occured while reading the file");
      e.printStackTrace();
    }
    return hl7Array;
  }
  /*
 This reading method enables reading file in every circumstances:
 Embedded in JAR, using IDEA, and unit test
  */
  private InputStream getFileFromResourceAsStream(String fileName) {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    if (inputStream == null) {
      throw new IllegalArgumentException("file not found! " + fileName);
    } else {
      return inputStream;
    }
  }

  public void closeCes(String closeLink, String xmlString, String caseId) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(closeLink))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .setHeader("caseId", caseId)
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(xmlString))
        .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    //System.out.println(response);
  }
}




class UncheckedObjectMapper2 extends ObjectMapper {
  /** Parses the given JSON string into a Map. */
  Map<String,String> readValue(String content) {
    try {
      return this.readValue(content, new TypeReference<>(){});
    } catch (IOException ioe) {
      throw new CompletionException(ioe);
    }
  }
}
