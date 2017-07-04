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
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Created by nitisha.khandelwal on 30/06/17.
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

  public Task(String approvalId, Long queueId, String type, Date runTime, String status) {
    this.approvalId = approvalId;
    this.queueId = queueId;
    this.type = type;
    this.runTime = runTime;
    this.status = status;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "approval_id", nullable = false)
  private String approvalId;

  @Column(name = "queue_id")
  private Long queueId;

  @Column(name = "type")
  private String type;

  @Column(name = "run_time")
  private Date runTime;

  @Column(name = "status")
  private String status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
}
