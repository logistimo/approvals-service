package com.logistimo.approval.models;

import java.util.Date;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class StatusResponse {

  private String status;

  private String updatedBy;

  private String messageId;

  private Date startTime;

  private Date endTime;
}
