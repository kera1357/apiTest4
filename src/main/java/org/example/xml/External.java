package org.example.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(name = "EXTERNAL")
@XmlType(propOrder = {"status", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class External {

  @XmlElement(name = "STATUS", required = true)
  private String status;

  @XmlElement(name = "VALUE", required = true)
  private String value;
}
