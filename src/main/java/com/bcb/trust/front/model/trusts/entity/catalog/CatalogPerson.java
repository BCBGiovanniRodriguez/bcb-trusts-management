package com.bcb.trust.front.model.trusts.entity.catalog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CatalogPerson")
public class CatalogPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PersonId;

    private String FirstName;

    private String SecondName;

    private String LastName;

    private String SecondLastName;

    private String Gender;

    private LocalDate BirthDate;

    private String Curp;

    private String Rfc;

    private LocalDateTime Created;

    public CatalogPerson() {
    }

    public Long getPersonId() {
        return PersonId;
    }

    public void setPersonId(Long personId) {
        PersonId = personId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSecondLastName() {
        return SecondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        SecondLastName = secondLastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public LocalDate getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        BirthDate = birthDate;
    }

    public String getCurp() {
        return Curp;
    }

    public void setCurp(String curp) {
        Curp = curp;
    }

    public String getRfc() {
        return Rfc;
    }

    public void setRfc(String rfc) {
        Rfc = rfc;
    }

    public LocalDateTime getCreated() {
        return Created;
    }

    public void setCreated(LocalDateTime created) {
        Created = created;
    }

    @Override
    public String toString() {
        return "CatalogPerson [PersonId=" + PersonId + ", FirstName=" + FirstName + ", SecondName=" + SecondName
                + ", LastName=" + LastName + ", SecondLastName=" + SecondLastName + ", Gender=" + Gender
                + ", BirthDate=" + BirthDate + ", Curp=" + Curp + ", Rfc=" + Rfc + ", Created=" + Created + "]";
    }
    
}
