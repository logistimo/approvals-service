package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.Approver;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
  private IApprovalAttributesRepository approvalAttributesRepository;

  @Autowired
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  private ModelMapper mapper = new ModelMapper();

  public ApprovalResponse invoke(String approvalId) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, String.format(APPROVAL_NOT_FOUND, approvalId));
    }

    ApprovalResponse response = mapper.map(approval, ApprovalResponse.class);

    setApprovers(approvalId, response);
    setApprovalAttributes(approvalId, response);
    setApprovalDomains(approvalId, response);

    return response;
  }

  private void setApprovers(String approvalId, ApprovalResponse response) {
    List<ApproverQueue> approverQueues = approverQueueRepository.findByApprovalId(approvalId);
    if (!CollectionUtils.isEmpty(approverQueues)) {
      List<Approver> approvers = new ArrayList<>();
      for (ApproverQueue approverQueue : approverQueues) {
        approvers.add(mapper.map(approverQueue, Approver.class));
      }
      response.setApprovers(approvers);
    }
  }

  private void setApprovalDomains(String approvalId, ApprovalResponse response) {
    List<ApprovalDomainMapping> domainMappings = approvalDomainMappingRepository
        .findByApprovalId(approvalId);
    if (!CollectionUtils.isEmpty(domainMappings)) {
      List<Long> domains = new ArrayList<>();
      for (ApprovalDomainMapping domainMapping : domainMappings) {
        domains.add(domainMapping.getDomainId());
      }
      response.setDomains(domains);
    }
  }

  private void setApprovalAttributes(String approvalId, ApprovalResponse response) {
    List<ApprovalAttributes> approvalAttributes = approvalAttributesRepository
        .findByApprovalId(approvalId);
    if (!CollectionUtils.isEmpty(approvalAttributes)) {
      Map<String, String> attributes = new HashMap<>();
      for (ApprovalAttributes approvalAttribute : approvalAttributes) {
        attributes.put(approvalAttribute.getKey(), approvalAttribute.getValue());
      }
      response.setAttributes(attributes);
    }
  }

}
