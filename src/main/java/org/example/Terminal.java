package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

public class Terminal {
  private Thread worker;
  public AtomicBoolean atomicBoolean = new AtomicBoolean(false);
  ExecutorService executor = Executors.newFixedThreadPool(10);

  @SneakyThrows
  public void runMllpReceiver(String hl7) {
    Process process = Runtime.getRuntime()
        .exec("cmd /c Mmm.His.MllpReceiverTest.exe " + hl7, null, new File("C:\\Program Files (x86)\\3MMllpReceiver\\"));
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

    String line = "";
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      if(line.contains("Message sent")) {
        System.out.println("runMllpReceiver done");
        reader.close();
        process.destroyForcibly();
        return;
      }
    }


  }

  public void runAutoCoding(String episodeId) throws IOException {
      Process process = Runtime.getRuntime()
          .exec("cmd /c AutomaticCoding.Invoker.exe", null, new File("C:\\Program Files (x86)\\3MAutomaticCoding\\"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    reader.readLine();
    reader.readLine();
    String line3 = reader.readLine();
      if(processSuccessfullyCompleted(line3, episodeId)) {
        reader.close();
        process.destroyForcibly();
        return; //go further
      }
      else {
        runAutoCoding(episodeId);
      }
  }

  private boolean noCasesToProceed(String line) {
    if(line.equals("No cases to proceed")) {
      return true;
    }
    return false;
  }

  private boolean processSuccessfullyCompleted(String line, String episodeId) {
    if(StringUtils.startsWith(line, "Correct cases") && line.contains(episodeId)) {
      return true;
    }
    return false;
  }


  public void runImportAndAutoCoding(String hl7, String episodeId) throws IOException {
    runMllpReceiver(hl7);

    runAutoCoding(episodeId);

//    Callable<String> callableTaskReceiver = () -> {
//      System.out.println("runMllpReceiver");
//      runMllpReceiver(hl7);
//      return "Task's execution";
//    };
//    Callable<String> callableTaskAutoCoding = () -> {
//      System.out.println("runAutoCoding");
//      runAutoCoding(episodeId);
//      return "Task's execution";
//    };
//    executor.submit(callableTaskReceiver);
//    executor.submit(callableTaskAutoCoding);
//    executor.awaitTermination(45, TimeUnit.SECONDS);
//    executor.shutdownNow();



  }

  private static class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
  }


}
