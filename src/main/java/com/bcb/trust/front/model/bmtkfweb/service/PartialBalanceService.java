package com.bcb.trust.front.model.bmtkfweb.service;

import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.service.LegacyService;

@Service
public class PartialBalanceService {

    private static final Integer RECORDS_PER_CYCLE = 1000;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private LegacyService legacyService;

    @Autowired
    private WorkerMovementService workerMovementService;

    private String transitionDateString = "2021-07-15"; // Fecha de cambio fideicomiso Banamex -> BCB Casa de Bolsa

    private LocalDate transitionDate;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public void calculatePartialBalance(Integer trustNumber) {
        Integer workersProcessed = 0;
        Integer totalWorkers = legacyService.getTotalWorkers(trustNumber);
        //Integer totalWorkers = 100;
        String workerAccount = "";
        List<WorkerDetail> workerList;
        String sql;
        List<Double> transitionBalanceList;
        List<Double> workerBalanceList;
        List<LocalDate> periodList;
        transitionDate = LocalDate.parse(this.transitionDateString, isoFormatter);

        try {
            periodList = legacyService.getPeriodList(trustNumber);
            workerList = legacyService.getWorkerList(trustNumber, workerAccount, RECORDS_PER_CYCLE);

            if (!workerList.isEmpty()) {
                while (workersProcessed < totalWorkers) {
                    for (WorkerDetail workerDetail : workerList) {
                        System.out.println("Processing worker: " + workerDetail.getDatDato());
                        
                        sql = "SELECT * FROM transitionBalance WHERE TrustNumber = :trustNumber AND WorkerAccount = :workerAccount";
                        SqlParameterSource parameters = new MapSqlParameterSource()
                            .addValue("trustNumber", trustNumber, Types.NUMERIC)
                            .addValue("workerAccount", workerDetail.getDatClave(), Types.VARCHAR);

                        List<Map<String, Object>> existingTransitionBalance = namedParameterJdbcTemplate.queryForList(sql, parameters);
                        if (existingTransitionBalance.isEmpty()) {
                            // If the transition balance data does not exist, insert data
                            // Get the worker's transition balance
                            transitionBalanceList = workerMovementService.getTransitionBalanceList(trustNumber, workerDetail.getDatClave());
                            Map<String, Object> parametersOne = new HashMap<>();
                            parametersOne.put("trustNumber", trustNumber);
                            parametersOne.put("subaccount", workerDetail.getDatClave());
                            parametersOne.put("workerDeposits", transitionBalanceList.get(0));
                            parametersOne.put("workerInterest", transitionBalanceList.get(1));
                            parametersOne.put("townshipDeposits", transitionBalanceList.get(2));
                            parametersOne.put("townshipInterest", transitionBalanceList.get(3));

                            sql = "INSERT INTO transitionbalance (TrustNumber, WorkerAccount, WorkerDeposits, WorkerInterest, TownshipDeposits, TownshipInterest) ";
                            sql += "VALUES (:trustNumber, :subaccount, :workerDeposits, :workerInterest, :townshipDeposits, :townshipInterest) ";

                            namedParameterJdbcTemplate.update(sql, parametersOne);

                            // Get the worker's intermediate balance

                            workerBalanceList = workerMovementService.getWorkerBalanceList(trustNumber, workerDetail.getDatClave(), transitionDate, periodList.get(0));
                            
                            Map<String, Object> parametersThree = new HashMap<>();
                            parametersThree.put("trustNumber", trustNumber);
                            parametersThree.put("workerAccount", workerDetail.getDatClave());
                            parametersThree.put("startDate", transitionDate);
                            parametersThree.put("endDate", periodList.get(0));
                            parametersThree.put("workerDeposits", workerBalanceList.get(0));
                            parametersThree.put("workerInterest", workerBalanceList.get(1));
                            parametersThree.put("townshipDeposits", workerBalanceList.get(2));
                            parametersThree.put("townshipInterest", workerBalanceList.get(3));
                            parametersThree.put("widthdrawWorker", workerBalanceList.get(4));
                            parametersThree.put("negativeWorkerInterest", workerBalanceList.get(5));
                            parametersThree.put("withdrawTownship", workerBalanceList.get(6));
                            parametersThree.put("negativeTownshipInterest", workerBalanceList.get(7));

                            // Process each worker
                            sql = "INSERT INTO intermediatebalance (TrustNumber, WorkerAccount, StartDate, EndDate, WorkerDeposits, WorkerInterest, TownshipDeposits, TownshipInterest, WithdrawWorker, NegativeWorkerInterest, WithdrawTownship, NegativeTownshipInterest) ";
                            sql += "VALUES (:trustNumber, :workerAccount, :startDate, :endDate, :workerDeposits, :workerInterest, :townshipDeposits, :townshipInterest, :widthdrawWorker, :negativeWorkerInterest, :withdrawTownship, :negativeTownshipInterest) ";
                            
                            namedParameterJdbcTemplate.update(sql, parametersThree);
                            
                        }
                        
                        System.out.println("Worker: " + workerDetail.getDatDato() + " processed");
                        System.out.println("Procesed " + workersProcessed + " / " + totalWorkers);
                        workersProcessed++;
                        
                    }
                    // Get the next list of workers
                    workerAccount = workerList.getLast().getDatClave();
                    workerList = legacyService.getWorkerList(trustNumber, workerAccount, RECORDS_PER_CYCLE);
                    
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("Error on PartialBalanceService::calculatePartialBalance " + e.getLocalizedMessage());
        }
    }
}
