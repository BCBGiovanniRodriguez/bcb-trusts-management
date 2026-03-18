package com.bcb.trust.front.modules.request.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.request.model.entity.UniquePerson;
import com.bcb.trust.front.modules.request.model.repository.UniquePersonRepository;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    @GetMapping("/person-id")
    public String get(@RequestParam(required = false) String name, @RequestParam(required = false) String rfc) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        List<CatalogPersonEntity> personList = new ArrayList<>();
        List<CatalogPersonEntity> resultList = new ArrayList<>();
        Set<Long> personIds = new HashSet<>();

        try {

            if (name != null && name != "") {
                personList.addAll(catalogPersonEntityRepository.findByFirstNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findBySecondNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findByLastNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findBySecondLastNameStartsWith(name));
            }

            if (rfc != null && rfc != "") {
                personList.addAll(catalogPersonEntityRepository.findByRfcStartsWith(rfc));
            }

            // Unique Id's
            for (CatalogPersonEntity entity : personList) {
                personIds.add(entity.getPersonId());
            }

            

            // Added, now filter by id
            for (CatalogPersonEntity person : personList) {
                if (!resultList.contains(person)) {

                    resultList.add(person);
                }
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", resultList);

            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en PersonIdRestController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PostMapping("/person-id")
    public String post(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
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
            
            catalogPersonEntityRepository.saveAndFlush(person);

            UniquePerson uniquePerson = new UniquePerson();
            uniquePerson.setPersonEntity(person);
            uniquePerson.setCreatedAt(LocalDateTime.now());

            uniquePersonRepository.saveAndFlush(uniquePerson);

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", uniquePerson.toMap());

            jsonResponse = mapper.writeValueAsString(resultMap);
            
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en PersonIdRestController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
    

}
