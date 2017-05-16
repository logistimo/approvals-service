package com.logistimo.approval.models;

import java.util.Date;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class Approver {

  private String userId;

  private Date startTime;

  private Date endTime;

}
