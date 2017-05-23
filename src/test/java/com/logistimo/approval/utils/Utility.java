package com.logistimo.approval.utils;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.models.DomainUpdateRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by nitisha.khandelwal on 23/05/17.
 */

public class Utility {

  public static List<ApprovalAttributes> getApprovalAttributes() {
    ApprovalAttributes attribute1 = new ApprovalAttributes();
    attribute1.setApprovalId("A123");
    attribute1.setKey("kioskId");
    attribute1.setValue("K001");
    return Collections.singletonList(attribute1);
  }

  public static List<ApproverQueue> getApproverQueue() {
    ApproverQueue queue = new ApproverQueue();
    queue.setId(1L);
    queue.setUserId("U001");
    queue.setApprovalId("A123");
    queue.setApproverStatus("QUEUED");
    return Collections.singletonList(queue);
  }

  public static List<ApprovalDomainMapping> getApprovalDomainMappings() {
    ApprovalDomainMapping mapping1 = new ApprovalDomainMapping();
    mapping1.setApprovalId("A123");
    mapping1.setDomainId(16L);
    return Collections.singletonList(mapping1);
  }

  public static Approval getApproval() {
    Approval approval = new Approval();
    approval.setId("A123");
    approval.setType("order");
    approval.setTypeId("O001");
    approval.setStatus("PENDING");
    approval.setRequesterId("R001");
    approval.setSourceDomainId(1L);
    approval.setConversationId("C001");
    approval.setUpdatedBy("R001");
    approval.setExpireAt(new Date(2017, 06, 12, 16, 53, 24));
    approval.setCreatedAt(new Date(2017, 05, 12, 16, 53, 24));
    approval.setUpdatedAt(new Date(2017, 05, 12, 16, 53, 24));
    approval.setRequesterId("R001");
    return approval;
  }

  public static List<ApprovalStatusHistory> getApprovalStatusHistories() {
    List<ApprovalStatusHistory> statusHistories = new ArrayList<>();
    ApprovalStatusHistory history1 = new ApprovalStatusHistory();
    history1.setApprovalId("A123");
    history1.setStatus("PENDING");
    history1.setUpdatedBy("U001");
    statusHistories.add(history1);
    ApprovalStatusHistory history2 = new ApprovalStatusHistory();
    history1.setApprovalId("A123");
    history1.setStatus("APPROVED");
    history1.setUpdatedBy("U002");
    statusHistories.add(history2);
    return statusHistories;
  }

  public static DomainUpdateRequest getDomainUpdateRequest() {
    DomainUpdateRequest request = new DomainUpdateRequest();
    request.setSourceDomainId(4L);
    request.setDomains(Arrays.asList(5L, 6L));
    return request;
  }
}
