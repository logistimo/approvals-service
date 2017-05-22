package com.logistimo.approval.actions;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetApprovalActionTest {

  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApproverQueueRepository approverQueueRepository;

  @Mock
  private IApprovalAttributesRepository approvalAttributesRepository;

  @Mock
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  @InjectMocks
  private GetApprovalAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(approvalRepository.findOne("A123")).thenReturn(getApproval());
    when(approverQueueRepository.findByApprovalId("A123")).thenReturn(getApproverQueue());
    when(approvalAttributesRepository.findByApprovalId("A123")).thenReturn(getApprovalAttributes());
    when(approvalDomainMappingRepository.findByApprovalId("A123"))
        .thenReturn(getApprovalDomainMappings());

  }

  @Test
  public void getApprovalTest() {
    ApprovalResponse response = action.invoke("A123");
    assertEquals(response.getApprovers().size(), 1);
    assertEquals(response.getDomains().size(), 1);
    assertEquals(response.getAttributes().size(), 1);
  }

  @Test(expected = BaseException.class)
  public void getApprovalThrowsExceptionTest() {
    action.invoke("A456");
  }

  private List<ApprovalAttributes> getApprovalAttributes() {
    ApprovalAttributes attribute1 = new ApprovalAttributes();
    attribute1.setApprovalId("A123");
    attribute1.setKey("kioskId");
    attribute1.setValue("K001");
    return Collections.singletonList(attribute1);
  }

  private List<ApproverQueue> getApproverQueue() {
    ApproverQueue queue = new ApproverQueue();
    queue.setId(1L);
    queue.setUserId("U001");
    queue.setApprovalId("A123");
    queue.setApproverStatus("QUEUED");
    return Collections.singletonList(queue);
  }

  private List<ApprovalDomainMapping> getApprovalDomainMappings() {
    ApprovalDomainMapping mapping1 = new ApprovalDomainMapping();
    mapping1.setApprovalId("A123");
    mapping1.setDomainId(16L);
    return Collections.singletonList(mapping1);
  }

  private Approval getApproval() {
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
}
