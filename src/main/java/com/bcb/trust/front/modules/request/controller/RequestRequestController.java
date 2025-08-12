package com.bcb.trust.front.modules.request.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogAddressEntityRepository;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.repository.RequestEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@RestController
@RequestMapping("/api/request")
public class RequestRequestController {

    @Autowired
    private SystemUserEntityRepository userEntityRepository;
    
    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    @Autowired
    private CatalogPersonEntityRepository personEntityRepository;

    @Autowired
    private CatalogAddressEntityRepository addressEntityRepository;

    @Autowired
    private RequestEntityRepository requestEntityRepository;
    
    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/request")
    public String create(@RequestBody Map<String, Object> data, Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SystemUserEntity systemUserEntity = userEntityRepository.findByNickname(userDetails.getUsername());

            for (String element : data.keySet()) {
                System.out.println(element);
            }
            LocalDateTime now = LocalDateTime.now();

            Map<String, Object> personMap = (Map<String, Object>) data.get("person");
            Map<String, Object> addressMap = (Map<String, Object>) data.get("address");

            Integer personType = Integer.parseInt(personMap.get("type").toString());

            CatalogPersonEntity personEntity = new CatalogPersonEntity();
            if (CatalogPersonEntity.TYPE_PERSON == personType) {
                personEntity.setFirstName((String) personMap.get("firstName"));
                personEntity.setSecondName((String) personMap.get("secondName"));
                personEntity.setLastName((String) personMap.get("lastName"));
                personEntity.setSecondLastName((String) personMap.get("secondLastName"));
                personEntity.setGender((Integer) personMap.get("gender"));
                String birthDateString = personMap.get("birthDate").toString();
                personEntity.setBirthDate(LocalDate.parse(birthDateString, isoFormatter));
                personEntity.setCurp((String) personMap.get("curp"));
            } else if(CatalogPersonEntity.TYPE_ENTERPRISE == personType) {
                personEntity.setFirstName("");
                personEntity.setSecondName("");
                personEntity.setLastName("");
                personEntity.setSecondLastName("");
                personEntity.setFullName((String) personMap.get("fullName"));
                personEntity.setGender(CatalogPersonEntity.GENDER_UNKWON);
                personEntity.setBirthDate(LocalDate.now());
                personEntity.setCurp("XAXX000000XXXXXX00");
                
            }
            personEntity.setRfc((String) personMap.get("rfc"));
            personEntity.setType(personType);
            personEntity.setCreated(now);
            personEntityRepository.saveAndFlush(personEntity);

            CatalogAddressEntity addressEntity = new CatalogAddressEntity();
            addressEntity.setInternalNumber(addressMap.get("internalNumber").toString());
            addressEntity.setExternalNumber(addressMap.get("externalNumber").toString());
            addressEntity.setStreet(addressMap.get("street").toString());
            addressEntity.setZipcode(addressMap.get("zipcode").toString());
            addressEntity.setColonyId(Long.parseLong(addressMap.get("colonyId").toString()));
            addressEntity.setFullAddress(addressMap.get("fullAddress").toString());
            addressEntity.setCreated(now);
            addressEntityRepository.saveAndFlush(addressEntity);

            RequestRequestEntity requestEntity = new RequestRequestEntity();
            requestEntity.setNumber("10");
            requestEntity.setTrustChange(Integer.parseInt(data.get("trustChange").toString()));
            requestEntity.setTrustChangeTrust(data.get("trustChangeTrust").toString());
            requestEntity.setWasRefered(Integer.parseInt(data.get("wasRefered").toString()));
            requestEntity.setWasReferedBy(Integer.parseInt(data.get("wasReferedBy").toString()));
            requestEntity.setWasReferedByFullName(data.get("wasReferedByFullName").toString());
            Long trustTypeId = Long.parseLong(data.get("type").toString());
            Optional<TrustTrustTypeEntity> result = trustTypeRepository.findById(trustTypeId);
            if (result.isPresent()) {
                TrustTrustTypeEntity trustTypeEntity = result.get();
                requestEntity.setTrustTypeEntity(trustTypeEntity);
            }
            requestEntity.setAddressEntity(addressEntity);
            requestEntity.setPersonEntity(personEntity);
            requestEntity.setState(1);
            requestEntity.setStatus(RequestRequestEntity.STATUS_ENABLED);
            requestEntity.setRegisteredBy(systemUserEntity);
            requestEntity.setCreated(now);

            requestEntityRepository.saveAndFlush(requestEntity);

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", requestEntity.toMap());
            
            jsonResponse = mapper.writeValueAsString(resultMap);
            
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemUserController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
}
