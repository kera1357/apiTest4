package org.example;

import java.io.StringReader;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.example.xml.CodeDx;
import org.example.xml.CodeXml;
import org.example.xml.Crs;


public class Converter {

  public String convertXmlToObject(List<String> allDx) {
    Integration integration = new Integration();
    String xmlAsString = integration.readXmlFile("input360.xml");
    JAXBContext jaxbContext = null;
    Crs crs = null;
    try {
      jaxbContext = JAXBContext.newInstance(Crs.class);
      StringReader reader = new StringReader(xmlAsString);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

       crs = (Crs) jaxbUnmarshaller.unmarshal(reader);
      //CodeXml i10Dxp = crs.getEncounter().getClaim().getI10Dxp();
      CodeXml i10Dxp = new CodeXml();
      i10Dxp.setValue(allDx.get(0));
      crs.getEncounter().getClaim().setI10Dxp(i10Dxp);
      allDx.remove(0);
      //List<CodeDx> i10Dx = crs.getEncounter().getClaim().getI10Dx();
      List<CodeDx> i10Dx = new ArrayList<>();
      for (String value : allDx) {
        CodeDx codeDx = new CodeDx();
        codeDx.setValue(value);
        i10Dx.add(codeDx);
      }
      crs.getEncounter().getClaim().setI10Dx(i10Dx);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return prepareXmlString(crs);
  }
  public String prepareXmlString(Crs crs) {
    try {
      //JAXBContext jaxbContext = JaxbInstanceCreator.getJAXBContextForRoot(Crs.class);
      JAXBContext jaxbContext = JAXBContext.newInstance(Crs.class);
      var mar = jaxbContext.createMarshaller();
      var stringWriter = new StringWriter();
      mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      mar.marshal(crs, stringWriter);
      return stringWriter.toString();
    } catch (JAXBException e) {
      System.out.println(e);
    }
    return null;
  }
}
