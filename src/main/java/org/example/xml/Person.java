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
@XmlRootElement(name = "PERSON")
@XmlType(
    propOrder = {
      "birthdayDate",
      "ageInYears",
      "ageInDays",
      "gender",
      "historyId",
      "firstName",
      "lastName"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {

  @XmlElement(name = "BDT", required = true)
  private String birthdayDate;

  @XmlElement(name = "AGEY")
  private int ageInYears;

  @XmlSchemaType(name = "unsignedInt")
  @XmlElement(name = "AGED")
  private Long ageInDays;

  @XmlElement(name = "GENDER", required = true)
  private int gender;

  @XmlElement(name = "HISTORYID")
  private String historyId;

  @XmlElement(name = "FIRSTNAME")
  private String firstName;

  @XmlElement(name = "LASTNAME")
  private String lastName;
}
