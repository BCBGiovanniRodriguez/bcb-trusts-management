package com.bcb.trust.front.modules.trust.service;

import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerYearBalanceRepository;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialPeriodEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialSelectedWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerBalanceEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerYearBalanceEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialYearEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialPeriodRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialSelectedWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerBalanceRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialYearRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustRepository;

@Service
public class BalanceWorkerService {

    private final TrustSpecialWorkerYearBalanceRepository trustSpecialWorkerYearBalanceRepository;

    @Autowired
    private TrustSpecialWorkerBalanceRepository trustSpecialWorkerBalanceRepository;

    @Autowired
    private TrustSpecialPeriodRepository trustSpecialPeriodRepository;
    
    @Autowired
    private TrustSpecialWorkerRepository trustSpecialWorkerRepository;

    @Autowired
    private TrustSpecialSelectedWorkerRepository trustSpecialSelectedWorkerRepository;

    @Autowired
    private TrustTrustRepository trustTrustRepository;

    private List<TrustSpecialPeriodEntity> periodList = new ArrayList<>();

    @Autowired
    @Qualifier("bmtkfwebNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate;

    @Autowired
    private TrustSpecialYearRepository trustSpecialYearRepository;

    BalanceWorkerService(TrustSpecialWorkerYearBalanceRepository trustSpecialWorkerYearBalanceRepository) {
        this.trustSpecialWorkerYearBalanceRepository = trustSpecialWorkerYearBalanceRepository;
    }

    private void calculateMonthlyBalance() {
        // Lógica para calcular el balance mensual de los trabajadores
        // Esto podría incluir la iteración sobre los trabajadores, la obtención de sus transacciones,
        // y el cálculo del balance basado en depósitos, retiros, intereses, etc.

        List<TrustSpecialSelectedWorkerEntity> selectedWorkers = trustSpecialSelectedWorkerRepository.findAll();
        Optional<TrustSpecialWorkerEntity> workerEntityOptional;
        TrustSpecialWorkerEntity workerEntity;

        for (TrustSpecialSelectedWorkerEntity trustSpecialSelectedWorkerEntity : selectedWorkers) {
            workerEntityOptional = trustSpecialWorkerRepository.findOneByAccount(trustSpecialSelectedWorkerEntity.getAccount());

            if (workerEntityOptional.isPresent()) {
                workerEntity = workerEntityOptional.get();
                
            } else {
                // No se encontro
            }
        }

    }

    public void generateBalance(Integer trustNumber) {
        periodList = trustSpecialPeriodRepository.findAll();
        TrustTrustEntity trustEntity;
        List<TrustSpecialWorkerEntity> workerEntityList;

        try {
            trustEntity = trustTrustRepository.findOneByNumber(trustNumber).orElseThrow(() -> new Exception("Trust with number " + trustNumber + " not found."));
            //workerEntityList = trustSpecialWorkerRepository.findAll();
            //generateWorkerBalance(workerEntityList);
            List<String> accounts = new ArrayList<>();
            accounts.add("1000000270");
            workerEntityList = trustSpecialWorkerRepository.findByAccountIn(accounts);
            generateWorkerBalance(workerEntityList);
        } catch (Exception e) {
            System.out.println("Error al generar el balance mensual: " + e.getMessage());
        }
        System.out.println("Balance mensual generado exitosamente.");
    }

    private void generateWorkerBalance(List<TrustSpecialWorkerEntity> workerEntityList) {
        String sql;
        String workerName = "";
        LocalDate dateStartWork;
        LocalDate dateEndWork;
        String subaccountWorkerBase;
        Boolean inPeriodWorker;
        Double balance;
        Double income;
        Double outcome;
        List<Double> results = new ArrayList<>();
        TrustSpecialWorkerEntity workerEntity;

        String subaccountWorkerDeposits; // Depositos Trabajador
        String subaccountWorkerInterest; // Intereses Trabajador
        String subaccountTownshipDeposits; // Depositos Ayuntamiento
        String subaccountTownshipInterest; // Intereses Ayuntamiento
        
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("trustNumber", workerEntityList.getFirst().getTrustEntity().getNumber(), Types.NUMERIC);
        parameters.addValue("startDate", null, Types.DATE);
        parameters.addValue("endDate", null, Types.DATE);

        try {/*
            for (int i = 0; i < workerEntityList.size(); i++) {
                if (workerEntityList.get(i).getAccount() == null || workerEntityList.get(i).getAccount().isEmpty()) {
                    throw new Exception("Worker with ID " + workerEntityList.get(i).getWorkerId() + " has an invalid account.");
                }
            }*/

            //for (TrustSpecialWorkerEntity workerEntity : workerEntityList) {
            //for (int i = 0; i < workerEntityList.size(); i++) {
            for (int i = 0; i < 4; i++) {
                workerEntity = workerEntityList.get(i);
                workerName = workerEntity.getName();
                dateStartWork = workerEntity.getStartWorkDate();
                dateEndWork = workerEntity.getEndWorkDate();
                subaccountWorkerBase = workerEntity.getAccount().substring(0, workerEntity.getAccount().length() - 1);
                
                subaccountWorkerDeposits = subaccountWorkerBase + "1"; // Depositos Trabajador
                subaccountWorkerInterest = subaccountWorkerBase + "5"; // Intereses Trabajador
                subaccountTownshipDeposits = subaccountWorkerBase + "3"; // Depositos Ayuntamiento
                subaccountTownshipInterest = subaccountWorkerBase + "6"; // Intereses Ayuntamiento

                parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits, Types.NUMERIC);
                parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest, Types.NUMERIC);
                parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits, Types.NUMERIC);
                parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest, Types.NUMERIC);

                sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_TIPO_OPER = 'D' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_TIPO_OPER = 'D' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_TIPO_OPER = 'D' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_TIPO_OPER = 'D' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_TIPO_OPER = 'R' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_TIPO_OPER = 'R' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_TIPO_OPER = 'R' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate UNION ALL ";
                sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_TIPO_OPER = 'R' AND MOV_FEC_OPER BETWEEN :startDate AND :endDate ";

                for (TrustSpecialPeriodEntity trustSpecialPeriodEntity : periodList) {
                    inPeriodWorker = (dateStartWork.isBefore(trustSpecialPeriodEntity.getEndDate()) || dateStartWork.isEqual(trustSpecialPeriodEntity.getEndDate())) && (dateEndWork == null || (dateEndWork.isAfter(trustSpecialPeriodEntity.getStartDate()) || dateEndWork.isEqual(trustSpecialPeriodEntity.getEndDate())));
                    if (inPeriodWorker) {
                        balance = 0.0;
                        income = 0.0;
                        outcome = 0.0;
                        // Get
                        parameters.addValue("startDate", trustSpecialPeriodEntity.getStartDate(), Types.DATE);
                        parameters.addValue("endDate", trustSpecialPeriodEntity.getEndDate(), Types.DATE);
                        results = bmtkfwebNamedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);

                        TrustSpecialWorkerBalanceEntity balanceEntity = new TrustSpecialWorkerBalanceEntity();
                        balanceEntity.setWorkerEntity(workerEntity);
                        balanceEntity.setPeriodEntity(trustSpecialPeriodEntity);

                        balanceEntity.setWorkerDeposits(results.get(0));
                        balanceEntity.setWorkerInterest(results.get(1));
                        balanceEntity.setTownshipDeposits(results.get(2));
                        balanceEntity.setTownshipInterest(results.get(3));
                        
                        balanceEntity.setWorkerWithdraw(results.get(4));
                        balanceEntity.setNegativeWorkerInterest(results.get(5));
                        balanceEntity.setTownshipWithdraw(results.get(6));
                        balanceEntity.setNegativeTownshipInterest(results.get(7));

                        income = balanceEntity.getWorkerDeposits() + balanceEntity.getWorkerInterest() + balanceEntity.getTownshipDeposits() + balanceEntity.getTownshipInterest();
                        outcome = balanceEntity.getWorkerWithdraw() + balanceEntity.getTownshipWithdraw() + balanceEntity.getNegativeWorkerInterest() + balanceEntity.getNegativeTownshipInterest();

                        balance = income - outcome;
                        if (outcome > 0) {
                            System.out.println("Worker: " + workerName + ", Income: " + income + ", Outcome: " + outcome + ", Balance: " + balance);
                        }
                        balanceEntity.setBalance(balance);

                        trustSpecialWorkerBalanceRepository.save(balanceEntity);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al calcular el balance mensual: " + e.getMessage());
            System.out.println("Worker: " + workerName);
        }
    }


    public void generateYearBalance(Integer trustNumber) {
        List<TrustSpecialYearEntity> yearList = new ArrayList<>();
        List<TrustSpecialWorkerBalanceEntity> balanceList = new ArrayList<>();
        List<TrustSpecialWorkerEntity> workerEntityList = new ArrayList<>();
        List<TrustSpecialPeriodEntity> periodList = new ArrayList<>();
        TrustTrustEntity trustEntity;
        TrustSpecialWorkerEntity trustSpecialWorkerEntity;

        // The annual balance could be calculated by summing the monthly balances for each worker and period, and then saving the result in a new entity representing the annual balance.
        try {
            trustEntity = trustTrustRepository.findOneByNumber(trustNumber).orElseThrow(() -> new Exception("Trust with number " + trustNumber + " not found."));
            periodList = trustSpecialPeriodRepository.findAllByTrustEntity(trustEntity);
            yearList = trustSpecialYearRepository.findAllByTrustEntity(trustEntity);
            List<String> accounts = new ArrayList<>();
            accounts.add("1000000270");
            //workerEntityList = trustSpecialWorkerRepository.findAll();
            workerEntityList = trustSpecialWorkerRepository.findByAccountIn(accounts);

            Double workerDeposits;
            Double workerInterest;
            Double townshipDeposits;
            Double townshipInterest;
            Double negativeWorkerInterest;
            Double negativeTownshipInterest;
            Double workerWithdraw;
            Double townshipWithdraw;
            Double workerTransfer;
            Double townshipTransfer;
            Double balance;

            List<TrustSpecialWorkerYearBalanceEntity> workerYearBalance = new ArrayList<>();

            //for (TrustSpecialWorkerEntity trustSpecialWorkerEntity : workerEntityList) {
            //for (int i = 0; i < workerEntityList.size(); i++) {
            for (int i = 0; i < 4; i++) {
                trustSpecialWorkerEntity = workerEntityList.get(i);
                for (TrustSpecialYearEntity trustSpecialYearEntity : yearList) {
                    workerDeposits = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getWorkerDeposits).sum();
                    workerInterest = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getWorkerInterest).sum();
                    townshipDeposits = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getTownshipDeposits).sum();
                    townshipInterest = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getTownshipInterest).sum();
                    negativeWorkerInterest = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getNegativeWorkerInterest).sum();
                    negativeTownshipInterest = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getNegativeTownshipInterest).sum();
                    workerWithdraw = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getWorkerWithdraw).sum();
                    townshipWithdraw = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getTownshipWithdraw).sum();

                    balance = trustSpecialWorkerEntity.getBalances().stream()
                        .filter(b -> b.getPeriodEntity().getYearEntity().equals(trustSpecialYearEntity)).mapToDouble(TrustSpecialWorkerBalanceEntity::getBalance).sum();
                    
                    TrustSpecialWorkerYearBalanceEntity workerYearBalanceEntity = new TrustSpecialWorkerYearBalanceEntity();
                    workerYearBalanceEntity.setWorkerEntity(trustSpecialWorkerEntity);
                    workerYearBalanceEntity.setYearEntity(trustSpecialYearEntity);
                    workerYearBalanceEntity.setWorkerDeposits(workerDeposits);
                    workerYearBalanceEntity.setWorkerInterest(workerInterest);
                    workerYearBalanceEntity.setTownshipDeposits(townshipDeposits);
                    workerYearBalanceEntity.setTownshipInterest(townshipInterest);
                    workerYearBalanceEntity.setNegativeWorkerInterest(negativeWorkerInterest);
                    workerYearBalanceEntity.setNegativeTownshipInterest(negativeTownshipInterest);
                    workerYearBalanceEntity.setWorkerWithdraw(workerWithdraw);
                    workerYearBalanceEntity.setTownshipWithdraw(townshipWithdraw);
                    
                    workerYearBalanceEntity.setBalance(balance);

                    trustSpecialWorkerYearBalanceRepository.save(workerYearBalanceEntity);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error al generar el balance anual: " + e.getMessage());
        }

    }
}
