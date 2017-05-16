package com.logistimo.approval.models;

import java.util.List;
import lombok.Data;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
public class DomainUpdateRequest {

  private Long sourceDomainId;

  private List<Long> domains;

}
