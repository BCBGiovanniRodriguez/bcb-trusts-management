package com.bcb.trust.front.modules.request.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.request.model.entity.UniquePerson;
import com.bcb.trust.front.modules.request.model.repository.UniquePersonRepository;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/request")
public class PersonIdRestController {

    @Autowired
    private UniquePersonRepository uniquePersonRepository;

    @Autowired
    private CatalogPersonEntityRepository personEntityRepository;

    @GetMapping("/person-id")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/person-id")
    public String post(@RequestBody Map<String, Object> data) {
        
        try {
            Integer typeInteger = Integer.parseInt(data.get("type").toString());
            Integer maritalStatusInteger = Integer.parseInt(data.get("maritalStatus").toString());
            Integer foreignStatusInteger = Integer.parseInt(data.get("foreignStatus").toString());
            Long nationalityIdInteger = Long.parseLong(data.get("nationalityId").toString());
            LocalDate birthDate = LocalDate.parse(data.get("birthDate").toString());
            Integer genderInteger = Integer.parseInt(data.get("gender").toString());

            CatalogPersonEntity person = new CatalogPersonEntity();
            person.setType(typeInteger);
            person.setMaritalStatus(maritalStatusInteger);
            person.setForeignStatus(foreignStatusInteger);
            person.setNationalityId(nationalityIdInteger);

            person.setFirstName(data.get("firstName").toString());
            person.setSecondName(data.get("secondName").toString());
            person.setLastName(data.get("lastName").toString());
            person.setSecondLastName(data.get("secondLastName").toString());
            person.setFullName(data.get("fullName").toString());
            person.setGender(genderInteger);
            person.setBirthdate(birthDate);
            person.setCurp(data.get("curp").toString());
            person.setRfc(data.get("rfc").toString());
            person.setCreatedAt(LocalDateTime.now());
            
            personEntityRepository.saveAndFlush(person);

            UniquePerson uniquePerson = new UniquePerson();
            uniquePerson.setPersonEntity(person);
            uniquePerson.setCreatedAt(LocalDateTime.now());

            uniquePersonRepository.saveAndFlush(uniquePerson);
            
        } catch (Exception e) {
            System.out.println();
        }
        
        return "";
    }
    
    

}
