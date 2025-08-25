package com.bcb.trust.front.modules.request.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogAddressEntityRepository;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.repository.RequestEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;

@Service
public class RequestRequestService {

    public static final Logger logger = Logger.getLogger(RequestRequestService.class.getName());

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    @Autowired
    private CatalogAddressEntityRepository addressEntityRepository;

    @Autowired
    private SystemUserEntityRepository userEntityRepository;

    @Autowired
    private RequestEntityRepository requestEntityRepository;

    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    public RequestRequestService() {
        configureLogger();
    }


    @Transactional
    public RequestRequestEntity saveRequest(Map<String, Object> data, Authentication authentication) {
        RequestRequestEntity requestEntity = null;
        Integer currentRequestNumber = 1;

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SystemUserEntity systemUserEntity = userEntityRepository.findByNickname(userDetails.getUsername());
            LocalDateTime now = LocalDateTime.now();

            RequestRequestEntity lastRequest = requestEntityRepository.findFirstByOrderByNumberDesc();
            if (lastRequest != null) {
                currentRequestNumber = lastRequest.getNumber() + 1;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> personMap = (Map<String, Object>) data.get("person");
            @SuppressWarnings("unchecked")
            Map<String, Object> addressMap = (Map<String, Object>) data.get("address");

            Integer personType = Integer.parseInt(personMap.get("type").toString());
            CatalogPersonEntity personEntity = new CatalogPersonEntity();

            if (CatalogPersonEntity.TYPE_PERSON == personType) {
                personEntity.setFirstName((String) personMap.get("firstName"));
                personEntity.setSecondName((String) personMap.get("secondName"));
                personEntity.setLastName((String) personMap.get("lastName"));
                personEntity.setSecondLastName((String) personMap.get("secondLastName"));
                
                Integer gender = Integer.parseInt(personMap.get("gender").toString());
                personEntity.setGender(gender);
                
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
            catalogPersonEntityRepository.save(personEntity);

            CatalogAddressEntity addressEntity = new CatalogAddressEntity();
            addressEntity.setInternalNumber(addressMap.get("internalNumber").toString());
            addressEntity.setExternalNumber(addressMap.get("externalNumber").toString());
            addressEntity.setStreet(addressMap.get("street").toString());
            addressEntity.setZipcode(addressMap.get("zipcode").toString());
            addressEntity.setColonyId(Long.parseLong(addressMap.get("colonyId").toString()));
            addressEntity.setFullAddress(addressMap.get("fullAddress").toString());
            addressEntity.setCreated(now);
            addressEntityRepository.save(addressEntity);

            requestEntity = new RequestRequestEntity();
            requestEntity.setNumber(currentRequestNumber);

            Integer trustChange = Integer.parseInt(data.get("trustChange").toString());
            requestEntity.setTrustChange(trustChange);
            if (CommonEntity.SIMPLE_OPTION_YES == trustChange) {
                requestEntity.setTrustChangeTrust(data.get("trustChangeTrust").toString());
            } else {
                requestEntity.setTrustChangeTrust(null);
            }

            Integer wasRefered = Integer.parseInt(data.get("wasRefered").toString());
            requestEntity.setWasRefered(wasRefered);
            if (CommonEntity.SIMPLE_OPTION_YES == wasRefered) {
                requestEntity.setWasReferedBy(Integer.parseInt(data.get("wasReferedBy").toString()));
                requestEntity.setWasReferedByFullName(data.get("wasReferedByFullName").toString());
            } else {
                requestEntity.setWasReferedBy(null);
                requestEntity.setWasReferedByFullName(null);
            }

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

            requestEntityRepository.save(requestEntity);
        } catch (Exception e) {
            logger.warning("[RequestRequestService][createRequest][Error: " + e.getLocalizedMessage() + "]");
        }

        return requestEntity;
    }


    private void configureLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
    }
}
