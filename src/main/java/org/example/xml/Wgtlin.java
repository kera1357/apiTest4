package org.example.xml;

import java.math.BigDecimal;
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
@XmlRootElement(name = "WGTLIN")
@XmlType(propOrder = {"drgwt", "alos", "lowt", "hight"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Wgtlin {
  @XmlElement(name = "DRGWT")
  private BigDecimal drgwt;

  @XmlElement(name = "ALOS")
  private BigDecimal alos;

  @XmlElement(name = "LOWT")
  @XmlSchemaType(name = "unsignedInt")
  private long lowt;

  @XmlElement(name = "HIGHT")
  @XmlSchemaType(name = "unsignedInt")
  private long hight;
}
