package com.bcb.trust.front.service;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;
import com.bcb.trust.front.model.bmtkfweb.mapper.PercentageRightsAcquiredMapper;
import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.model.mapper.WorkerDetailRowMapper;
import com.bcb.trust.front.model.trusts.entity.ProcessDetailEntity;
import com.bcb.trust.front.model.trusts.entity.ProcessEntity;
import com.bcb.trust.front.model.trusts.enums.ProcessStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;
import com.bcb.trust.front.model.trusts.repository.ProcessDetailRepository;
import com.bcb.trust.front.model.trusts.repository.ProcessRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialSelectedWorkerEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.json.data.JsonDataSource;

@Service
public class UnprocessedWorkersService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessDetailRepository processDetailRepository;

    @Autowired
    private IndividualReportService individualReportService;

    @Autowired
    @Qualifier("trustNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate trustNamedParameterJdbcTemplate;
    
    @Autowired
    @Qualifier("bmtkfwebNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate;

    private int RECORDS_PER_CYCLE = 4;

    private String primaryOutputPath = "./trusts/trust";

    private String secondaryOutputPath = "/reports/massive/";

    DateTimeFormatter filedateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    DateTimeFormatter mexFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String tableName = "FID_DATOS_EST_CTAS";

    public List<LocalDate> getPeriodList(Integer trustNumber) {
        String sql = "SELECT RCI_PERIODO FROM REPCTAIND LIMIT 1";
        List<LocalDate> periodList = new ArrayList<>();

        try {
            //String rawValues = namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, String.class);

            //LocalDate dateStartWork = LocalDate.parse(rawValues.split("[^\\{alpha}]al[^\\{alpha}]")[0], mexFormatter);
            //LocalDate dateEndWork = LocalDate.parse(rawValues.split("[^\\{alpha}]al[^\\{alpha}]")[1], mexFormatter);

            LocalDate dateStartWork = LocalDate.parse("2025-01-01");
            LocalDate dateEndWork = LocalDate.parse("2025-06-30");

            periodList.add(dateStartWork);
            periodList.add(dateEndWork);
        } catch (Exception e) {
            System.out.println("Error en LegacyService:: getPeriod " + e.getLocalizedMessage());
        }

        return periodList;
    }

    @SuppressWarnings("null")
    private int getTotalWorkers(String contractNumber) {
        int totalWorkers = 0;
        //String sqlQuery = "SELECT COUNT(*) AS TOTAL_WORKERS FROM WorkersForProcess";
        String sqlQuery = "SELECT COUNT(*) AS TOTAL_WORKERS FROM LEGACY_SELECTED_WORKERS WHERE CONTRACT_NUMBER = :contractNumber";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contractNumber", contractNumber);
        //parameters.addValue("nivel", 2);
        //parameters.addValue("status", "INACTIVO");

        totalWorkers = (int) trustNamedParameterJdbcTemplate.getJdbcTemplate()
                .queryForObject(sqlQuery, Integer.class);

        return totalWorkers;
    }

    public List<PercentageRightsAcquired> getRightsPercentageList() {
        String sql = "SELECT * FROM FID_DER_ADQ";
        List<PercentageRightsAcquired> percentageRightsAcquiredList = new ArrayList<>();

        try {
            percentageRightsAcquiredList = bmtkfwebNamedParameterJdbcTemplate
                .getJdbcTemplate().query(sql, new PercentageRightsAcquiredMapper());

        } catch (Exception e) {
            System.out.println("Error en LegacyService:: getRightsPercentageList" + e.getLocalizedMessage());
        }

        return percentageRightsAcquiredList;
    }

    /**
     * 
     * @param contractNumber
     * @param account
     * @param limit
     */
    public List<WorkerDetail> getWorkerList(int contractNumber, String account, int limit) {
        List<WorkerDetail> workerList = new ArrayList<>();
        List<TrustSpecialSelectedWorkerEntity> selectedWorkerList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM TRUST_SELECTED_WORKERS WHERE CONTRACT_NUMBER = :contractNumber";
        MapSqlParameterSource parametersOne = new MapSqlParameterSource();

        try {
            if (account != null && !account.equals("")) {
                parametersOne.addValue("account", account);
                sqlQuery += " AND account > :account ";
            }

            // NO FUNCIONA EN ORACLE
            parametersOne.addValue("limit", limit);
            sqlQuery += " LIMIT :limit";

            //selectedWorkerList = trustNamedParameterJdbcTemplate.query(sqlQuery, parametersOne, new SelectedWorkerRowMapper());
            System.out.println("#Workers: " + selectedWorkerList.size());

            StringBuilder subaccountsStringBuilder = new StringBuilder();
            /*
            for (SelectedWorkerEntity selectedWorker : selectedWorkerList) {
                subaccountsStringBuilder.append(selectedWorker.getAccount()).append(", ");
            }
            */
            //subaccountsStringBuilder.append("0");
            System.out.println(subaccountsStringBuilder.toString());

            sqlQuery = "SELECT * FROM FID_DATOS_EST_CTAS WHERE ";
            sqlQuery += "DAT_CONTRATO = :contract AND DAT_NIVEL = :level AND DAT_CLAVE IN (";
            sqlQuery += subaccountsStringBuilder.toString() + ")";

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("contract", contractNumber);
            parameters.addValue("level", 2);
            // parameters.addValue("status", "INACTIVO");
            // parameters.addValue("limit", limit);
            // parameters.addValue("subaccounts", subaccountsStringBuilder.toString());

            workerList = bmtkfwebNamedParameterJdbcTemplate
                .query(sqlQuery, parameters, new WorkerDetailRowMapper());

        } catch (Exception e) {
            System.out.println("Error on UnprocessedWorkersService::getWorkerList: " + e.getLocalizedMessage());
        }

        return workerList;
    }

    public void process(int trustNumber) {
        LocalDateTime now = LocalDateTime.now();
        // String outputPath = primaryOutputPath + trustNumber + secondaryOutputPath +
        // now.format(filedateFormatter);
        String outputPath = primaryOutputPath + trustNumber + secondaryOutputPath + "20260330134101/pending/pendientes";
        String fileName;
        Integer workersProcessed = 0;
        Integer totalWorkers = 0;
        individualReportService.setTrustNumber(trustNumber);
        List<LocalDate> periodList;
        double processPercentage = 0D;

        try {
            periodList = getPeriodList(trustNumber);
            ClassPathResource resource = new ClassPathResource("TemplateJson.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());

            //totalWorkers = getTotalWorkers(trustNumber + "");
            totalWorkers = 4;
            System.out.println("Workers for process: " + totalWorkers);
            String account = ""; // null or empty on first iteration
            //String account = "1000201650"; // null or empty on first iteration
            List<WorkerDetail> workerList = getWorkerList(trustNumber, account, RECORDS_PER_CYCLE);
            individualReportService.setPercentageRightsAcquiredList(getRightsPercentageList());

            Path path = Paths.get(outputPath);
            Files.createDirectories(path);

            // Create main process
            ProcessEntity process = new ProcessEntity();
            process.setType(ProcessTypeEnum.MASSIVE_REPORT_GENERATION);
            process.setState(ProcessStateEnum.STARTED);
            process.setTotalElements(totalWorkers);
            process.setElementsProcessed(0L);
            process.setProcessPercent(processPercentage);
            process.setCreatedAt(new Date());
            // Save the main process
            processRepository.saveAndFlush(process);

            if (!workerList.isEmpty()) {

                while (workersProcessed < totalWorkers) {
                    for (WorkerDetail workerDetail : workerList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("worker", workerDetail.getDatDato());
                        jsonObject.put("workerAccount", workerDetail.getDatClave());

                        ProcessDetailEntity processDetail = new ProcessDetailEntity();
                        processDetail.setProcess(process);

                        try {
                            individualReportService.setSubaccountWorker(workerDetail.getDatClave());
                            individualReportService.setStartDate(periodList.get(0));
                            individualReportService.setEndDate(periodList.get(1));
                            individualReportService.setWorkerDetail(workerDetail);

                            individualReportService.generate();

                            Map<String, Object> parameters = individualReportService.getParameters();
                            String jsonData = convertListToJson(individualReportService.getWorkerMovementList());
                            ByteArrayInputStream jsonDataInputStream = new ByteArrayInputStream(jsonData.getBytes());
                            JsonDataSource jsonDataSource = new JsonDataSource(jsonDataInputStream);
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                                    jsonDataSource);

                            fileName = workerDetail.getDatClave() + "_" + workerDetail.getDatDato().replace(" ", "_")
                                    + "_" + now.format(filedateFormatter) + ".pdf";
                            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + "/" + fileName);

                            System.out.println("Filename: " + fileName + " generated");

                            processDetail.setState(1);
                            processDetail.setFilename(fileName);
                            jsonObject.put("status", 1);
                        } catch (Exception e) {
                            System.out.println("UnprocessedWorkersService:: " + e.getLocalizedMessage());
                            // Get worker detail and save result
                            processDetail.setState(2);
                            jsonObject.put("status", 2);
                            System.out.println("Error Detail: " + e.getMessage());
                        }

                        // Save the detail of the worker
                        processDetail.setDetail(jsonObject.toString());
                        processDetail.setCreatedAt(new Date());
                        processDetailRepository.saveAndFlush(processDetail);

                        workersProcessed++;
                    }

                    // Get the next list of workers
                    account = workerList.getLast().getDatClave();
                    workerList = getWorkerList(trustNumber, account, RECORDS_PER_CYCLE);

                    // Update the main process
                    processPercentage = (workersProcessed * 100) / totalWorkers;
                    process.setProcessPercent(processPercentage);
                    process.setElementsProcessed(workersProcessed);
                    processRepository.saveAndFlush(process);

                    // break; // Validar que se obtienen los siguientes trabajadores
                }

                // End of process
                process.setState(ProcessStateEnum.FINISHED);
                processRepository.saveAndFlush(process);

            }

        } catch (Exception e) {
            System.out.println("UnprocessedWorkersService::process" + e.getLocalizedMessage());
        }

        System.out.println("UnprocessedWorkersService::process finished!!!");
    }

    public static String convertListToJson(List<?> list) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }
}
