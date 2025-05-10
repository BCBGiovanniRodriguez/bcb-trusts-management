package com.bcb.trust.front.model.trusts.entity;

import java.util.Date;
import java.util.Set;

import com.bcb.trust.front.model.trusts.enums.ProcessStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_process")
public class ProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processId;

    private ProcessTypeEnum processType;

    private ProcessStateEnum processState;

    private long totalElements;
    
    private long elementsProcessed;

    private double processPercentage; 

    private String path;

    @OneToMany(mappedBy = "process")
    private Set<ProcessDetailEntity> details;

    private Date created;

    public ProcessEntity() {
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public ProcessTypeEnum getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessTypeEnum processType) {
        this.processType = processType;
    }

    public ProcessStateEnum getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessStateEnum processState) {
        this.processState = processState;
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

    public double getProcessPercentage() {
        return processPercentage;
    }

    public void setProcessPercentage(double processPercentage) {
        this.processPercentage = processPercentage;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
