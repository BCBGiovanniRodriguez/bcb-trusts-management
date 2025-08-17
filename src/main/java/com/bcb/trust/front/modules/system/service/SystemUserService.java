package com.bcb.trust.front.modules.system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Service
public class SystemUserService {

    public static final Logger logger = Logger.getLogger(SystemUserService.class.getName());

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private SystemProfileRepository systemProfileEntityRepository;

    @Autowired
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public SystemUserEntity createUser(Map<String, Object> data) {
        SystemUserEntity systemUserEntity = null;
        configureLogger();

        try {
            // Unwrap data
            Long profileId = Long.parseLong(data.get("profileId").toString());

            Optional<SystemProfileEntity> result = systemProfileEntityRepository.findById(profileId);

            if (!result.isPresent()) {
                throw new Exception("Identificador de Perfil de sistema no encontrado");
            } else {
                SystemProfileEntity profileEntity = result.get();
                LocalDateTime now = LocalDateTime.now();
                
                @SuppressWarnings("unchecked")
                Map<String, Object> personMap = (Map<String, Object>) data.get("person");

                CatalogPersonEntity catalogPersonEntity = new CatalogPersonEntity();
                catalogPersonEntity.setFirstName(personMap.get("firstName").toString());
                catalogPersonEntity.setSecondName(personMap.get("secondName").toString());
                catalogPersonEntity.setLastName(personMap.get("lastName").toString());
                catalogPersonEntity.setSecondLastName(personMap.get("secondLastName").toString());
                catalogPersonEntity.setGender(Integer.parseInt(personMap.get("gender").toString()));

                String birthDateString = personMap.get("birthDate").toString();
                catalogPersonEntity.setBirthDate(LocalDate.parse(birthDateString, isoFormatter));
                
                catalogPersonEntity.setRfc(personMap.get("rfc").toString());
                catalogPersonEntity.setCurp(personMap.get("curp").toString());
                catalogPersonEntity.setType(CatalogPersonEntity.TYPE_PERSON);
                catalogPersonEntity.setCreated(now);
                
                catalogPersonEntityRepository.save(catalogPersonEntity);

                systemUserEntity = new SystemUserEntity();
                systemUserEntity.setEmail(data.get("email").toString());
                systemUserEntity.setNickname(data.get("nickname").toString());
                systemUserEntity.setAccess(encoder.encode("test"));
                systemUserEntity.setProfile(profileEntity);
                systemUserEntity.setPerson(catalogPersonEntity);
                systemUserEntity.setStatus(SystemUserEntity.STATUS_ENABLED);
                systemUserEntity.setCreated(now);
                systemUserEntityRepository.save(systemUserEntity);

                logger.info("[SystemUserService][createUser][Usuario de Sistema registrado: " + systemUserEntity.getNickname() + "]");
            }
        } catch (Exception e) {
            logger.warning("[SystemUserService][createUser][Error: " + e.getLocalizedMessage() + "]");
        }

        return systemUserEntity;
    }


    public SystemUserEntity getSystemUserEntityByNickname(String nickname) {
        SystemUserEntity systemUserEntity = null;
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
