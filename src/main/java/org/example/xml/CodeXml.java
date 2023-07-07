package org.example.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@XmlType(
    propOrder = {
      "value",
      "text",
      "originalInput",
      "poa",
      "docCoderId",
      "lsi",
      "lrm",
      "affect",
      "ec",
      "pec",
      "admitlsi",
      "admitlrm"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeXml {

  @XmlElement(name = "VALUE")
  private String value;

  @XmlElement(name = "TEXT")
  private String text;

  @XmlElement(name = "ORIGINAL_INPUT")
  private String originalInput;

  @XmlElement(name = "POA")
  private String poa;

  @XmlElement(name = "DOCCODERID")
  private String docCoderId;

  @XmlElement(name = "LSI")
  private String lsi;

  @XmlElement(name = "LRM")
  private String lrm;

  @XmlElement(name = "AFFECT")
  private String affect;

  @XmlElement(name = "EC")
  private int ec;

  @XmlElement(name = "PEC")
  private int pec;

  @XmlElement(name = "ADMITLSI", required = true)
  private String admitlsi;

  @XmlElement(name = "ADMITLRM", required = true)
  private String admitlrm;

  public CodeXml(
      String value,
      String text,
      String originalInput,
      String poa,
      String docCoderId,
      String lsi,
      String lrm,
      String affect,
      int ec,
      int pec,
      String admitlsi,
      String admitlrm) {
    this.value = value;
    this.text = text;
    this.originalInput = originalInput;
    this.poa = poa;
    this.docCoderId = docCoderId;
    this.lsi = lsi;
    this.lrm = lrm;
    this.affect = affect;
    this.ec = ec;
    this.pec = pec;
    this.admitlsi = admitlsi;
    this.admitlrm = admitlrm;
  }
}
