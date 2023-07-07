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
@XmlRootElement(name = "VSN")
@XmlType(propOrder = {"scheme", "version"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Vsn {
  @XmlElement(name = "SCHEME")
  private String scheme;

  @XmlElement(name = "VERSION")
  private String version;
}
