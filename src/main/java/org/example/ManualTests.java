package org.example;

import static com.jayway.jsonpath.Option.ALWAYS_RETURN_LIST;
import static com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS;

import com.google.common.base.Splitter;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class ManualTests {

  @SneakyThrows
  void run() {

    List<CsvBean> csvBeans = readCsvFileToBean();
    extractMetadata(csvBeans);
    System.out.println();
//    List<List<String>> inputRaw = readCsvFile();
//    ArrayList<String> input = transformInput2(inputRaw);

    test_response_on_search(csvBeans);


  }

  private void extractMetadata(List<CsvBean> csvBeans) {
    for (CsvBean record : csvBeans) {
      String literalWithContamination = record.getLiteral();

      List<String> contaminatedLiteral = Splitter.on(" ")
          .trimResults()
          .omitEmptyStrings()
          .splitToList(literalWithContamination).stream().collect(Collectors.toList());

      //remove trash behind literal if exists
      if (contaminatedLiteral.get(contaminatedLiteral.size() - 1).contains("_")) {
        String withoutUnderscore = contaminatedLiteral.get(contaminatedLiteral.size() - 1).substring(1);
        record.interactiveCave = withoutUnderscore;
        contaminatedLiteral.remove(contaminatedLiteral.size() - 1);
        System.out.println();
      }

      //contaminatedLiteral -> metaData or trash or empty; literal;
      String metaDataCandidate = contaminatedLiteral.get(0);
      if (metaDataCandidate.contains("_")) { //extract meda data or trash
        if (metaDataCandidate.contains("2021")) {
          metaDataCandidate = "";
        }
        if (metaDataCandidate.isEmpty()) {
          metaDataCandidate = "_obs";
        }
        Optional<MetaData> metaDataOpt = MetaData.fromText(metaDataCandidate);
        if (metaDataOpt.isEmpty()) {
          throw new RuntimeException("no meta data found -> make sure there is some");
        }
        MetaData metaData = metaDataOpt.get();
        record.metadata = metaData;

        String onlyLiteral = contaminatedLiteral.stream().skip(1).collect(Collectors.joining(" "));
        record.setLiteral(onlyLiteral);

      } else { //metaDataCandidate contains literal and need to be replaced with default value
        metaDataCandidate = "_obs";
        Optional<MetaData> metaDataOpt = MetaData.fromText(metaDataCandidate);
        if (metaDataOpt.isEmpty()) {
          throw new RuntimeException("no meta data found -> make sure there is some");
        }
        MetaData metaData = metaDataOpt.get();
        record.metadata = metaData;
        String onlyLiteral = contaminatedLiteral.stream().collect(Collectors.joining(" "));
        record.setLiteral(onlyLiteral);
      }


    }

  }

  @SneakyThrows
  private List<CsvBean> readCsvFileToBean() {
    URL fileUrl = ManualTests.class.getClassLoader().getResource("manualTest1.csv");
    return new CsvToBeanBuilder(new FileReader(fileUrl.getFile()))
        .withType(CsvBean.class)
        .build()
        .parse();
  }


  private void test_response_on_search(List<CsvBean> csvBean) {
    //open app
    Configuration config = Configuration
        .builder()
        .options(ALWAYS_RETURN_LIST, SUPPRESS_EXCEPTIONS)
        .build();

      for (CsvBean record : csvBean) {
        System.out.println();
        System.out.println();
        System.out.println("executing: " + record.getLiteral());

        MetaData metadata = record.metadata;
        String fileName = chooseInputFile(metadata);
        System.out.println("fileName: " + fileName);
        UserinterfacesApi userinterfacesApi = getUserinterfacesApi(fileName);
        String responseFromAdd = userinterfacesApi.getCurrentState();
        String response = userinterfacesApi.searchForLiteral(record.getLiteral());
        String cleanInputPath = "$.inputPair.cleanInput";
        ArrayList<String> cleanInput = JsonPath.using(config).parse(response).read( cleanInputPath);
        System.out.println(cleanInput);
        //if(record.interactiveCave.equals("intoxicación")) {
        if(!record.interactiveCave.isEmpty()) {
          System.out.println("check IC");
          List<String> icList = checkInteractiveCave(response, record, config);
          String icResponse = userinterfacesApi.searchForLiteral(icList.get(0));
         // clickIteractiveCave();
          checkCodes(icResponse, record, config);
        } else {
          checkCodes(response, record, config);
        }
        userinterfacesApi.closeWithCancel();
      }

  }

  public List<String> checkInteractiveCave(String response, CsvBean record, Configuration config) {
    String icLiteralsPath = "$.caves[*].parts[*].text";
    List<String> icLiterals = JsonPath.using(config).parse(response).read( icLiteralsPath);
    List<String> icList = icLiterals.stream().filter(ic -> !ic.isEmpty()).filter(ic -> ic.contains("$")).toList();
    return icList;
  }

  /*
  There are three cases: codes are presents, there is no code, there is thesaurus
   */
  private void checkCodes(String response, CsvBean record, Configuration config) {
    String codesPath = "$.codes[*].code";
    List<String> codes = JsonPath.using(config).parse(response).read( codesPath);
    List<String> expectedCodes = new ArrayList<>(
        Arrays.asList(record.getCode1(), record.getCode2(),
            record.getCode3(), record.getCode4())).stream().filter(e -> !e.isEmpty()).collect(
        Collectors.toList());

    if(!expectedCodes.equals(codes)) {
      System.out.println("============= Watch out - there is mismatch =============");
      System.out.println(codes.toString());
      System.out.println(expectedCodes.toString());
    }
  }

  private static UserinterfacesApi getUserinterfacesApi(String inputName) {
    String generatedString = RandomStringUtils.randomAlphabetic(10);
    UserinterfacesApi userinterfacesApi = new UserinterfacesApi(generatedString);
    userinterfacesApi.openApplication(inputName);
    return userinterfacesApi;
  }

  public List<List<String>> readCsvFile() {
    Integration integration = new Integration();
    //integration.readHl7File("manualTest2.txt", "egal");
    InputStream fileFromResourceAsStream = integration.getFileFromResourceAsStream(
        "manualTest1.csv");

    List<List<String>> records = new ArrayList<List<String>>();
    try (CSVReader csvReader = new CSVReader(new InputStreamReader(fileFromResourceAsStream));) {
      String[] values = null;
      while ((values = csvReader.readNext()) != null) {
        records.add(Arrays.asList(values));
      }
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException(e);
    }
    return records;
  }

  /*
  Desired input format: sorted metadata; literal; results;
   */
//  private ArrayList<String> transformInput(ArrayList<String> literals) {
//    ArrayList<String> literalsWithoutUnderscore = new ArrayList<>();
//
//    for (String literal : literals) {
//      String[] split = StringUtils.split(literal);
//      ArrayList<String> strList = new ArrayList<String>(Arrays.asList(split));
//      strList.remove(strList.size()-1); //remove some trash
//      if(strList.get(0).contains("_")) { //extract meda data
//        String metaDataCandidate = strList.get(0);
//        if(metaDataCandidate.isEmpty()) {
//          metaDataCandidate = "_Age_adult";
//        }
//        //boolean b = Stream.of(MetaData.values()).anyMatch(v -> v.name().equals(metaDataCandidate));
//        String fileName = chooseInputFile(metaDataCandidate);
////        Integration integration1 = new Integration();
////        integration.readXmlFile(fileName);
//        strList.remove(0);
//        String literalClean = strList.stream()
//            .collect(Collectors.joining(" "));
//      }
////    List<String> literalWithoutUnderscore = strList.stream().filter(w -> !w.contains("_")).toList();
////    String commaSeparated = literalWithoutUnderscore.stream()
////        .collect(Collectors.joining(" "));
////    literalsWithoutUnderscore.add(commaSeparated);
//    }
//    return literalsWithoutUnderscore;
//  }
  private ArrayList<String> transformInput2(List<List<String>> inputRaw) {
    List<List<String>> inputFormatted = new ArrayList<>();
    for (List<String> line : inputRaw) {
      String literalWithContamination = line.get(0);
      String[] split = StringUtils.split(literalWithContamination);
      ArrayList<String> contaminatedLiteral = new ArrayList<String>(Arrays.asList(split));
      contaminatedLiteral.remove(contaminatedLiteral.size() - 1); //remove trash behind literal

      //contaminatedLiteral -> metaData or trash; literal;
      if (contaminatedLiteral.get(0).contains("_")) { //extract meda data or trash
        String metaDataCandidate = contaminatedLiteral.get(0);
        if (metaDataCandidate.isEmpty()) {
          metaDataCandidate = "_Age_adult";
        }
        Optional<MetaData> metaDataOpt = MetaData.fromText(metaDataCandidate);
        if (metaDataOpt.isEmpty()) {
          throw new RuntimeException("no meta data found -> make sure there is some");
        }
        MetaData metaData = metaDataOpt.get();
        List<String> newLine = new ArrayList<>(line);
        newLine.add(0, metaData.metadata);
        System.out.println();
      }

    }
    return null;
  }

  private static String chooseInputFile(MetaData metaData) {
    //MetaData metaData = MetaData.valueOf(metaDataCandidate);
    String parameter = switch (metaData) {
      case ADULT:
        yield "inputAdultF.xml";
      case MALE:
        yield "inputAdultM.xml";
      case FEMALE:
        yield "inputAdultF.xml";
      case NEW_BORN:
        yield "inputNewBorn.xml";
      case OBS: //this should be a default input, when no metadata is provided
        yield "inputAdultF_OBS.xml";
      case URG:
        yield "inputAdultF.xml";
      default:
        throw new IllegalStateException("Unexpected value: " + metaData);
    };
    return parameter;
  }

  public enum MetaData {
    NEW_BORN("_Age_newborn"),
    ADULT("_Age_adult"),
    MALE("_Sex_male"),
    FEMALE("_Sex_female"),
    OBS("_obs"),
    URG("_ucias");
    private String metadata;

    MetaData(String metadata) {
      this.metadata = metadata;
    }

    public String getMetadata() {
      return metadata;
    }

    public static Optional<MetaData> fromText(String metadata) {
      return Arrays.stream(values())
          .filter(m -> m.metadata.equalsIgnoreCase(metadata))
          .findFirst();
    }
  }

}
