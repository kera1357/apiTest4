package org.example.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
@XmlType(propOrder = {"i10Dxp", "i10Dx", "i10Prp", "i10Pr", "drg"})
@XmlRootElement(name = "CLAIM")
@XmlAccessorType(XmlAccessType.FIELD)
public class Claim {

  @XmlElement(name = "I10DXP", type = CodeDx.class)
  private CodeXml i10Dxp;

  @XmlElements({
    @XmlElement(name = "I10DX", type = CodeDx.class),
    @XmlElement(name = "ICDO3M", type = CodeMo.class)
  })
  List i10Dx;

  @XmlElement(name = "I10PRP")
  private CodePx i10Prp;

  @XmlElement(name = "I10PR")
  private List<CodePx> i10Pr;

  @XmlElement(name = "DRG")
  private List<Drg> drg;
}
