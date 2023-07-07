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
@XmlRootElement(name = "MDC")
@XmlType(propOrder = {"value", "text"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Mdc {
  @XmlElement(name = "VALUE")
  private int value;

  @XmlElement(name = "TEXT")
  private String text;
}
