package org.example;


import lombok.SneakyThrows;
import xml.Claim;
import xml.Cntrl;
import xml.Crs;
import xml.Encounter;
import xml.Person;


public class MainWithouSelenium {
  @SneakyThrows
  public static void main(String[] args) {

//    Converter converter = new Converter();
//    converter.convertXmlToObject();
    //String s = converter.prepareXmlString(crs);
    System.out.println();
    //CesTests.run();

    E360Tests.run();
  }


}
