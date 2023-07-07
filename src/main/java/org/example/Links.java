package org.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Links {

  String resultLink;
  String applicationLink;
  String acknowledgeResult;
  String caseId;

}
