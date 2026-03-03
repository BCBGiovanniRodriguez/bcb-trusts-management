package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrusteeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_persons")
public class CatalogPersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String firstName;

    private String secondName;

    private String lastName;

    private String secondLastName;

    private String fullName;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer gender;

    private LocalDate birthdate;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer foreignStatus;

    private Long nationalityId;

    private String curp;

    private String rfc;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer maritalStatus;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer type;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "personEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestRequestEntity> requestSet;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrustorEntity> trustorList = new ArrayList<>();

    @OneToMany(mappedBy = "personEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrusteeEntity> trusteeList = new ArrayList<>();

    public static final Integer GENDER_FEMALE = 1;

    public static final Integer GENDER_MALE = 2;

    public static final Integer GENDER_UNKNOWN = 3;

    public static final String[] genders = { "Seleccione Opción", "Femenino", "Masculino", "No especificado" };

    public static final Integer TYPE_PERSON = 1;

    public static final Integer TYPE_ENTERPRISE = 2;

    public static final String[] types = { "Seleccione Opción", "Persona Física", "Persona Moral" };

    public static final Integer FOREIGN_STATUS_CITIZEN = 1;

    public static final Integer FOREIGN_STATUS_FOREIGNER = 2;

    public static final String[] foreignStatuses = { "Seleccione Opción", "Nacional", "Extranjero" };

    public static final Integer MARITAL_STATUS_SINGLE = 1;

    public static final Integer MARITAL_STATUS_MARRIED = 2;
    
    public static final Integer MARITAL_STATUS_WIDOWED = 3;
    
    public static final Integer MARITAL_STATUS_DIVORCED = 4;

    public static final Integer MARITAL_STATUS_UNKNOWN = 5;

    public static final String[] maritalStatuses = { "Seleccione Opción", "Soltero(a)", "Casado(a)", "Viudo(a)", "Divorciado(a)", "No especificado" };

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

        if (this.fullName != null && !this.fullName.equals("")) {
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthDate) {
        this.birthdate = birthDate;
    }

    public Long getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Long nationalityId) {
        this.nationalityId = nationalityId;
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

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer status) {
        this.maritalStatus = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    public Map<String, Object> toMap() {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> map = new HashMap<>();
        map.put("person_id", this.personId);
        map.put("first_name", this.firstName);
        map.put("second_name", this.secondName);
        map.put("last_name", this.lastName);
        map.put("second_last_name", this.secondLastName);
        map.put("full_name", this.fullName);
        map.put("gender", this.gender);
        map.put("birth_date", this.birthdate != null ? this.birthdate.format(DateTimeFormatter.ISO_DATE) : null);
        map.put("curp", this.curp);
        map.put("rfc", this.rfc);
        map.put("foreign_status", this.foreignStatus);
        map.put("type", this.type);
        map.put("marital_status", this.maritalStatus);
        map.put("created_at", this.createdAt != null ? this.createdAt.format(isoFormatter) : null);

        return map;
    }

    public String getGenderAsString() throws Exception {
        if (this.gender < 0 || (this.gender > CatalogPersonEntity.genders.length)) {
            throw new Exception("CatalogPersonEntity::getGenderAsString::Valor de género fuera del rango");
        }

        return CatalogPersonEntity.genders[this.gender];
    }

    public String getMaritalStatusAsString() throws Exception {
        if (this.maritalStatus < 0 || (this.maritalStatus > CatalogPersonEntity.maritalStatuses.length)) {
            throw new Exception("CatalogPersonEntity::getMaritalStatusAsString::Valor de estatus fuera del rango");
        }

        return CatalogPersonEntity.maritalStatuses[this.maritalStatus];
    }

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogPersonEntity.types.length)) {
            throw new Exception("CatalogPersonEntity::getTypeAsString::Valor de tipo fuera del rango");
        }

        return CatalogPersonEntity.types[this.type];
    }

    public String getForeignStatusAsString() throws Exception {
        if (this.foreignStatus < 0 || (this.foreignStatus > CatalogPersonEntity.foreignStatuses.length)) {
            throw new Exception("CatalogPersonEntity::getForeignStatusAsString::Valor de tipo fuera del rango");
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
