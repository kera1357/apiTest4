package org.example.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@XmlRootElement(name = "CRS")
//@XmlType(propOrder = {"cntrl", "person", "encounter", "caas"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Crs {

  @XmlElement(name = "CNTRL", required = true)
  private Cntrl cntrl;

  @XmlElement(name = "PERSON")
  private Person person;

  @XmlElement(name = "ENCOUNTER")
  private Encounter encounter;


}
