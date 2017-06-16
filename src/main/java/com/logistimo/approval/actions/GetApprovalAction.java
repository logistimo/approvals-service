package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Constants.*;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.models.ApproverResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import java.util.List;
import java.util.Optional;
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
  private IApprovalAttributesRepository attributesRepository;

  @Autowired
  private IApprovalDomainMappingRepository domainMappingRepository;

  public ApprovalResponse invoke(String approvalId) {

    Approval approval = approvalRepository.findOne(approvalId);

    if (approval == null) {
      throw new BaseException(Response.SC_NOT_FOUND, String.format(APPROVAL_NOT_FOUND, approvalId));
    }

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setAmbiguityIgnored(true);

    ApprovalResponse response = mapper.map(approval, ApprovalResponse.class);

    List<ApproverQueue> queues = approverQueueRepository.findByApprovalId(approvalId);

    if (!CollectionUtils.isEmpty(queues)) {
      for (ApproverQueue queue : queues) {
        if (ACTIVE_STATUS.equalsIgnoreCase(queue.getApproverStatus())) {
          response.setActiveApproverType(queue.getType());
        }
        response.getApprovers().add(mapper.map(queue, ApproverResponse.class));
      }
    }

    Optional.ofNullable(domainMappingRepository.findByApprovalId(approvalId)).ifPresent(
        l -> l.forEach(item -> response.getDomains().add(item.getDomainId())));

    Optional.ofNullable(attributesRepository.findByApprovalId(approvalId)).ifPresent(
        l -> l.forEach(item -> response.getAttributes().put(item.getKey(), item.getValue())));

    return response;
  }
}
