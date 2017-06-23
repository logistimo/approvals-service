package com.logistimo.approval.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "approval_status_history")
public class ApprovalStatusHistory {

  public ApprovalStatusHistory(String approvalId, String status, String updatedBy,
      String messageId, Date startTime) {
    this.approvalId = approvalId;
    this.status = status;
    this.updatedBy = updatedBy;
    this.messageId = messageId;
    this.startTime = startTime;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "approval_id")
  private String approvalId;

  @Column(name = "status")
  private String status;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "message_id")
  private String messageId;

  @CreationTimestamp
  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @Version
  @Column(name = "version")
  private Long version;
}
