package com.logistimo.approval.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by nitisha.khandelwal on 10/05/17.
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "approval_attributes")
public class ApprovalAttributes {

  public ApprovalAttributes(String approvalId, String key, String value) {
    this.approvalId = approvalId;
    this.key = key;
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "approval_id")
  private String approvalId;

  @Column(name = "approval_key")
  private String key;

  @Column(name = "approval_value")
  private String value;
}
