package com.bcb.trust.front.modules.legacy.service;

import java.lang.Thread.State;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogAddressEntityRepository;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.repository.RequestEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustQuarterEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrusteeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustorEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustWorkerDepartmentEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustQuarterRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrusteeRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustorRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustWorkerDepartmentRepository;

@Service
public class WorkerService {

    private final RequestEntityRepository requestEntityRepository;

    @Autowired
    @Qualifier("bmtkfwebJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TrustQuarterRepository trustQuarterRepository;

    @Autowired
    private TrustWorkerDepartmentRepository workerDepartmentRepository;

    @Autowired
    private TrustTrustRepository trustRepository;

    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    @Autowired
    private TrustTrustWorkerRepository trustWorkerRepository;

    @Autowired
    private CatalogPersonEntityRepository personEntityRepository;

    @Autowired
    private CatalogAddressEntityRepository addressEntityRepository;

    @Autowired
    private TrustTrustorRepository trustTrustorRepository;

    @Autowired
    private TrustTrusteeRepository trustTrusteeRepository;

    private DateTimeFormatter isoShortFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    WorkerService(RequestEntityRepository requestEntityRepository) {
        this.requestEntityRepository = requestEntityRepository;
    }
    
    public void migrateRequest(SystemUserEntity userEntity) {
        String sql;
        List<Map<String, Object>> resultList;
        RequestRequestEntity requestEntity;
        String migrationStandardText = "MIGRACION";

        try {
            TrustTrustTypeEntity trustTypeEntity = trustTypeRepository.findById(1L).get();

            sql = "SELECT * FROM PROSPECT p ORDER BY p.PRS_NUM_PROSPECTO ";
            resultList = jdbcTemplate.queryForList(sql);

            // Get default type 

            Integer requestNumber;
            Integer trustChange = 1;
            Integer wasRefered;
            Integer wasReferedBy;
            String wasReferedByFullName;
            CatalogAddressEntity addressEntity;
            CatalogPersonEntity personEntity;
            //Integer state;
            Integer status;
            LocalDateTime created;
            // -- 
            String fullName;
            Integer gender = CatalogPersonEntity.GENDER_UNKWON;
            LocalDate birthDate;
            String curp;
            String rfc;
            Integer foreignStatus;
            Integer type;
            // -- Address
            String street;
            String colony;
            String township;
            String state;
            String country;
            String zipcode;
            //Long colonyId;
            String fullAddress;
            String trustChangeTrust;
            Optional<CatalogPersonEntity> result;

            for (Map<String,Object> map : resultList) {
                requestNumber = Integer.parseInt(map.get("PRS_NUM_PROSPECTO").toString());

                rfc = map.get("PRS_RFC").toString();
                
                
                fullName = map.get("PRS_NOM_PROSPECTO").toString();
                if (map.get("PRS_TIPO_PERS").toString().equals("FISICA NACIONAL")) {
                    type = CatalogPersonEntity.TYPE_PERSON;
                } else {
                    type = CatalogPersonEntity.TYPE_ENTERPRISE;
                }

                result = personEntityRepository.findOneByRfc(rfc);

                if (result.isPresent()) {
                    personEntity = result.get();
                } else {
                    personEntity = new CatalogPersonEntity();
                    personEntity.setFirstName(migrationStandardText);
                    personEntity.setSecondName(migrationStandardText);
                    personEntity.setLastName(migrationStandardText);
                    personEntity.setSecondLastName(migrationStandardText);
                    personEntity.setFullName(fullName);
                    personEntity.setGender(CatalogPersonEntity.GENDER_UNKWON);
                    personEntity.setBirthDate(LocalDate.parse("1900-01-01"));
                    personEntity.setCurp(null);
                    personEntity.setRfc(rfc);
                    personEntity.setForeignStatus(CatalogPersonEntity.FOREIGN_STATUS_CITIZEN);
                    personEntity.setType(type);
                    personEntity.setStatus(CommonEntity.STATUS_ENABLED);
                    personEntity.setCreated(LocalDateTime.now());

                    personEntityRepository.saveAndFlush(personEntity);
                }

                street = map.get("PRS_NOM_CALLE") != null ? map.get("PRS_NOM_CALLE").toString() : migrationStandardText;
                colony = map.get("PRS_NOM_COLONIA") != null ? map.get("PRS_NOM_COLONIA").toString() : migrationStandardText;
                township = map.get("PRS_NOM_POBLACION") != null ? map.get("PRS_NOM_POBLACION").toString() : migrationStandardText;
                state = map.get("PRS_NOM_ESTADO") != null ? map.get("PRS_NOM_ESTADO").toString() : migrationStandardText;
                zipcode = map.get("PRS_CODIGO_POSTAL") != null ? map.get("PRS_CODIGO_POSTAL").toString() : migrationStandardText;
                if (zipcode.length() > 4) {
                    zipcode = "0" + zipcode;
                }
                
                fullAddress = "CALLE: " + street + ", NUMERO INTERIOR: N/A, NUMERO EXTERIOR: N/A, CODIGO POSTAL: " + zipcode;
                fullAddress += ", COLONIA: " + colony + ", MUNICIPIO: " + township + ", ESTADO: " + state;

                addressEntity = new CatalogAddressEntity();
                addressEntity.setStreet(street);
                addressEntity.setInternalNumber(migrationStandardText);
                addressEntity.setExternalNumber(migrationStandardText);
                addressEntity.setZipcode(zipcode);
                addressEntity.setColonyId(null);
                addressEntity.setFullAddress(fullAddress);
                addressEntity.setCreated(LocalDateTime.now());
                addressEntityRepository.saveAndFlush(addressEntity);

                trustChangeTrust = map.get("PRS_NUM_CONTRATO").toString();
                requestEntity = new RequestRequestEntity();
                requestEntity.setNumber(requestNumber);
                requestEntity.setTrustChange(CommonEntity.SIMPLE_OPTION_NO);
                requestEntity.setTrustChangeTrust(migrationStandardText);
                requestEntity.setWasRefered(CommonEntity.SIMPLE_OPTION_NO);
                requestEntity.setWasReferedBy(RequestRequestEntity.WAS_REFERED_BY_UNKOWN);
                requestEntity.setWasReferedByFullName(migrationStandardText);
                requestEntity.setTrustTypeEntity(trustTypeEntity);
                requestEntity.setAddressEntity(addressEntity);
                requestEntity.setPersonEntity(personEntity);
                requestEntity.setState(RequestRequestEntity.STATE_REGISTERED);
                requestEntity.setStatus(CommonEntity.STATUS_ENABLED);
                requestEntity.setCreated(LocalDateTime.now());
                requestEntity.setRegisteredBy(userEntity);
                requestEntityRepository.saveAndFlush(requestEntity);
                
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void migrateTrusterAndTrustees() {
        String sql;
        List<Map<String, Object>> resultList;

        try {
            sql = "SELECT * FROM FIDEICOM_PROSPECTO fp ORDER BY fp.FID_NUM_PROSPECTO, fp.FID_SECUENCIAL_PERSONA ";
            resultList = jdbcTemplate.queryForList(sql);

            String firstName;
            String lastName;
            String secondLastName;
            String rfc;
            String curp;
            String cvePersonType;
            Integer requestNumber;
            CatalogPersonEntity personEntity;
            Optional<CatalogPersonEntity> result;
            Optional<RequestRequestEntity> resultRequest;
            RequestRequestEntity request;

            for (Map<String,Object> map : resultList) {
                requestNumber = Integer.parseInt(map.get("FID_NUM_PROSPECTO").toString());
                resultRequest = requestEntityRepository.findOneByNumber(requestNumber);
                
                if (resultRequest.isPresent()) { // Load trustors and trustees only if request exist
                    request = resultRequest.get();
                    
                    rfc = map.get("FID_RFC_PERSONA") != null ? map.get("FID_RFC_PERSONA").toString() : "";
                    result = personEntityRepository.findOneByRfc(rfc);
    
                    if (result.isPresent()) {
                        personEntity = result.get();
                    } else {
                        firstName = map.get("FID_NOM_PERSONA").toString();
                        lastName = map.get("FID_AP_PATERNO") != null ? map.get("FID_AP_PATERNO").toString() : "";
                        secondLastName = map.get("FID_AP_MATERNO") != null ? map.get("FID_AP_MATERNO").toString() : "";
                        curp = map.get("FID_CURP_PERSONA") != null ? map.get("FID_CURP_PERSONA").toString() : "";
                        
                        personEntity = new CatalogPersonEntity();
                        personEntity.setFirstName(firstName);
                        personEntity.setSecondName(null);
                        personEntity.setLastName(lastName);
                        personEntity.setSecondLastName(secondLastName);
                        personEntity.setFullName(null);
                        personEntity.setGender(CatalogPersonEntity.GENDER_UNKWON);
                        personEntity.setCurp(curp);
                        personEntity.setRfc(rfc);
                        personEntity.setBirthDate(LocalDate.parse("1900-01-01"));
                        personEntity.setForeignStatus(CatalogPersonEntity.FOREIGN_STATUS_CITIZEN);
                        if (personEntity.getRfc().length() == 11) {
                            personEntity.setType(CatalogPersonEntity.TYPE_ENTERPRISE);
                        } else {
                            personEntity.setType(CatalogPersonEntity.TYPE_PERSON);
                        }
                        personEntity.setStatus(CommonEntity.STATUS_ENABLED);
                        personEntityRepository.saveAndFlush(personEntity);
                    }
    
                    cvePersonType = map.get("FID_CVE_PERSONA") != null ? map.get("FID_CVE_PERSONA").toString() : "";
                    if (cvePersonType.equals("FIDEICOMITENTE")) {
                        TrustTrustorEntity trustorEntity = new TrustTrustorEntity();
                        trustorEntity.setCreated(LocalDateTime.now());
                        trustorEntity.setPerson(personEntity);
                        trustorEntity.setStatus(CommonEntity.STATUS_ENABLED);
                        trustorEntity.setTrust(null);
                        trustorEntity.setRequest(request);
    
                        trustTrustorRepository.saveAndFlush(trustorEntity);
                    } else if(cvePersonType.equals("FIDEICOMISARIO")) {
                        TrustTrusteeEntity trusteeEntity = new TrustTrusteeEntity();
                        trusteeEntity.setPersonEntity(personEntity);
                        trusteeEntity.setCreated(LocalDateTime.now());
                        trusteeEntity.setStatus(CommonEntity.STATUS_ENABLED);
                        trusteeEntity.setTrustEntity(null);
                        trusteeEntity.setRequest(request);
    
                        trustTrusteeRepository.saveAndFlush(trusteeEntity);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
    }

    public void migrateTrusts(SystemUserEntity userEntity) {
        String sql;
        List<Map<String, Object>> resultList;

        try {
            TrustTrustTypeEntity trustTypeEntity = trustTypeRepository.findById(1L).get();
            
            sql = "SELECT * FROM ANTEPROY a ORDER BY a.ANT_NUM_PROSPECTO";
            resultList = jdbcTemplate.queryForList(sql);
            
            Optional<RequestRequestEntity> resultRequest;
            RequestRequestEntity requestEntity;
            Integer requestNumber;
            Integer trustNumber;
            String trustName;

            for (Map<String,Object> map : resultList) {
                requestNumber = Integer.parseInt(map.get("ANT_NUM_PROSPECTO").toString());
                resultRequest = requestEntityRepository.findOneByNumber(requestNumber);
                trustName = map.get("ANT_NOM_NEGOCIO").toString();

                if (resultRequest.isPresent()) {
                    requestEntity = resultRequest.get();
                    trustNumber = Integer.parseInt(map.get("ANT_NUM_CONTRATO").toString());
                    
                    TrustTrustEntity trustEntity = new TrustTrustEntity();
                    trustEntity.setName(trustName);
                    trustEntity.setNumber(trustNumber);
                    trustEntity.setRequestEntity(requestEntity);
                    trustEntity.setTrustTypeEntity(trustTypeEntity);
                    trustEntity.setState(TrustTrustEntity.STATE_ACTIVE);
                    trustEntity.setStatus(CommonEntity.STATUS_ENABLED);
                    trustEntity.setCreated(LocalDateTime.now());
                    trustEntity.setRegisteredBy(userEntity);

                    trustRepository.saveAndFlush(trustEntity);
                }
            }
            // 
            String migrationText = "MIGRACION";
            sql = "SELECT * FROM CONTRATO c ORDER BY c.CTO_NUM_CONTRATO ";
            resultList = jdbcTemplate.queryForList(sql);
            Optional<TrustTrustEntity> resultTrust;

            for (Map<String,Object> map : resultList) {
                trustNumber = Integer.parseInt(map.get("CTO_NUM_CONTRATO").toString());
                resultTrust = trustRepository.findOneByNumber(trustNumber);

                // Create new trust if not already registered
                if (!resultTrust.isPresent()) {

                    CatalogPersonEntity newPerson = new CatalogPersonEntity();
                    newPerson.setFirstName(migrationText);
                    newPerson.setLastName(migrationText);
                    newPerson.setSecondLastName(migrationText);
                    newPerson.setCurp("");
                    newPerson.setRfc("");
                    newPerson.setBirthDate(LocalDate.parse("1900-01-01"));
                    newPerson.setForeignStatus(CatalogPersonEntity.FOREIGN_STATUS_CITIZEN);
                    newPerson.setStatus(CommonEntity.STATUS_ENABLED);
                    newPerson.setGender(CatalogPersonEntity.GENDER_UNKWON);
                    newPerson.setType(CatalogPersonEntity.TYPE_PERSON);
                    newPerson.setCreated(LocalDateTime.now());
                    personEntityRepository.saveAndFlush(newPerson);

                    CatalogAddressEntity newAddress = new CatalogAddressEntity();
                    newAddress.setColonyId(null);
                    newAddress.setExternalNumber(migrationText);
                    newAddress.setInternalNumber(migrationText);
                    newAddress.setStreet(migrationText);
                    newAddress.setZipcode(migrationText);
                    newAddress.setFullAddress(migrationText);
                    newAddress.setCreated(LocalDateTime.now());
                    addressEntityRepository.saveAndFlush(newAddress);

                    RequestRequestEntity newRequest = new RequestRequestEntity();
                    newRequest.setNumber(trustNumber); // get last 
                    newRequest.setPersonEntity(newPerson);
                    newRequest.setAddressEntity(newAddress);
                    newRequest.setTrustTypeEntity(trustTypeEntity);
                    newRequest.setRegisteredBy(userEntity);
                    newRequest.setCreated(LocalDateTime.now());
                    newRequest.setState(1);
                    newRequest.setStatus(CommonEntity.STATUS_ENABLED);
                    newRequest.setTrustChange(CommonEntity.SIMPLE_OPTION_NO);
                    newRequest.setTrustChangeTrust(migrationText);
                    newRequest.setWasRefered(CommonEntity.SIMPLE_OPTION_NO);
                    newRequest.setWasReferedBy(RequestRequestEntity.WAS_REFERED_BY_UNKOWN);
                    newRequest.setWasReferedByFullName(migrationText);
                    requestEntityRepository.saveAndFlush(newRequest);

                    trustName = map.get("CTO_NOM_CONTRATO").toString();
                    
                    TrustTrustEntity trustEntity = new TrustTrustEntity();
                    trustEntity.setName(trustName);
                    trustEntity.setNumber(trustNumber);
                    trustEntity.setRequestEntity(newRequest);
                    trustEntity.setTrustTypeEntity(trustTypeEntity);
                    trustEntity.setState(TrustTrustEntity.STATE_ACTIVE);
                    trustEntity.setStatus(CommonEntity.STATUS_ENABLED);
                    trustEntity.setCreated(LocalDateTime.now());
                    trustEntity.setRegisteredBy(userEntity);

                    trustRepository.saveAndFlush(trustEntity);
                }

            }

        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
    }

    public void generateDepartments(String trustNumber) {
        String sql;
        List<Map<String, Object>> resultList;
        String departmentNumber;
        LocalDateTime now = LocalDateTime.now();
        TrustWorkerDepartmentEntity workerDepartmentEntity;

        try {
            sql = "SELECT DISTINCT SUBSTR(fdec.DAT_DESCRIPCION, 1, 2) AS DEPARTMENT_ID, SUBSTR(fdec.DAT_DESCRIPCION, 4) AS DEPARTMENT_NAME ";
            sql += "FROM FID_DATOS_EST_CTAS fdec ";
            sql += "WHERE fdec.DAT_CONTRATO = '" + trustNumber + "' ";
            sql += "AND REGEXP_LIKE(SUBSTR(fdec.DAT_DESCRIPCION, 1, 2), '^[[:digit:]]+$') ";
            sql += "ORDER BY DEPARTMENT_ID ";

            resultList = jdbcTemplate.queryForList(sql);
            
            for (Map<String,Object> map : resultList) {
                departmentNumber = map.get("DEPARTMENT_ID").toString();

                workerDepartmentEntity = workerDepartmentRepository.findByNumber(departmentNumber).get();

                if (workerDepartmentEntity == null) {
                    TrustWorkerDepartmentEntity workerDepartmentEntityNew = new TrustWorkerDepartmentEntity();
                    workerDepartmentEntityNew.setName(map.get("DEPARTMENT_NAME").toString());
                    workerDepartmentEntityNew.setNumber(departmentNumber);
                    workerDepartmentEntityNew.setCreated(now);

                    workerDepartmentRepository.saveAndFlush(workerDepartmentEntityNew);
                }

            }
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
    }

    public void generateFixedPeriods(Integer trustNumber) {
        String sql;
        List<Map<String, Object>> dateList;
        String startDateString;
        String endDateString;
        LocalDate startDate;
        LocalDate endDate;
        Integer startYear;
        Integer endYear;

        try {
            sql = "SELECT MIN(fmci.MOV_FEC_OPER) AS START_DATE, MAX(fmci.MOV_FEC_OPER ) AS END_DATE ";
            sql += "FROM FID_MOV_CTAS_IND fmci WHERE fmci.MOV_CONTRATO = '" + trustNumber + "' ";

            dateList = jdbcTemplate.queryForList(sql);
            startDateString = dateList.get(0).get("START_DATE").toString();
            endDateString = dateList.get(0).get("END_DATE").toString();

            startDate = LocalDate.parse(startDateString, isoShortFormatter);
            endDate = LocalDate.parse(endDateString, isoShortFormatter);

            startYear = startDate.getYear();
            endYear = endDate.getYear();

            LocalDateTime now = LocalDateTime.now();

            for (int i = startYear; i <= endYear; i++) {
                TrustQuarterEntity quarterOne = new TrustQuarterEntity();
                TrustQuarterEntity quarterTwo = new TrustQuarterEntity();
                TrustQuarterEntity quarterThree = new TrustQuarterEntity();
                TrustQuarterEntity quarterFour = new TrustQuarterEntity();

                quarterOne.setYear(i);
                quarterOne.setName(i + "1T");
                quarterOne.setFixed(1);
                quarterOne.setStartDate(LocalDate.of(i, 1, 1));
                quarterOne.setStartDate(LocalDate.of(i, 3, 31));
                quarterOne.setCreated(now);

                quarterTwo.setYear(i);
                quarterTwo.setName(i + "2T");
                quarterTwo.setFixed(1);
                quarterTwo.setStartDate(LocalDate.of(i, 4, 1));
                quarterTwo.setStartDate(LocalDate.of(i, 6, 30));
                quarterTwo.setCreated(now);

                quarterThree.setYear(i);
                quarterThree.setName(i + "3T");
                quarterThree.setFixed(1);
                quarterThree.setStartDate(LocalDate.of(i, 7, 1));
                quarterThree.setStartDate(LocalDate.of(i, 9, 30));
                quarterThree.setCreated(now);

                quarterFour.setYear(i);
                quarterFour.setName(i + "4T");
                quarterFour.setFixed(1);
                quarterFour.setStartDate(LocalDate.of(i, 10, 1));
                quarterFour.setStartDate(LocalDate.of(i, 12, 31));
                quarterFour.setCreated(now);

                trustQuarterRepository.save(quarterOne);
                trustQuarterRepository.save(quarterTwo);
                trustQuarterRepository.save(quarterThree);
                trustQuarterRepository.save(quarterFour);

                trustQuarterRepository.flush();
            }

            System.out.println("Periodos Generados");
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
    }
    
    public void migrateWorkers(String trustNumber) {
        String sql;
        List<Map<String, Object>> resultList;
        Optional<TrustTrustEntity> resultTrust;;
        TrustTrustEntity trust;
        Integer workerAccount = null;

        try {
            resultTrust = trustRepository.findOneByNumber(Integer.parseInt(trustNumber));

            if (!resultTrust.isPresent()) {
                
            } else {
                // Ensure first if already has workers migrated
                long totalWorkers = trustWorkerRepository.count();
                int offset = 500;

                if (totalWorkers > 0) {
                    TrustTrustWorkerEntity lastWorker = trustWorkerRepository.findEntityWithMaxNumber();
                    workerAccount = lastWorker.getAccount();
                } else {
                    workerAccount = 1000001440; // Very first 
                }

                trust = resultTrust.get();
                Integer number;
                Integer account;
                String name;
                Integer status;
                LocalDate registerDate;
                LocalDate endDate;

                for(int counter = 0; counter < offset; counter++) {

                    sql = "SELECT * FROM FID_DATOS_EST_CTAS fdec ";
                    sql += "WHERE fdec.DAT_CONTRATO = '" + trustNumber + "' ";
                    sql += "AND fdec.DAT_NIVEL = '2' ";

                    if (workerAccount != null) {
                        sql += "AND fdec.DAT_CLAVE > '" + workerAccount + "' ";
                    }

                    sql += "ORDER BY fdec.DAT_CLAVE ";
                    sql += "FETCH NEXT 950 ROWS ONLY ";
        
                    resultList = jdbcTemplate.queryForList(sql);

                    for (Map<String,Object> map : resultList) {
                        account = Integer.parseInt(map.get("DAT_CLAVE").toString());
                        number = Integer.parseInt(map.get("DAT_CLAVE").toString());
                        name = map.get("DAT_DATO").toString();
                        status = map.get("DAT_ESTATUS").toString().equals("ACTIVO") ? 1 : 0;
                        registerDate = LocalDate.parse(map.get("DAT_FEC_ALTA").toString(), isoShortFormatter);
                        endDate = LocalDate.parse(map.get("DAT_FEC_BAJA").toString(), isoShortFormatter);
        
                        TrustTrustWorkerEntity trustTrustWorkerEntity = new TrustTrustWorkerEntity();
                        trustTrustWorkerEntity.setTrust(trust);
                        trustTrustWorkerEntity.setNumber(number);
                        trustTrustWorkerEntity.setAccount(account);
                        trustTrustWorkerEntity.setName(name);
                        trustTrustWorkerEntity.setStatus(status);
                        trustTrustWorkerEntity.setRegisterDate(registerDate);
                        trustTrustWorkerEntity.setEndDate(endDate);
                        trustTrustWorkerEntity.setCreated(LocalDateTime.now());
        
                        trustWorkerRepository.saveAndFlush(trustTrustWorkerEntity);
                    }
                }

            }
            
        } catch (Exception e) {
            // TODO: handle exception
        } 
    }


    public void migrateMovements() {
        String sql;
        try {
           // Get periods
           List<TrustQuarterEntity> quarterList = trustQuarterRepository.findAll();

           LocalDate starDate;
           LocalDate endDate;
           sql = "SELECT * FROM ";
           for (TrustQuarterEntity trustQuarterEntity : quarterList) {
                
           }
           

        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
    }

}
