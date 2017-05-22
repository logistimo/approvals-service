package com.logistimo.approval.actions;


import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalStatusUpdateMessage;
import com.logistimo.approval.models.StatusUpdateRequest;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.utils.JmsUtil;
import java.util.Date;
import org.apache.catalina.connector.Response;
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
  private IApprovalStatusHistoryRepository approvalStatusHistoryRepository;


  @Autowired
  private JmsUtil jmsUtil;

  public Void invoke(String approvalId, StatusUpdateRequest request) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, String.format(APPROVAL_NOT_FOUND, approvalId));
    }

    approval.setStatus(request.getStatus());
    approval.setUpdatedBy(request.getUpdatedBy());
    approvalRepository.save(approval);

    Date now = new Date();

    ApprovalStatusHistory lastStatus = approvalStatusHistoryRepository
        .findLastUpdateByApprovalId(approvalId);
    lastStatus.setEndTime(now);

    approvalStatusHistoryRepository.save(lastStatus);

    ApprovalStatusHistory newStatus = new ApprovalStatusHistory();
    newStatus.setApprovalId(approvalId);
    newStatus.setStatus(request.getStatus());
    newStatus.setMessageId(request.getMessageId());
    newStatus.setUpdatedBy(request.getUpdatedBy());
    newStatus.setStartTime(now);

    approvalStatusHistoryRepository.save(newStatus);

    publishApprovalStatusUpdateMessage(approval);

    return null;
  }

  private void publishApprovalStatusUpdateMessage(Approval approval) {
    ApprovalStatusUpdateMessage message = new ApprovalStatusUpdateMessage();
    message.setApprovalId(approval.getId());
    message.setStatus(approval.getStatus());
    message.setUpdatedBy(approval.getUpdatedBy());
    message.setType(approval.getType());
    message.setTypeId(approval.getTypeId());
    jmsUtil.sendMessage(message);
  }
}
