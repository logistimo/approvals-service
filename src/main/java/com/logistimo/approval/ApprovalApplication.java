package com.logistimo.approval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.logistimo.approval.repository")
public class ApprovalApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApprovalApplication.class, args);
  }
}
