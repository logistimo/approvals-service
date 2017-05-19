package com.logistimo.approval.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
@Entity
@Table(name = "approver_queue")
public class ApproverQueue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "approval_id")
  private String approvalId;

  @Column(name = "approver_status")
  private String approverStatus;

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

}
