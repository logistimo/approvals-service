package com.logistimo.approval.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Created by nitisha.khandelwal on 08/05/17.
 */

@Data
@Entity
@Table(name = "approvals")
public class Approval {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  @Column(name = "type")
  private String type;

  @Column(name = "type_id")
  private String typeId;

  @Column(name = "status")
  private String status;

  @Column(name = "requester_id")
  private String requesterId;

  @Column(name = "source_domain_id")
  private Long sourceDomainId;

  @Column(name = "conversation_id")
  private String conversationId;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "expire_at")
  private Date expireAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Date updatedAt;
}
