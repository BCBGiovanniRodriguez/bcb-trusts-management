package com.bcb.trust.front.model.trusts.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_process_details")
public class ProcessDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processDetailId;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private ProcessEntity process;

    private String detail;

    @Column(columnDefinition = "TINYINT")
    private Integer state;

    private String filename;

    private Date createdAt;

    public ProcessDetailEntity() {
    }

    public Long getProcessDetailId() {
        return processDetailId;
    }

    public void setProcessDetailId(Long processDetailId) {
        this.processDetailId = processDetailId;
    }

    public ProcessEntity getProcess() {
        return process;
    }

    public void setProcess(ProcessEntity process) {
        this.process = process;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String fileName) {
        this.filename = fileName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created) {
        this.createdAt = created;
    }

}
