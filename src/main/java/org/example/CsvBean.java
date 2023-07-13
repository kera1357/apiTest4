package org.example;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Different.MetaData;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class CsvBean {
  @CsvBindByPosition(position = 0)
  private String literal;
  @CsvBindByPosition(position = 1)
  private String code1;
  @CsvBindByPosition(position = 2)
  private String code2;
  @CsvBindByPosition(position = 3)
  private String code3;
  @CsvBindByPosition(position = 4)
  private String code4;
  @CsvBindByPosition(position = 5)
  private String ms1;
  @CsvBindByPosition(position = 6)
  private String ms2;
  @CsvBindByPosition(position = 7)
  private String ms3;
  @CsvBindByPosition(position = 8)
  private String ms4;

  public MetaData metadata;

  @Override
  public String toString() {
    return "CsvBean{" +
        "literal='" + literal + '\'' +
        ", code1='" + code1 + '\'' +
        ", code2='" + code2 + '\'' +
        ", code3='" + code3 + '\'' +
        ", code4='" + code4 + '\'' +
        ", ms1='" + ms1 + '\'' +
        ", ms2='" + ms2 + '\'' +
        ", ms3='" + ms3 + '\'' +
        ", ms4='" + ms4 + '\'' +
        ", ms5='" + ms5 + '\'' +
        '}';
  }

  @CsvBindByPosition(position = 9)
  private String ms5;


  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  public String getCode1() {
    return code1;
  }

  public void setCode1(String code1) {
    this.code1 = code1;
  }

  public String getCode2() {
    return code2;
  }

  public void setCode2(String code2) {
    this.code2 = code2;
  }

  public String getCode3() {
    return code3;
  }

  public void setCode3(String code3) {
    this.code3 = code3;
  }

  public String getCode4() {
    return code4;
  }

  public void setCode4(String code4) {
    this.code4 = code4;
  }

  public String getMs1() {
    return ms1;
  }

  public void setMs1(String ms1) {
    this.ms1 = ms1;
  }

  public String getMs2() {
    return ms2;
  }

  public void setMs2(String ms2) {
    this.ms2 = ms2;
  }

  public String getMs3() {
    return ms3;
  }

  public void setMs3(String ms3) {
    this.ms3 = ms3;
  }

  public String getMs4() {
    return ms4;
  }

  public void setMs4(String ms4) {
    this.ms4 = ms4;
  }

  public String getMs5() {
    return ms5;
  }

  public void setMs5(String ms5) {
    this.ms5 = ms5;
  }


}
