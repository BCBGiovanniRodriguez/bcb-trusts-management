package com.bcb.trust.front.model.bmtkfweb.service;

import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.entity.IndividualReportAcount;
import com.bcb.trust.front.model.mapper.IndividualReportAccountRowMapper;

@Service
public class WorkerMovementService {

    @Autowired
    //@Qualifier("bmtkfwebNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String transitionDateString = "2021-07-15"; // Fecha de cambio fideicomiso Banamex -> BCB Casa de Bolsa

    private LocalDate transitionDate;

    private DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public WorkerMovementService() {
        transitionDate = LocalDate.parse(this.transitionDateString, isoFormatter);
    }

    /**
     * Get the transition balance list of worker 
     * @param contractNumber
     * @param subaccountWorkerBase
     */
    public List<Double> getTransitionBalanceList(Integer contractNumber, String subaccountWorker) {
        String sql;
        List<Double> transitionBalanceList = new ArrayList<>();
        String subaccountWorkerBase = subaccountWorker.substring(0, 9);

        try {
            String subaccountWorkerDeposits = subaccountWorkerBase + "1"; // Depositos Trabajador
            String subaccountWorkerInterest = subaccountWorkerBase + "5"; // Intereses Trabajador
            String subaccountTownshipDeposits = subaccountWorkerBase + "3"; // Depositos Ayuntamiento
            String subaccountTownshipInterest = subaccountWorkerBase + "6"; // Intereses Ayuntamiento

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("contractNumber", contractNumber, Types.NUMERIC);
            parameters.addValue("startDate", transitionDate, Types.DATE);
            parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits, Types.NUMERIC);
            parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest, Types.NUMERIC);
            parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits, Types.NUMERIC);
            parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest, Types.NUMERIC);

            sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER <= :startDate";

            transitionBalanceList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);
        } catch (Exception e) {
            System.out.println("Error on WorkerMovementService::getTransitionBalanceList" + e.getLocalizedMessage());
        }

        return transitionBalanceList;
    }

    /**
     * 
     * @param trustNumber
     * @param subaccountWorkerBase
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Double> getWorkerBalanceList(Integer trustNumber, String subaccountWorker, LocalDate startDate, LocalDate endDate) {
        String sql;
        List<Double> workerBalanceList = new ArrayList<>();
        String subaccountWorkerBase = subaccountWorker.substring(0, 9);

        try {
            String subaccountWorkerDeposits = subaccountWorkerBase + "1"; // Depositos Trabajador
            String subaccountWorkerInterest = subaccountWorkerBase + "5"; // Intereses Trabajador
            String subaccountTownshipDeposits = subaccountWorkerBase + "3"; // Depositos Ayuntamiento
            String subaccountTownshipInterest = subaccountWorkerBase + "6"; // Intereses Ayuntamiento

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("trustNumber", trustNumber, Types.NUMERIC);
            parameters.addValue("startDate", startDate, Types.DATE);
            parameters.addValue("endDate", endDate, Types.DATE);
            parameters.addValue("depositType", "D", Types.VARCHAR);
            parameters.addValue("widthdrawType", "R", Types.VARCHAR);

            parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits, Types.NUMERIC);
            parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest, Types.NUMERIC);
            parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits, Types.NUMERIC);
            parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest, Types.NUMERIC);

            sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :depositType AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :depositType AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :depositType AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :depositType AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :widthdrawType AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :widthdrawType AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :widthdrawType AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :trustNumber AND MOV_TIPO_OPER = :widthdrawType AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER < :endDate ";

            workerBalanceList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);

        } catch (Exception e) {
            System.out.println("Error on WorkerMovementService::getDetailWorkerBalanceList" + e.getLocalizedMessage());
        }

        return workerBalanceList;
    }

    /**
     * 
     * @param contractNumber
     * @param subaccount
     * @return
     */
    public List<IndividualReportAcount> getWorkerMovements(Integer trustNumber, String subaccountWorker, Double previousBalance) {
        String sql;
        List<IndividualReportAcount> list = new ArrayList<>();
        Double generalBalance = previousBalance;

        try {
            IndividualReportAcount record = new IndividualReportAcount();
            record.setDateIra("");
            record.setNameInversIra("SALDO ANTERIOR");
            record.setDepositsIra(0D);
            record.setWithdrawsIra(0D);
            record.setPartialBalanceIra(previousBalance);
            list.add(record);
            
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("trustNumber", trustNumber, Types.NUMERIC);
            parameters.addValue("subaccountWorker", subaccountWorker, Types.VARCHAR);

            sql = "SELECT * FROM REPCTAIND WHERE RCI_NUM_FIDEICOMISO = :trustNumber AND RCI_NUM_N2 = :subaccountWorker ORDER BY RCI_SECUENCIAL";
            

            for (IndividualReportAcount individualReportAcount : namedParameterJdbcTemplate.query(sql,parameters, new IndividualReportAccountRowMapper())) {
                generalBalance = generalBalance + individualReportAcount.getDepositsIra() - individualReportAcount.getWithdrawsIra();
                individualReportAcount.setPartialBalanceIra(generalBalance);
    
                list.add(individualReportAcount);
            }
        } catch (Exception e) {
            System.out.println("Error on WorkerMovementService::getWorkerMovements" + e.getLocalizedMessage());
        }

        return list;
    }

    /**
     * 
     * @param contractNumber
     * @param subaccount
     * @return
     */
    public List<Double> getWorkerMovementsBalanceList(Integer contractNumber, String subaccount) {
        String sql;
        List<Double> workerBalanceList = new ArrayList<>();
        
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("contractNumber", contractNumber, Types.NUMERIC);
            parameters.addValue("subaccount", subaccount, Types.NUMERIC);

            sql = "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_NOM_INVERS = 'DEPOSITO TRABAJADOR' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_NOM_INVERS = 'DEPOSITO H. AYUNTAMIENTO' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_NOM_INVERS = 'INTERES TRABAJADOR' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_NOM_INVERS = 'INTERES H. AYUNTAMIENTO' UNION ALL ";

            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0' AND RCI_NOM_INVERS = 'INTERES TRABAJADOR' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0'  AND RCI_NOM_INVERS = 'INTERES H. AYUNTAMIENTO' UNION ALL ";

            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0'  AND RCI_NOM_INVERS = 'RETIRO TRABAJADOR' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0'  AND RCI_NOM_INVERS = 'RETIRO H. AYUNTAMIENTO' UNION ALL ";

            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0'  AND RCI_NOM_INVERS = 'TRASPASO FONDO ESTABILIZADOR TRABAJADOR' UNION ALL ";
            sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount AND RCI_RETIROS <> '0.0'  AND RCI_NOM_INVERS = 'TRASPASO FONDO ESTABILIZADOR H. AYUNTAMIENTO' ";

            workerBalanceList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);
        } catch (Exception e) {
            System.out.println("Error on WorkerMovementService::getWorkerMovementsBalanceList" + e.getLocalizedMessage());
        }

        return workerBalanceList;
    }

    public String getTransitionDateString() {
        return transitionDateString;
    }

    public LocalDate getTransitionDate() {
        return transitionDate;
    }
}
