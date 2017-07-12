package com.logistimo.approval.utils;

import static com.logistimo.approval.utils.Constants.ACTIVE_STATUS;
import static com.logistimo.approval.utils.Constants.EXPIRED_STATUS;
import static com.logistimo.approval.utils.Constants.EXPIRY_TASK;
import static com.logistimo.approval.utils.Constants.STATUS_UPDATED_BY;
import static com.logistimo.approval.utils.Constants.TASK_ACTIVE;
import static com.logistimo.approval.utils.Constants.TASK_DONE;

import com.logistimo.approval.entity.Approval;
import com.logistimo.approval.entity.ApproverQueue;
import com.logistimo.approval.entity.Task;
import com.logistimo.approval.models.ApprovalStatusUpdateEvent;
import com.logistimo.approval.models.ApproverStatusUpdateEvent;
import com.logistimo.approval.repository.IApprovalRepository;
import com.logistimo.approval.repository.IApproverQueueRepository;
import com.logistimo.approval.repository.ITaskRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

  @Scheduled(fixedRate = 1000)
  public void run() {

    Date now = new Date();
    List<Task> tasks = taskRepository.findPendingTasks(now);

    for (Task task : tasks) {

      updateTaskStatus(task, TASK_ACTIVE);

      if (EXPIRY_TASK.equalsIgnoreCase(task.getType())) {

        Approval approval = approvalRepository.findOne(task.getApprovalId());

        List<ApproverQueue> currentApprovers = approverQueueRepository
            .findByApprovalIdAndQueueId(task.getApprovalId(), task.getQueueId());

        List<ApproverQueue> nextApprovers = null;
        List<String> nextApproverIds = new ArrayList<>();

        if (task.getQueueId() < approval.getApproverQueuesCount()) {
          nextApprovers = approverQueueRepository
              .findByApprovalIdAndQueueId(task.getApprovalId(), task.getQueueId() + 1);
          for (ApproverQueue approver : nextApprovers) {
            nextApproverIds.add(approver.getUserId());
          }
        }

        for (ApproverQueue approver : currentApprovers) {

          approver.setApproverStatus(EXPIRED_STATUS);
          approverQueueRepository.save(approver);

          utility.publishApproverStatusUpdateEvent(new ApproverStatusUpdateEvent(
              task.getApprovalId(), approval.getType(), approval.getTypeId(),
              approval.getRequesterId(), approval.getCreatedAt(), approver.getEndTime(),
              (int) TimeUnit.MILLISECONDS.toHours(
                  approver.getEndTime().getTime() - approver.getStartTime().getTime()),
              nextApproverIds, approver.getUserId(), EXPIRED_STATUS));
        }

        if (CollectionUtils.isEmpty(nextApprovers)) {

          approval.setStatus(EXPIRED_STATUS);
          Approval approvalFromDB = approvalRepository.save(approval);

          List<String> approverIds = new ArrayList<>();

          Optional.ofNullable(approverQueueRepository.findByApprovalId(task.getApprovalId()))
              .ifPresent(l -> l.forEach(item -> approverIds.add(item.getUserId())));

          utility.publishApprovalStatusUpdateEvent(new ApprovalStatusUpdateEvent(
              task.getApprovalId(), approval.getType(), approval.getTypeId(),
              approval.getRequesterId(), approverIds, EXPIRED_STATUS, STATUS_UPDATED_BY,
              approvalFromDB.getUpdatedAt()));

        } else {

          for (ApproverQueue approver : nextApprovers) {

            approver.setApproverStatus(ACTIVE_STATUS);
            approverQueueRepository.save(approver);

            utility.publishApproverStatusUpdateEvent(new ApproverStatusUpdateEvent(
                task.getApprovalId(), approval.getType(), approval.getTypeId(),
                approval.getRequesterId(), approval.getCreatedAt(), null,
                (int) TimeUnit.MILLISECONDS.toHours(
                    approver.getEndTime().getTime() - approver.getStartTime().getTime()),
                nextApproverIds, approver.getUserId(), ACTIVE_STATUS));
          }
        }

        updateTaskStatus(task, TASK_DONE);
      }
    }
  }

  private void updateTaskStatus(Task task, String taskDone) {
    task.setStatus(taskDone);
    taskRepository.save(task);
  }
}
