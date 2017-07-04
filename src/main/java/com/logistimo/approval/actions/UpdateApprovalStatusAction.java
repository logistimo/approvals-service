package com.logistimo.approval.actions;


import static com.logistimo.approval.utils.ValidateApprovalStatusUpdateRequest.validateRequest;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.Utility;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Component
public class UpdateApprovalStatusAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @Autowired
  private Utility utility;

  public Void invoke(String approvalId, StatusUpdateRequest request) {

    Approval approval = approvalRepository.findOne(approvalId);

    validateRequest(approvalId, approval, request);

    approval.setStatus(request.getStatus());
    approval.setUpdatedBy(request.getUpdatedBy());
    approvalRepository.save(approval);

    Date now = new Date();

    ApprovalStatusHistory lastStatus = statusHistoryRepository.findLastUpdateByApprovalId(approvalId);
    lastStatus.setEndTime(now);
    statusHistoryRepository.save(lastStatus);

    ApprovalStatusHistory currentStatus = statusHistoryRepository.save(new ApprovalStatusHistory(
        approvalId, request.getStatus(), request.getUpdatedBy(), request.getMessage(), now));

    utility.addMessageToConversation(approvalId, request.getMessage(), request.getUpdatedBy(),
        null);

    utility.publishApprovalStatusUpdateEvent(new ApprovalStatusUpdateEvent(approval.getId(),
        approval.getType(), approval.getTypeId(), approval.getStatus(),
        approval.getUpdatedBy(), currentStatus.getStartTime()));

    return null;
  }
}
