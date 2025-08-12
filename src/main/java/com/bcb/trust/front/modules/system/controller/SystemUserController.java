package com.bcb.trust.front.modules.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.common.enums.CommonPersonTypeEnum;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/system")
public class SystemUserController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    @Autowired
    private SystemProfileRepository systemProfileRepository;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/user")
    public String postMethodName(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            for (String element : data.keySet()) {
                System.out.println(element);
            }

            Optional<SystemProfileEntity> result = systemProfileRepository.findById(Long.parseLong(data.get("profileId").toString()));

            if (!result.isPresent()) {
                throw new Exception("Identificador de Perfil de sistema no encontrado");
            } else {
                LocalDateTime now = LocalDateTime.now();
                SystemProfileEntity profileEntity = result.get();
                @SuppressWarnings("unchecked")
                Map<String, Object> personMap = (Map<String, Object>) data.get("person");

                CatalogPersonEntity catalogPersonEntity = new CatalogPersonEntity();
                catalogPersonEntity.setFirstName((String) personMap.get("firstName"));
                catalogPersonEntity.setSecondName((String) personMap.get("secondName"));
                catalogPersonEntity.setLastName((String) personMap.get("lastName"));
                catalogPersonEntity.setSecondLastName((String) personMap.get("secondLastName"));
                catalogPersonEntity.setGender((String) personMap.get("gender"));
                String birthDateString = personMap.get("birthDate").toString();
                catalogPersonEntity.setBirthDate(LocalDate.parse(birthDateString, isoFormatter));
                catalogPersonEntity.setRfc((String) personMap.get("rfc"));
                catalogPersonEntity.setCurp((String) personMap.get("curp"));
                catalogPersonEntity.setType(1);
                catalogPersonEntity.setCreated(now);
                catalogPersonEntityRepository.save(catalogPersonEntity);

                SystemUserEntity systemUserEntity = new SystemUserEntity();
                systemUserEntity.setEmail((String) data.get("email"));
                systemUserEntity.setNickname((String) data.get("nickname"));
                systemUserEntity.setAccess(encoder.encode("test"));
                systemUserEntity.setProfile(profileEntity);
                systemUserEntity.setPerson(catalogPersonEntity);;
                systemUserEntity.setStatus(1);
                systemUserEntity.setCreated(now);
                systemUserEntityRepository.saveAndFlush(systemUserEntity);

                resultMap.put("status", 1);
                resultMap.put("message", "Petición Correcta");
                resultMap.put("data", systemUserEntity.toMap());
                
                jsonResponse = mapper.writeValueAsString(resultMap);
            }

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemUserController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
}
