package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrusteeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_persons")
public class CatalogPersonEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String firstName;

    private String secondName;

    private String lastName;

    private String secondLastName;

    private String fullName;

    private Integer gender;

    private LocalDate birthDate;

    private String curp;

    private String rfc;

    private Integer foreignStatus;

    private Integer type;

    private Integer status;

    private LocalDateTime created;

    @OneToMany(mappedBy = "personEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestRequestEntity> requestSet;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrustorEntity> trustorList = new ArrayList<>();

    @OneToMany(mappedBy = "personEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrusteeEntity> trusteeList = new ArrayList<>();

    public static final Integer GENDER_FEMALE = 1;

    public static final Integer GENDER_MALE = 2;

    public static final Integer GENDER_UNKWON = 3;

    public static final String[] genders = {"Seleccione Opción", "Femenino", "Masculino", "No especificado"};

    public static final Integer TYPE_PERSON = 1;

    public static final Integer TYPE_ENTERPRISE = 2;

    public static final String[] types = {"Seleccione Opción", "Persona Física", "Persona Moral"};

    public static final Integer FOREIGN_STATUS_CITIZEN = 1;

    public static final Integer FOREIGN_STATUS_FOREIGNER = 2;

    public static final String[] foreignStatuses = {"Seleccione Opción", "Nacional", "Extranjero"};

    public CatalogPersonEntity() {
        this.requestSet = new HashSet<>();
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getFullName() {
        String fullName = "";
        
        if(this.fullName != null && !this.fullName.equals("")) {
            fullName = this.fullName;
        } else {
            fullName = this.lastName != null ? fullName += this.lastName + " " : "";
            fullName = this.secondLastName != null ? fullName += this.secondLastName + " " : "";
            fullName = this.firstName != null ? fullName += this.firstName + " " : "";
            fullName = this.secondName != null ? fullName += this.secondName + " " : "";
        }
        
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Integer getForeignStatus() {
        return foreignStatus;
    }

    public void setForeignStatus(Integer foreignStatus) {
        this.foreignStatus = foreignStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("personId", this.personId);
        map.put("firstName", this.firstName);
        map.put("secondName", this.secondName);
        map.put("lastName", this.lastName);
        map.put("secondLastName", this.secondLastName);
        map.put("fullName", this.fullName);
        map.put("gender", this.gender);
        map.put("birthDate", this.birthDate);
        map.put("curp", this.curp);
        map.put("rfc", this.rfc);
        map.put("foreignStatus", this.foreignStatus);
        map.put("type", this.type);
        map.put("status", this.status);
        map.put("created", this.created);

        return map;
    }

    public String getGenderAsString() throws Exception {
        if (this.gender < 0 || (this.gender > CatalogPersonEntity.genders.length)) {
            throw new Exception("CatalogPersonEntity::getGenderAsString::Valor de género fuera del rango");
        }

        return CatalogPersonEntity.genders[this.gender];
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("CatalogPersonEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogPersonEntity.types.length)) {
            throw new Exception("CatalogPersonEntity::getStatusAsString::Valor de tipo fuera del rango");
        }

        return CatalogPersonEntity.types[this.type];
    }

    public String getForeignStatusAsString() throws Exception {
        if (this.foreignStatus < 0 || (this.foreignStatus > CatalogPersonEntity.foreignStatuses.length)) {
            throw new Exception("CatalogPersonEntity::getStatusAsString::Valor de tipo fuera del rango");
        }

        return CatalogPersonEntity.foreignStatuses[this.foreignStatus];
    }

    public Set<RequestRequestEntity> getRequestSet() {
        return requestSet;
    }

    public void setRequestSet(Set<RequestRequestEntity> requestSet) {
        this.requestSet = requestSet;
    }

    public List<TrustTrustorEntity> getTrustorList() {
        return trustorList;
    }

    public void setTrustorList(List<TrustTrustorEntity> trustorList) {
        this.trustorList = trustorList;
    }

    public List<TrustTrusteeEntity> getTrusteeList() {
        return trusteeList;
    }

    public void setTrusteeList(List<TrustTrusteeEntity> trusteeList) {
        this.trusteeList = trusteeList;
    }

}
