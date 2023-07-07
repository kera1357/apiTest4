package org.example.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
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
@XmlRootElement(name = "I10PR")
@XmlType(
    propOrder = {
      "value",
      "text",
      "originalInput",
      "docCoderId",
      "affect",
      "external",
      "date",
      "physician",
      "departament",
      "procedureType",
      "anesthesiaType",
      "reintervention"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class CodePx {

  @XmlElement(name = "VALUE")
  private String value;

  @XmlElement(name = "TEXT")
  private String text;

  @XmlElement(name = "ORIGINAL_INPUT")
  private String originalInput;

  @XmlElement(name = "DOCCODERID")
  private String docCoderId;

  @XmlElement(name = "AFFECT")
  private String affect;

  @XmlElement(name = "EXTERNAL")
  private External external;

  @XmlElement(name = "DATE")
  @XmlSchemaType(name = "date")
  private String date;

  @XmlElement(name = "PHYSICIAN")
  private Physician physician;

  @XmlElement(name = "DEPARTMENT") // readonly as column but send them back
  private Departament departament;

  @XmlElement(name = "PROCEDURE_TYPE") // readonly as column but send them back
  private String procedureType;

  @XmlElement(name = "ANESTHESIA_TYPE") // readonly as column but send them back
  private String anesthesiaType;

  @XmlElement(name = "REINTERVENTION") // readonly as column but send them back
  private String reintervention;
}
