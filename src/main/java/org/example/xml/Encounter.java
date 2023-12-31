package org.example.xml;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@XmlRootElement(name = "ENCOUNTER")
@XmlType(
    propOrder = {
      "episodeId",
      "patientType",
      "department",
      "dsp",
      "admissionDate",
      "dischargeDate",
      "los",
      "dmv",
      "provisional",
      "memo",
      "claim",
      "wgt"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounter {

  @XmlElement(name = "EPISODEID")
  private String episodeId;

  @XmlElement(name = "PATIENTTYPE", required = true)
  private int patientType;

  @XmlElement(name = "DEPARTMENT")
  private String department;

  @XmlElement(name = "DSP")
  private int dsp;

  @XmlElement(name = "ADT")
  private String admissionDate;

  @XmlElement(name = "DDT", required = true)
  private String dischargeDate;

  @XmlElement(name = "LOS")
  private int los;

  @XmlElement(name = "DMV")
  private int dmv;

  @XmlElement(name = "PROVISIONAL")
  private int provisional;

  @XmlElement(name = "MEMO")
  private String memo;

  @XmlElement(name = "CLAIM")
  private Claim claim;

  @XmlElement(name = "WGT")
  protected Long wgt;

}
