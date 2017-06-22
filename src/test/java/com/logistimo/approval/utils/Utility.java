package com.logistimo.approval.utils;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.ApproverRequest;
import com.logistimo.approval.models.DomainUpdateRequest;
import com.logistimo.approval.models.StatusUpdateRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * Created by nitisha.khandelwal on 23/05/17.
 */

public class Utility {

  public static final String APPROVAL_ID = "A123";

  public static List<ApprovalDomainMapping> getApprovalDomainMappings() {
    return Collections.singletonList(new ApprovalDomainMapping(APPROVAL_ID, 16L));
  }

  public static Approval getApproval() {
    Approval approval = new Approval();
    approval.setId(APPROVAL_ID);
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
    approval.setDomains(Collections.singleton((new ApprovalDomainMapping(APPROVAL_ID, 16L))));
    approval.setApprovers(Collections.singleton(new ApproverQueue(APPROVAL_ID, "U001", "QUEUED",
        "PRIMARY", null, null)));
    approval.setAttributes(Collections.singleton(new ApprovalAttributes(APPROVAL_ID, "kioskId",
        "K001")));
    return approval;
  }

  public static Approval getApprovalFromDB() {
    Approval approval = new Approval();
    approval.setId("A789");
    approval.setType("order");
    approval.setTypeId("O002");
    approval.setStatus("PENDING");
    approval.setRequesterId("R002");
    approval.setSourceDomainId(2L);
    approval.setConversationId("C001");
    approval.setUpdatedBy("R002");
    approval.setExpireAt(new Date(2017, 06, 12, 16, 53, 24));
    approval.setCreatedAt(new Date(2017, 05, 12, 16, 53, 24));
    approval.setUpdatedAt(new Date(2017, 05, 12, 16, 53, 24));
    return approval;
  }


  public static List<ApprovalStatusHistory> getApprovalStatusHistories() {
    List<ApprovalStatusHistory> statusHistories = new ArrayList<>();
    statusHistories.add(new ApprovalStatusHistory(APPROVAL_ID, "PENDING", "R001", null, null));
    statusHistories.add(new ApprovalStatusHistory(APPROVAL_ID, "APPROVED", "U002", null, null));
    return statusHistories;
  }

  public static DomainUpdateRequest getDomainUpdateRequest() {
    DomainUpdateRequest request = new DomainUpdateRequest();
    request.setSourceDomainId(4L);
    request.setDomains(Arrays.asList(5L, 6L));
    return request;
  }

  public static StatusUpdateRequest getStatusUpdateRequest() {
    StatusUpdateRequest request = new StatusUpdateRequest();
    request.setStatus("APPROVED");
    request.setUpdatedBy("U002");
    request.setMessageId("M002");
    return request;
  }

  public static ApprovalStatusHistory getLastStatus() {
    return new ApprovalStatusHistory(APPROVAL_ID, "PENDING", "R001", null, null);
  }

  public static ApprovalStatusHistory getCurrentStatus() {
    return new ApprovalStatusHistory(APPROVAL_ID, "APPROVED", "U002", "M002",
        Date.from(Instant.now()));
  }

  public static ApprovalRequest getApprovalRequest() {
    ApprovalRequest request = new ApprovalRequest();
    request.setType("order");
    request.setTypeId("O002");
    request.setRequesterId("R002");
    request.setSourceDomainId(2L);
    request.setMessage("This is a message.");
    request.setAttributes(Collections.singletonMap("kioskId", "K002"));
    ApproverRequest approver = new ApproverRequest();
    approver.setUserIds(Collections.singletonList("U002"));
    request.setApprovers(Collections.singletonList(approver));
    request.setDomains(Arrays.asList(4L, 5L));
    return request;
  }

}
