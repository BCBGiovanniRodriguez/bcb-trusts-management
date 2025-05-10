package com.bcb.trust.front.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_report_individual_report_account_log")
public class IndividualReportAccountReportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long individualReportAccountLogId;

    private String detail;

    private int state;

    private Date created;

}
