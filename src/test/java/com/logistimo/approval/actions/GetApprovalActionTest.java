package com.logistimo.approval.actions;

import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.logistimo.approval.utils.Utility.*;
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

}
