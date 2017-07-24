package com.logistimo.approval.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by charan on 24/07/17.
 */
@Data
public class AttributeFilter {

  private String key;

  private List<String> values = new ArrayList<>(1);
}
