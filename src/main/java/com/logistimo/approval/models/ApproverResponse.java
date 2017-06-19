package com.logistimo.approval.models;

import java.util.Date;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 15/06/17.
 */

@Data
public class ApproverResponse {

  private String userId;

  private String type;

  private Date startTime;

  private Date endTime;

}
