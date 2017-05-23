package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Utility.*;
import static com.logistimo.approval.utils.Utility.getApprovalDomainMappings;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.DomainUpdateRequest;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by nitisha.khandelwal on 23/05/17.
 */

public class UpdateApprovalDomainMappingActionTest {

  public static final String APPROVAL_ID = "A123";

  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  @InjectMocks
  private UpdateApprovalDomainMappingAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(approvalRepository.findOne(APPROVAL_ID)).thenReturn(getApproval());
    when(approvalDomainMappingRepository.findByApprovalId("A123"))
        .thenReturn(getApprovalDomainMappings());
  }

  @Test
  public void updateApprovalDomainMappingTest() {
    DomainUpdateRequest request = getDomainUpdateRequest();
    action.invoke(APPROVAL_ID, request);
    verify(approvalRepository, times(1)).save(any(Approval.class));
    verify(approvalDomainMappingRepository, times(1)).deleteByApprovalId(APPROVAL_ID);
    verify(approvalDomainMappingRepository, times(request.getDomains().size()))
        .save(any(ApprovalDomainMapping.class));
  }

  @Test(expected = BaseException.class)
  public void updateApprovalDomainMappingThrowsExceptionTest() throws IOException {
    action.invoke("A456", getDomainUpdateRequest());
  }
}
