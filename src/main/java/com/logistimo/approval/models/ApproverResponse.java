package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 15/06/17.
 */

@Data
public class ApproverResponse {

  private String userId;

  private String type;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date startTime;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date endTime;

}