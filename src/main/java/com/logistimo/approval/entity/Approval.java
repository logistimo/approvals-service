package com.logistimo.approval.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
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

  @Column(name = "approver_queues_count")
  private Long approverQueuesCount;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "expire_at")
  private Date expireAt;

  @Version
  @Column(name = "version")
  private Long version;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "approval_id")
  private Set<ApproverQueue> approvers;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "approval_id")
  private Set<ApprovalAttributes> attributes;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "approval_id")
  private Set<ApprovalDomainMapping> domains;
}
