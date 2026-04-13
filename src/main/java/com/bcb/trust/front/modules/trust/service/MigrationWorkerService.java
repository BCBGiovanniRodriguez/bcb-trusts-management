package com.bcb.trust.front.modules.trust.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.model.mapper.WorkerDetailRowMapper;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustRepository;

@Service
public class MigrationWorkerService {

    @Autowired
    @Qualifier("trustNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate trustNamedParameterJdbcTemplate;
    
    @Autowired
    @Qualifier("bmtkfwebNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate;
    
    @Autowired
    @Qualifier("bmtkfwebJdbcTemplate")
    private JdbcTemplate bmtkfwebJdbcTemplate;

    @Autowired
    private TrustSpecialWorkerRepository trustSpecialWorkerRepository;

    @Autowired
    private TrustTrustRepository trustTrustRepository;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Integer BATCH_SIZE = 1000;

    public void checkWorkers(Integer trustNumber) {
        String sql = "SELECT * FROM FID_DATOS_EST_CTAS WHERE DAT_CONTRATO = '1045' AND DAT_NIVEL = '2' AND LENGTH(DAT_CLAVE) >= '10' ORDER BY DAT_CLAVE";
        try {
            Optional<TrustTrustEntity> trustEntityOptional = trustTrustRepository.findOneByNumber(trustNumber);
            if (trustEntityOptional.isPresent()) {
                TrustTrustEntity trustEntity = trustEntityOptional.get();
                if (trustSpecialWorkerRepository.countByTrustEntity(trustEntity) > 0) {
                    System.out.println("Workers migrated successfully for trust number: " + trustNumber);
                    // Get last migrated worker account and migrate remaining workers
                    TrustSpecialWorkerEntity specialWorkerEntity = trustSpecialWorkerRepository.findTopByTrustEntityOrderByCreatedAtDesc(trustEntity);
                    String lastMigratedAccount = specialWorkerEntity.getAccount();
                    System.out.println("Last migrated worker account: " + lastMigratedAccount);
                    this.migrateAllWorkersFromAccount(trustNumber, lastMigratedAccount);
                } else { // Migrate all workers
                    this.migrateAllWorkers(trustNumber);
                } 
            } else {
                System.out.println("Trust with number " + trustNumber + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error checking workers for trust number " + trustNumber + ": " + e.getLocalizedMessage());
        }
    }

    private void migrateAllWorkers(Integer trustNumber) {
        Long totalWorkers;
        String sql;
        Double progress;
        Integer workerStatus;
        Integer workerNumber;
        TrustTrustEntity trustEntity = new TrustTrustEntity();
        try {
            trustEntity = trustTrustRepository.findOneByNumber(trustNumber).orElse(null);

            if (trustEntity != null) { // Verify if trust exists
                sql = "SELECT COUNT(*) FROM FID_DATOS_EST_CTAS WHERE DAT_CONTRATO = '" + trustNumber + "' AND DAT_NIVEL = '2' AND LENGTH(DAT_CLAVE) >= '10'";
                totalWorkers = bmtkfwebJdbcTemplate.queryForObject(sql, Long.class);

                if (totalWorkers != null && totalWorkers > 0) {
                    System.out.println("Total workers to migrate: " + totalWorkers);
                    
                    for (int offset = 0; offset < totalWorkers; offset += BATCH_SIZE) {
                        sql = "SELECT * FROM FID_DATOS_EST_CTAS WHERE DAT_CONTRATO = '" + trustNumber + "' AND DAT_NIVEL = '2' AND LENGTH(DAT_CLAVE) >= '10' ORDER BY DAT_CLAVE OFFSET " + offset + " ROWS FETCH FIRST " + BATCH_SIZE + " ROWS ONLY";
                        List<WorkerDetail> workerDetails = bmtkfwebNamedParameterJdbcTemplate.query(sql, new WorkerDetailRowMapper());
                        
                        for (WorkerDetail workerDetail : workerDetails) {
                            workerNumber = Integer.parseInt(workerDetail.getDatClave().trim());
                            workerNumber = (workerNumber - 1000000000) / 10; // Get last 9 digits of the worker number
                            workerStatus = workerDetail.getDatEstatus().equals("INACTIVO") ? 2 : 1;

                            TrustSpecialWorkerEntity specialWorkerEntity = new TrustSpecialWorkerEntity();
                            specialWorkerEntity.setTrustEntity(trustEntity);
                            specialWorkerEntity.setAccount(workerDetail.getDatClave());
                            specialWorkerEntity.setNumber(workerNumber);
                            specialWorkerEntity.setName(workerDetail.getDatDato());
                            specialWorkerEntity.setStatus(workerStatus);
                            specialWorkerEntity.setStartWorkDate(workerDetail.getDatFechaAlta());
                            specialWorkerEntity.setEndWorkDate(workerDetail.getDatFechaBaja());
                            specialWorkerEntity.setCreatedAt(LocalDateTime.now());

                            trustSpecialWorkerRepository.save(specialWorkerEntity);
                        }

                        progress = (double) (offset + workerDetails.size()) / totalWorkers * 100;
                        System.out.println("Migrated " + (offset + workerDetails.size()) + " of " + totalWorkers + " workers. Progress: " + String.format("%.2f", progress) + "%");
                    }
                }
                System.out.println("Worker migration completed for trust number: " + trustNumber);
            }
        } catch (Exception e) {
            System.out.println("Error migrating workers for trust number " + trustNumber + ": " + e.getLocalizedMessage());
        }

        System.out.println("Migration process finished for trust number: " + trustNumber);
    }

    private void migrateAllWorkersFromAccount(Integer trustNumber, String account) {
        Long totalWorkers;
        String sql;
        Double progress;
        Integer workerStatus;
        TrustTrustEntity trustEntity = new TrustTrustEntity();
        try {
            trustEntity = trustTrustRepository.findOneByNumber(trustNumber).orElse(null);

            if (trustEntity != null) { // Verify if trust exists
                sql = "SELECT COUNT(*) FROM FID_DATOS_EST_CTAS WHERE DAT_CONTRATO = '" + trustNumber + "' AND DAT_NIVEL = '2' AND LENGTH(DAT_CLAVE) >= '10' AND DAT_CLAVE > '" + account + "'";
                totalWorkers = bmtkfwebJdbcTemplate.queryForObject(sql, Long.class);

                if (totalWorkers != null && totalWorkers > 0) {
                    System.out.println("Total workers to migrate: " + totalWorkers);
                    
                    for (int offset = 0; offset < totalWorkers; offset += BATCH_SIZE) {
                        sql = "SELECT * FROM FID_DATOS_EST_CTAS WHERE DAT_CONTRATO = '" + trustNumber + "' AND DAT_NIVEL = '2' AND LENGTH(DAT_CLAVE) >= '10' AND DAT_CLAVE > '" + account + "' ORDER BY DAT_CLAVE OFFSET " + offset + " ROWS FETCH FIRST " + BATCH_SIZE + " ROWS ONLY";
                        List<WorkerDetail> workerDetails = bmtkfwebNamedParameterJdbcTemplate.query(sql, new WorkerDetailRowMapper());
                        
                        for (WorkerDetail workerDetail : workerDetails) {
                            workerStatus = workerDetail.getDatEstatus().equals("INACTIVO") ? 2 : 1;

                            TrustSpecialWorkerEntity specialWorkerEntity = new TrustSpecialWorkerEntity();
                            specialWorkerEntity.setTrustEntity(trustEntity);
                            specialWorkerEntity.setAccount(workerDetail.getDatClave());
                            specialWorkerEntity.setName(workerDetail.getDatDato());
                            specialWorkerEntity.setStatus(workerStatus);
                            specialWorkerEntity.setStartWorkDate(workerDetail.getDatFechaAlta());
                            specialWorkerEntity.setEndWorkDate(workerDetail.getDatFechaBaja());
                            specialWorkerEntity.setCreatedAt(LocalDateTime.now());

                            trustSpecialWorkerRepository.save(specialWorkerEntity);
                        }

                        progress = (double) (offset + workerDetails.size()) / totalWorkers * 100;
                        System.out.println("Migrated " + (offset + workerDetails.size()) + " of " + totalWorkers + " workers. Progress: " + String.format("%.2f", progress) + "%");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error migrating workers for trust number " + trustNumber + " from account " + account + ": " + e.getLocalizedMessage());
        }
    }
}
