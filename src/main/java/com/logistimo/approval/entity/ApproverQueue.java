package com.logistimo.approval.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "approver_queue")
public class ApproverQueue {

  public ApproverQueue(String approvalId, String userId, String approverStatus, String type,
      Date startTime, Date endTime) {
    this.approvalId = approvalId;
    this.userId = userId;
    this.approverStatus = approverStatus;
    this.type = type;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "approval_id")
  private String approvalId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "approver_status")
  private String approverStatus;

  @Column(name = "type")
  private String type;

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

}
