package com.logistimo.approval.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class Approver {

  private String userId;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date startTime;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date endTime;
}
