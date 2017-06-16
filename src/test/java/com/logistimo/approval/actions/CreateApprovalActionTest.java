package com.logistimo.approval.actions;

import static com.logistimo.approval.utils.Utility.getApproval;
import static com.logistimo.approval.utils.Utility.getApprovalFromDB;
import static com.logistimo.approval.utils.Utility.getApprovalRequest;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.logistimo.approval.conversationclient.IConversationClient;
import com.logistimo.approval.conversationclient.request.PostMessageResponse;
import com.logistimo.approval.conversationclient.response.PostMessageRequest;
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
import java.io.IOException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
  private IConversationClient conversationClient;

  @InjectMocks
  private CreateApprovalAction action;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    PostMessageResponse postMessageResponse = new PostMessageResponse();
    postMessageResponse.setConversationId("C001");
    postMessageResponse.setMessageId("M001");
    when(conversationClient.postMessage(any(PostMessageRequest.class), anyString(), anyString()))
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

    final ArgumentCaptor<PostMessageRequest> captor = ArgumentCaptor
        .forClass(PostMessageRequest.class);

    verify(conversationClient, times(1)).postMessage(captor.capture(), anyString(), anyString());
    assertEquals(captor.getValue().getData(), request.getMessage());
    assertEquals(captor.getValue().getDomainId(), request.getSourceDomainId());
    assertEquals(captor.getValue().getUserId(), request.getRequesterId());

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
