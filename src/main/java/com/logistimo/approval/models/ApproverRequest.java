package com.logistimo.approval.models;

import java.util.List;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class ApproverRequest {

  private List<String> userIds;

  private int expiry;

  private String type;
}
