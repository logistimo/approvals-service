package com.logistimo.approval.utils;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApprovalStatusHistory;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.entity.Task;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.ApproverStatusUpdateEvent;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApprovalStatusHistoryRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import com.logistimo.approval.repository.ITaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.logistimo.approval.utils.Constants.ACTIVE_STATUS;
import static com.logistimo.approval.utils.Constants.EXPIRED_STATUS;
import static com.logistimo.approval.utils.Constants.EXPIRY_TASK;
import static com.logistimo.approval.utils.Constants.SYSTEM_USER;
import static com.logistimo.approval.utils.Constants.TASK_ACTIVE;
import static com.logistimo.approval.utils.Constants.TASK_DONE;

/**
 * Created by nitisha.khandelwal on 28/06/17.
 */

@Component
@ConditionalOnProperty(name = "task.machine", havingValue = "true")
public class ScheduledTask {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

  @Autowired
  private Utility utility;

  @Autowired
  private ITaskRepository taskRepository;

  @Autowired
  private IApprovalRepository approvalRepository;

  @Autowired
  private IApproverQueueRepository approverQueueRepository;

  @Autowired
  private IApprovalStatusHistoryRepository statusHistoryRepository;

  @Scheduled(fixedRate = 30_000)
  public void run() {

    log.info("Inside ScheduledTask");

    Date now = new Date();
    List<Task> tasks = taskRepository.findPendingTasks(now);

    for (Task task : tasks) {
      log.info("Executing task - {}", task);
      updateTaskStatus(task, TASK_ACTIVE);

      if (EXPIRY_TASK.equalsIgnoreCase(task.getType())) {
        Approval approval = approvalRepository.findOne(task.getApprovalId());
        List<ApproverQueue> currentApprovers = approverQueueRepository
            .findByApprovalIdAndQueueId(task.getApprovalId(), task.getQueueId());

        List<String> nextApproverIds = new ArrayList<>();
        List<ApproverQueue> nextApprovers = getNextApprovers(task, approval, nextApproverIds);

        for (ApproverQueue approver : currentApprovers) {
          updateApproverStatus(approver, EXPIRED_STATUS, approval, nextApproverIds,
              approver.getEndTime());
        }

        if (CollectionUtils.isEmpty(nextApprovers)) {
          updateApprovalStatusToExpired(approval);
        } else {
          for (ApproverQueue approver : nextApprovers) {
            updateApproverStatus(approver, ACTIVE_STATUS, approval, nextApproverIds, null);
          }
        }

        updateTaskStatus(task, TASK_DONE);
      }
    }
  }

  private List<ApproverQueue> getNextApprovers(Task task, Approval approval,
      List<String> nextApproverIds) {
    List<ApproverQueue> nextApprovers = null;
    if (task.getQueueId() < approval.getApproverQueuesCount()) {
      nextApprovers = approverQueueRepository
          .findByApprovalIdAndQueueId(task.getApprovalId(), task.getQueueId() + 1);
      for (ApproverQueue approver : nextApprovers) {
        nextApproverIds.add(approver.getUserId());
      }
    }
    return nextApprovers;
  }

  private void updateApproverStatus(ApproverQueue approver, String status, Approval approval,
      List<String> nextApproverIds, Date expiryTime) {
    approver.setApproverStatus(status);
    approverQueueRepository.save(approver);

    utility.publishApproverStatusUpdateEvent(new ApproverStatusUpdateEvent(approval.getId(),
        approval.getType(), approval.getTypeId(), approval.getRequesterId(),
        approval.getCreatedAt(), expiryTime, (int) TimeUnit.MILLISECONDS.toHours(
        approver.getEndTime().getTime() - approver.getStartTime().getTime()),
        nextApproverIds, approver.getUserId(), status));
  }


  private void updateApprovalStatusToExpired(Approval approval) {

    approval.setStatus(EXPIRED_STATUS);
    Approval approvalFromDB = approvalRepository.save(approval);

    List<String> approverIds = new ArrayList<>();
    Optional.ofNullable(approverQueueRepository.findByApprovalId(approval.getId()))
        .ifPresent(l -> l.forEach(item -> approverIds.add(item.getUserId())));

    Date now = new Date();

    ApprovalStatusHistory lastStatus = statusHistoryRepository
        .findLastUpdateByApprovalId(approval.getId());
    lastStatus.setEndTime(now);
    statusHistoryRepository.save(lastStatus);
    statusHistoryRepository.save(new ApprovalStatusHistory(
        approval.getId(), EXPIRED_STATUS, SYSTEM_USER, null, now));

    utility.publishApprovalStatusUpdateEvent(new ApprovalStatusUpdateEvent(approval.getId(),
        approval.getType(), approval.getTypeId(), approval.getRequesterId(), approverIds,
        EXPIRED_STATUS, SYSTEM_USER, approvalFromDB.getUpdatedAt()));
  }

  private void updateTaskStatus(Task task, String taskDone) {
    task.setStatus(taskDone);
    taskRepository.save(task);
  }
}
