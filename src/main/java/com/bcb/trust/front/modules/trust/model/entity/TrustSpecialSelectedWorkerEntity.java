package com.bcb.trust.front.modules.trust.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_special_selected_workers")
public class TrustSpecialSelectedWorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectedWorkerId;

    private Integer contractNumber;

    private String account;

    private String name;

    public TrustSpecialSelectedWorkerEntity() {
    }

    public Long getSelectedWorkerId() {
        return selectedWorkerId;
    }

    public void setSelectedWorkerId(Long selectedWorkerId) {
        this.selectedWorkerId = selectedWorkerId;
    }

    public Integer getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrustSpecialSelectedWorkerEntity [selectedWorkerId=" + selectedWorkerId + ", contractNumber="
                + contractNumber + ", account=" + account + ", name=" + name + "]";
    }

}
