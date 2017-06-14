package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.Approver;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import java.util.Optional;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nitisha.khandelwal on 11/05/17.
 */

@Component
public class GetApprovalAction {

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApproverQueueRepository approverQueueRepository;

  @Autowired
  private IApprovalAttributesRepository attributesRepository;

  @Autowired
  private IApprovalDomainMappingRepository domainMappingRepository;

  private ModelMapper mapper = new ModelMapper();

  public ApprovalResponse invoke(String approvalId) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, String.format(APPROVAL_NOT_FOUND, approvalId));
    }

    ApprovalResponse response = mapper.map(approval, ApprovalResponse.class);

    Optional.ofNullable(approverQueueRepository.findByApprovalId(approvalId)).ifPresent(
        l -> l.forEach(item -> response.getApprovers().add(mapper.map(item, Approver.class))));

    Optional.ofNullable(domainMappingRepository.findByApprovalId(approvalId)).ifPresent(
        l -> l.forEach(item -> response.getDomains().add(item.getDomainId())));

    Optional.ofNullable(attributesRepository.findByApprovalId(approvalId)).ifPresent(
        l -> l.forEach(item -> response.getAttributes().put(item.getKey(), item.getValue())));

    return response;
  }
}
