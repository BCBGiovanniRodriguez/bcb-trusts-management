package com.bcb.trust.front.modules.request.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_requests")
public class RequestRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "registeredBy", referencedColumnName = "userId")
    private SystemUserEntity registeredBy;

    private Integer type;

    private Integer state;
    
    private LocalDateTime created;
}
