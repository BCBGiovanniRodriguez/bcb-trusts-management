package com.bcb.trust.front.model.trusts.entity.trust;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TrustTrusts")
public class TrustTrust {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TrustId;

    private String Name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TrustType", referencedColumnName = "TrustTypeId")
    private TrustTrustType Type;

    

    
}
