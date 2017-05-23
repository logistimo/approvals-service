package com.logistimo.approval.actions;

import com.logistimo.approval.entity.Approval;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nitisha.khandelwal on 22/05/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetApprovalActionTest {

  public static final String APPROVAL_ID = "A123";
  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApproverQueueRepository approverQueueRepository;

  @Mock
  private IApprovalAttributesRepository attributesRepository;

  @Mock
  private IApprovalDomainMappingRepository domainMappingRepository;

  @InjectMocks
  private GetApprovalAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    when(approverQueueRepository.findByApprovalId(APPROVAL_ID)).thenReturn(getApproverQueue());
    when(attributesRepository.findByApprovalId(APPROVAL_ID)).thenReturn(getApprovalAttributes());
    when(domainMappingRepository.findByApprovalId(APPROVAL_ID))z.thenReturn(getApprovalDomainMappings());
  }

  @Test
  public void getApprovalTest() {
    ApprovalResponse response = action.invoke(APPROVAL_ID);
    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
    verify(approverQueueRepository, times(1)).findByApprovalId(APPROVAL_ID);
    verify(attributesRepository, times(1)).findByApprovalId(APPROVAL_ID);
    verify(domainMappingRepository, times(1)).findByApprovalId(APPROVAL_ID);
    assertEquals(response.getApprovers().size(), 1);
    assertEquals(response.getDomains().size(), 1);
    assertEquals(response.getAttributes().size(), 1);
  }

  @Test(expected = BaseException.class)
  public void getApprovalThrowsExceptionTest() {
    action.invoke("A456");
    verify(approvalRepository, times(1)).findOne(APPROVAL_ID);
  }

}
