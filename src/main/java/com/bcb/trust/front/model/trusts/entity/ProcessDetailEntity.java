package com.bcb.trust.front.model.trusts.entity;

import java.util.Date;
import java.util.Set;

import com.bcb.trust.front.entity.enums.ProcessDetailStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_process_detail")
public class ProcessDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processDetailId;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private ProcessEntity process;

    private String detail;

    private ProcessDetailStateEnum processDetailState;

    private String fileName;

    private Date created;

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

    public ProcessDetailStateEnum getProcessDetailState() {
        return processDetailState;
    }

    public void setProcessDetailState(ProcessDetailStateEnum processDetailState) {
        this.processDetailState = processDetailState;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
        
}
