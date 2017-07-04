package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.TestUtility.getApproval;
import static com.logistimo.approval.utils.TestUtility.getApprovalFromDB;
import static com.logistimo.approval.utils.TestUtility.getApprovalRequest;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalAttributes;
import com.logistimo.approval.entity.ApprovalDomainMapping;
import com.logistimo.approval.exception.BaseException;
import com.logistimo.approval.models.ApprovalRequest;
import com.logistimo.approval.models.ApprovalResponse;
import com.logistimo.approval.repository.IApprovalAttributesRepository;
import com.logistimo.approval.repository.IApprovalDomainMappingRepository;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import com.logistimo.approval.repository.ITaskRepository;
import com.logistimo.approval.utils.Utility;
import java.io.IOException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by nitisha.khandelwal on 25/05/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateApprovalActionTest {

  @Mock
  private IApprovalRepository approvalRepository;

  @Mock
  private IApprovalStatusHistoryRepository approvalStatusHistoryRepository;

  @Mock
  private IApproverQueueRepository approverQueueRepository;

  @Mock
  private IApprovalAttributesRepository approvalAttributesRepository;

  @Mock
  private IApprovalDomainMappingRepository approvalDomainMappingRepository;

  @Mock
  private ITaskRepository taskRepository;

  @Mock
  private Utility utility;

  @InjectMocks
  private CreateApprovalAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    PostMessageResponse postMessageResponse = new PostMessageResponse();
    postMessageResponse.setConversationId("C001");
    postMessageResponse.setMessageId("M001");
    when(utility.addMessageToConversation(anyString(), anyString(), anyString(), any(Long.class)))
        .thenReturn(postMessageResponse);
    when(approvalRepository.save(any(Approval.class))).thenReturn(getApprovalFromDB());
  }

  @Test
  public void createApprovalTest() {

    ApprovalRequest request = getApprovalRequest();
    ApprovalResponse response = action.invoke(getApprovalRequest());

    verify(approvalRepository, times(1))
        .findApprovedOrPendingApprovalsByTypeAndTypeId(anyString(), anyString());
    verify(approvalAttributesRepository, times(request.getAttributes().size()))
        .save(any(ApprovalAttributes.class));
    verify(approvalDomainMappingRepository, times(request.getDomains().size()))
        .save(any(ApprovalDomainMapping.class));
    verify(approvalAttributesRepository, times(request.getApprovers().size()))
        .save(any(ApprovalAttributes.class));

    verify(utility, times(1)).addMessageToConversation(anyString(), anyString(), anyString(),
        any(Long.class));

    assertEquals(response.getApprovalId(), getApprovalFromDB().getId());
    assertEquals(response.getType(), request.getType());
    assertEquals(response.getTypeId(), request.getTypeId());
    assertEquals(response.getRequesterId(), request.getRequesterId());
    assertEquals(response.getStatus(), getApprovalFromDB().getStatus());
    assertEquals(response.getConversationId(), getApprovalFromDB().getConversationId());
    assertEquals(response.getSourceDomainId(), request.getSourceDomainId());
    assertEquals(response.getDomains(), request.getDomains());
    assertEquals(response.getAttributes(), request.getAttributes());
    assertEquals(response.getCreatedAt(), getApprovalFromDB().getCreatedAt());
    assertEquals(response.getUpdatedAt(), getApprovalFromDB().getUpdatedAt());
  }

  @Test(expected = BaseException.class)
  public void approvalAlreadyExists() throws IOException {

    ApprovalRequest request = new ApprovalRequest();
    request.setType("order");
    request.setTypeId("O001");

    when(approvalRepository.findApprovedOrPendingApprovalsByTypeAndTypeId("order", "O001"))
        .thenReturn(Collections.singletonList(getApproval()));

    action.invoke(request);

    verify(approvalRepository, times(1))
        .findApprovedOrPendingApprovalsByTypeAndTypeId(anyString(), anyString());
  }
}
