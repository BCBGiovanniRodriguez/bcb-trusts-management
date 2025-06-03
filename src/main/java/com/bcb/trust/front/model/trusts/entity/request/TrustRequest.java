package com.bcb.trust.front.model.trusts.entity.request;

import com.bcb.trust.front.model.trusts.entity.system.SystemUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "TrustRequest")
public class TrustRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RequestId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RegisteredBy", referencedColumnName = "UserId")
    private SystemUser RegisteredBy;

    
}
