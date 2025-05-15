package com.bcb.trust.front.service;

import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;
import com.bcb.trust.front.model.bmtkfweb.mapper.PercentageRightsAcquiredMapper;
import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.model.mapper.IndividualReportAccountRowMapper;
import com.bcb.trust.front.model.mapper.WorkerDetailRowMapper;

@Service
public class LegacyService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    DateTimeFormatter mexFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String tableName = "FID_DATOS_EST_CTAS";

    private String workerMovementsTableName = "REPCTAIND";

    /**
     * 
     * @param contractNumber
     * @return
     */
    public int getTotalWorkers(int contractNumber) {
        int totalWorkers = 0;
        String sqlQuery = "SELECT COUNT(*) AS TOTAL_WORKERS FROM " + tableName + " WHERE DAT_CONTRATO = :contrato AND DAT_NIVEL = :nivel AND DAT_ESTATUS <> :status AND LENGTH(DAT_CLAVE) >= 10";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contrato", contractNumber, Types.INTEGER);
        parameters.addValue("nivel", 2, Types.INTEGER);
        parameters.addValue("status", "INACTIVO", Types.VARCHAR);

        totalWorkers = (int) namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, Integer.class);
        return totalWorkers;
    }

    /**
     * 
     * @param contractNumber
     * @param account
     * @param limit
     * @return
     */
    public List<WorkerDetail> getWorkerList(int contractNumber, String account, int limit) {
        List<WorkerDetail> workerList = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contrato", contractNumber, Types.INTEGER);
        parameters.addValue("nivel", 2, Types.INTEGER);
        parameters.addValue("estatus", "INACTIVO", Types.VARCHAR);
        parameters.addValue("limit", limit, Types.INTEGER);
        
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE ";
        sqlQuery += "DAT_CONTRATO = :contrato AND DAT_NIVEL = :nivel AND DAT_ESTATUS <> :estatus AND LENGTH(DAT_CLAVE) >= 10 ";
        
        if (account != null && !account.equals("")) {
            parameters.addValue("account", account);
            sqlQuery += " AND DAT_CLAVE >= :account";
        }

        sqlQuery += " ORDER BY DAT_CLAVE LIMIT :limit";

        //workerList = namedParameterJdbcTemplate.queryForList(sqlQuery, parameters, WorkerDetail.class);
        workerList = namedParameterJdbcTemplate.query(sqlQuery, parameters, new WorkerDetailRowMapper());
        return workerList;
    }

    // 

    public WorkerDetail getWorkerData(int contractNumber, String account) {
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE DAT_CONTRATO = :contractNumber AND DAT_CLAVE = :account";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contractNumber", contractNumber, Types.INTEGER);
        parameters.addValue("account", account, Types.VARCHAR);

        WorkerDetail workerDetail = namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, new WorkerDetailRowMapper());
        return workerDetail;
    }

    public Map<String, Double> getHeaderData(String subaccount) {

        Double totalDepositsWorker = 0D;
        Double totalDepositsTownship = 0D;
        Double grandTotalDeposits = 0D;
        Double totalWithdrawWorker = 0D;
        Double totalWithdrawTownship = 0D;
        Double grandTotalWithdraws = 0D;

        Map<String, Double> headers = new HashMap<>();

        String nomInvers = "DEPOSITO TRABAJADOR";
        String sql = "SELECT SUM(RCI_DEPOSITOS) AS TOTAL_DEPOSITS_WORKER FROM REPCTAIND WHERE RCI_NOM_INVERS = :nomInvers AND RCI_NUM_N2 = :subaccount";

        totalDepositsWorker = (Double) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("subaccount", subaccount).addValue("nomInvers", nomInvers), Double.class);
        totalDepositsWorker = totalDepositsWorker != null ? totalDepositsWorker : 0D;
        
        nomInvers = "DEPOSITO H. AYUNTAMIENTO";
        sql = "SELECT SUM(RCI_DEPOSITOS) AS TOTAL_DEPOSITS_TOWNSHIP FROM REPCTAIND WHERE RCI_NOM_INVERS = :nomInvers AND RCI_NUM_N2 = :subaccount";
        totalDepositsTownship = (Double) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("subaccount", subaccount).addValue("nomInvers", nomInvers), Double.class);
        totalDepositsTownship = totalDepositsTownship != null ? totalDepositsTownship : 0D;

        nomInvers = "RETIRO TRABAJADOR";
        sql = "SELECT SUM(RCI_RETIROS) AS TOTAL_WITHDRAWS_WORKER FROM REPCTAIND WHERE RCI_NOM_INVERS = :nomInvers AND RCI_NUM_N2 = :subaccount";
        totalWithdrawWorker = (Double) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("subaccount", subaccount).addValue("nomInvers", nomInvers), Double.class);
        totalWithdrawWorker = totalWithdrawWorker != null ? totalWithdrawWorker : 0D;

        nomInvers = "RETIRO H. AYUNTAMIENTO";
        sql = "SELECT SUM(RCI_DEPOSITOS) AS TOTAL_DEPOSITS_TOWNSHIP FROM REPCTAIND WHERE RCI_NOM_INVERS = :nomInvers AND RCI_NUM_N2 = :subaccount";
        totalWithdrawTownship = (Double) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("subaccount", subaccount).addValue("nomInvers", nomInvers), Double.class);
        totalWithdrawTownship = totalWithdrawTownship != null ? totalWithdrawTownship : 0D;

        headers.put("totalDepositsWorker", totalDepositsWorker);
        headers.put("totalDepositsTownship", totalDepositsTownship);
        headers.put("grandTotalDeposits", grandTotalDeposits);

        headers.put("totalWithdrawWorker", totalWithdrawWorker);
        headers.put("totalWithdrawTownship", totalWithdrawTownship);
        headers.put("grandTotalWithdraws", grandTotalWithdraws);

        return headers;
    }


    /**
     * 
     * @param contractNumber
     * @param account
     * @return
     */
    public List<Map<String, Object>> getWorkerMovements(int contractNumber, String account) {
        String sqlQuery = "SELECT * FROM REPCTAIND WHERE DAT_CONTRATO = :contractNumber AND DAT_CLAVE = :account";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contractNumber", contractNumber, Types.INTEGER);
        parameters.addValue("account", account, Types.VARCHAR);

        List<Map<String, Object>> results = new ArrayList<>();
        results =  namedParameterJdbcTemplate.queryForList(sqlQuery, parameters);

        return results;
    }

    /**
     * 
     * @return
     */
    public List<PercentageRightsAcquired> getRightsPercentageList() {
        String sql = "SELECT * FROM FID_DER_ADQ";
        List<PercentageRightsAcquired> percentageRightsAcquiredList = new ArrayList<>();
        
        try {
            percentageRightsAcquiredList = namedParameterJdbcTemplate.getJdbcTemplate().query(sql, new PercentageRightsAcquiredMapper());
        } catch (Exception e) {
            System.out.println("Error en LegacyService:: getRightsPercentageList" + e.getLocalizedMessage());
        }
        
        return percentageRightsAcquiredList;
    }

    /**
     * 
     * @param trustNumber
     * @return
     */
    public List<LocalDate> getPeriodList(Integer trustNumber) {
        String sql = "SELECT RCI_PERIODO FROM REPCTAIND LIMIT 1";
        List<LocalDate> periodList = new ArrayList<>();

        try {
            String rawValues = namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, String.class);

            LocalDate dateStartWork = LocalDate.parse(rawValues.split("[^\\{alpha}]al[^\\{alpha}]")[0], mexFormatter);
            LocalDate dateEndWork = LocalDate.parse(rawValues.split("[^\\{alpha}]al[^\\{alpha}]")[1], mexFormatter);

            periodList.add(dateStartWork);
            periodList.add(dateEndWork);
        } catch (Exception e) {
            System.out.println("Error en LegacyService:: getPeriod " + e.getLocalizedMessage());
        }

        return periodList;
    }
}
