package com.bcb.trust.front.model.trusts.entity;

import java.util.Date;
import java.util.Set;

import com.bcb.trust.front.model.trusts.enums.ProcessStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_processes")
public class ProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processId;

    private ProcessTypeEnum type;

    private ProcessStateEnum state;

    private long totalElements;

    private long elementsProcessed;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private double processPercent;

    private String path;

    private Date createdAt;

    @OneToMany(mappedBy = "process")
    private Set<ProcessDetailEntity> details;

    public ProcessEntity() {
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public ProcessTypeEnum getType() {
        return type;
    }

    public void setType(ProcessTypeEnum processType) {
        this.type = processType;
    }

    public ProcessStateEnum getState() {
        return state;
    }

    public void setState(ProcessStateEnum processState) {
        this.state = processState;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getElementsProcessed() {
        return elementsProcessed;
    }

    public void setElementsProcessed(long elementsProcessed) {
        this.elementsProcessed = elementsProcessed;
    }

    public double getProcessPercent() {
        return processPercent;
    }

    public void setProcessPercent(double processPercentage) {
        this.processPercent = processPercentage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<ProcessDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(Set<ProcessDetailEntity> details) {
        this.details = details;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created) {
        this.createdAt = created;
    }

}
