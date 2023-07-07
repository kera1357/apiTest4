package org.example.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
@XmlRootElement(name = "DRG")
@XmlType(propOrder = {"value", "text", "wgtlin", "ms", "mdc", "clmsoi", "clmrom", "vsn"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Drg {
  @XmlElement(name = "VALUE")
  private String value;

  @XmlElement(name = "TEXT")
  private String text;

  @XmlElement(name = "WGTLIN")
  private Wgtlin wgtlin;

  @XmlElement(name = "MS")
  private String ms;

  @XmlElement(name = "MDC")
  private Mdc mdc;

  @XmlElement(name = "CLMSOI")
  private Clmsoi clmsoi;

  @XmlElement(name = "CLMROM")
  private Clmrom clmrom;

  @XmlElement(name = "VSN")
  private Vsn vsn;

  @XmlAttribute private String codeset;
  @XmlAttribute private String type;
}
