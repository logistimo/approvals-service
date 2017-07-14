package com.logistimo.approval.actions;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.DomainUpdateRequest;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.logistimo.approval.utils.Constants.APPROVAL_NOT_FOUND;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Component
public class UpdateApprovalDomainMappingAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  public Void invoke(String approvalId, DomainUpdateRequest request) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, APPROVAL_NOT_FOUND, approvalId);
    }

    if (!StringUtils.isEmpty(request.getSourceDomainId())) {
      approval.setSourceDomainId(request.getSourceDomainId());
      approvalRepository.save(approval);
    }

    if (!CollectionUtils.isEmpty(request.getDomains())) {
      approvalDomainMappingRepository.deleteByApprovalId(approvalId);
      request.getDomains().forEach(item -> approvalDomainMappingRepository.save(
          new ApprovalDomainMapping(approvalId, item)));
    }

    return null;
  }

}
