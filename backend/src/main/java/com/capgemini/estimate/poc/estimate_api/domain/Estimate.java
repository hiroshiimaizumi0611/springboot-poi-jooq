package com.capgemini.estimate.poc.estimate_api.domain;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Estimate {
  @Id public Long id;
  public String title;
  public String customerName;
  public Integer totalAmount;
}
