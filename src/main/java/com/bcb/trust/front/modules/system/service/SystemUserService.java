package com.bcb.trust.front.modules.system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;

@Service
public class SystemUserService {

    public static final Logger logger = Logger.getLogger(SystemUserService.class.getName());

    @Autowired
    private UserEntityRepository systemUserEntityRepository;

    @Autowired
    private ProfileRepository systemProfileEntityRepository;

    @Autowired
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public CatalogUserEntity createUser(Map<String, Object> data) {
        CatalogUserEntity systemUserEntity = null;
        configureLogger();
        Integer type;
        String fullName;

        try {
            // Unwrap data
            LocalDateTime now = LocalDateTime.now();
            
            @SuppressWarnings("unchecked")
            Map<String, Object> personMap = (Map<String, Object>) data.get("person"); 

            type = Integer.parseInt(personMap.get("type").toString());
            CatalogPersonEntity catalogPersonEntity = new CatalogPersonEntity();
            catalogPersonEntity.setFirstName(personMap.get("firstName").toString());
            catalogPersonEntity.setSecondName(personMap.get("secondName").toString());
            catalogPersonEntity.setLastName(personMap.get("lastName").toString());
            catalogPersonEntity.setSecondLastName(personMap.get("secondLastName").toString());
            catalogPersonEntity.setGender(Integer.parseInt(personMap.get("gender").toString()));

            if (type == CatalogPersonEntity.TYPE_PERSON) {
                fullName = catalogPersonEntity.getLastName() + " " + catalogPersonEntity.getSecondLastName() + ", " + catalogPersonEntity.getFirstName() + " " + catalogPersonEntity.getSecondName();
                catalogPersonEntity.setFullName(fullName);
            }

            String birthDateString = personMap.get("birthDate").toString();
            catalogPersonEntity.setBirthdate(LocalDate.parse(birthDateString, isoFormatter));
            catalogPersonEntity.setRfc(personMap.get("rfc").toString());
            catalogPersonEntity.setCurp(personMap.get("curp").toString());
            catalogPersonEntity.setMaritalStatus(CatalogPersonEntity.MARITAL_STATUS_UNKNOWN);
            catalogPersonEntity.setType(type);
            catalogPersonEntity.setCreatedAt(now);
            
            catalogPersonEntityRepository.save(catalogPersonEntity);

            systemUserEntity = new CatalogUserEntity();
            systemUserEntity.setEmail(data.get("email").toString());
            systemUserEntity.setNickname(data.get("nickname").toString());
            systemUserEntity.setAccess(encoder.encode("test"));
            systemUserEntity.setPerson(catalogPersonEntity);
            systemUserEntity.setStatus(CatalogUserEntity.STATUS_ENABLED);
            systemUserEntity.setCreatedAt(now);
            systemUserEntityRepository.save(systemUserEntity);

            logger.info("[SystemUserService][createUser][Usuario de Sistema registrado: " + systemUserEntity.getNickname() + "]");
        } catch (Exception e) {
            logger.warning("[SystemUserService][createUser][Error: " + e.getLocalizedMessage() + "]");
        }

        return systemUserEntity;
    }


    public CatalogUserEntity getSystemUserEntityByNickname(String nickname) {
        CatalogUserEntity systemUserEntity = null;
        configureLogger();

        try {
            systemUserEntity = systemUserEntityRepository.findByNickname(nickname);
        } catch (Exception e) {
            logger.warning("[SystemUserService][getSystemUserEntityByNickname][Error: " + e.getLocalizedMessage() + "]");
        }

        return systemUserEntity;
    }

    private void configureLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
    }
}
