package com.logistimo.approval.actions;


import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
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
  private IApprovalStatusHistoryRepository statusHistoryRepository;

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

    ApprovalStatusHistory lastStatus = statusHistoryRepository.findLastUpdateByApprovalId(approvalId);
    lastStatus.setEndTime(now);
    statusHistoryRepository.save(lastStatus);

    ApprovalStatusHistory currentStatus = statusHistoryRepository.save(
        new ApprovalStatusHistory(approvalId, request.getStatus(), request.getUpdatedBy(),
            request.getMessageId(), now));

    jmsUtil.sendMessage(new ApprovalStatusUpdateEvent(approval.getId(),
        approval.getType(), approval.getTypeId(), approval.getStatus(),
        approval.getUpdatedBy(), currentStatus.getStartTime()));

    return null;
  }

}
