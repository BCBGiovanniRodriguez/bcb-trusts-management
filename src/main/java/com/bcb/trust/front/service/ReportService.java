package com.bcb.trust.front.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bcb.trust.front.entity.IndividualReportAcount;
import com.bcb.trust.front.entity.enums.ProcessDetailStateEnum;
import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.model.mapper.IndividualReportAccountRowMapper;
import com.bcb.trust.front.model.trusts.entity.ProcessDetailEntity;
import com.bcb.trust.front.model.trusts.entity.ProcessEntity;
import com.bcb.trust.front.model.trusts.enums.ProcessStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;
import com.bcb.trust.front.model.trusts.repository.ProcessDetailRepository;
import com.bcb.trust.front.model.trusts.repository.ProcessRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.json.data.JsonDataSource;

@Service
public class ReportService {

    @Autowired
    private LegacyService legacyService;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessDetailRepository processDetailRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //private int RECORDS_PER_CYCLE = 1000;
    private int RECORDS_PER_CYCLE = 4;

    private String primaryOutputPath = "./trusts/trust";

    private String secondaryOutputPath = "/reports/massive/";

    private Integer maxYearsRightsAcquired;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Date previousBalanceDate;

    DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter mexFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void generateReport() throws ParseException {
        this.previousBalanceDate = simpleDateFormat.parse("2021-07-15");
        //System.out.println("Fecha: " + simpleDateFormat.format(this.previousBalanceDate));
        int workersProcessed = 0;
        double processPercentage = 0;
        String outputPath = primaryOutputPath + "1045" + secondaryOutputPath;
        String fileName;
        List<IndividualReportAcount> dataList;

        try {
            Connection connection = namedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
            File reportTemplate = ResourceUtils.getFile("classpath:TemplateJson.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());

            int totalWorkers = 4; // For development purposes
            //int totalWorkers = legacyService.getTotalWorkers(1045); // Uncomment
            System.out.println("Workers for process: " + totalWorkers);
            String account = ""; // null or empty on first iteration
            List<WorkerDetail> workerList = legacyService.getWorkerList(1045, account, RECORDS_PER_CYCLE);

            if (!workerList.isEmpty()) {

                String sql = "SELECT MAX(DER_ANT) FROM FID_DER_ADQ";
                //maxYearsRightsAcquired = (Integer) namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, new MapSqlParameterSource().addValue("yearsWorked", yearsWorked), Integer.class);
                maxYearsRightsAcquired = (Integer) namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);

                // Define output dir for pdf reports
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddhhmmss");
                outputPath += dateFormat.format(new Date());
                System.out.println("OutputPath: " + outputPath);

                Path path = Paths.get(outputPath);
                Files.createDirectories(path);
                
                // Create main process
                ProcessEntity process = new ProcessEntity();
                process.setProcessType(ProcessTypeEnum.MASSIVE_REPORT_GENERATION);
                process.setProcessState(ProcessStateEnum.STARTED);
                process.setTotalElements(totalWorkers);
                process.setElementsProcessed(0L);
                process.setProcessPercentage(processPercentage);
                process.setCreated(new Date());
                // Save the main process
                processRepository.saveAndFlush(process);
                
                // Do the process
                while (workersProcessed < totalWorkers) {
                    
                    for (WorkerDetail workerDetail : workerList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("worker", workerDetail.getDatDato());
                        jsonObject.put("workerAccount", workerDetail.getDatClave());
                        
                        ProcessDetailEntity processDetail = new ProcessDetailEntity();
                        processDetail.setProcess(process);
                        // Apply try catch for continue the process
                        try {
                            fileName = workerDetail.getDatClave() + "_" + workerDetail.getDatDato().replace(" ", "_") + "_" + dateFormat.format(new Date()) + ".pdf";
                            // Get worker details
                            System.out.println(workerDetail.toString());
                            // Get worker movements
            
                            // Fill & save report
                            //dataList = getData(workerDetail);
                            //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

                            Map<String, Object> parameters = this.getParameters(workerDetail);
                            Double previousBalance = (Double) parameters.get("GRANDTOTAL_PREVIOUS_BALANCE");
                            dataList = getData(workerDetail, previousBalance);
                            String jsonData = convertListToJson(dataList);
                            ByteArrayInputStream jsonDataInputStream = new ByteArrayInputStream(jsonData.getBytes());
                            JsonDataSource jsonDataSource = new JsonDataSource(jsonDataInputStream);
                            //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

                            
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jsonDataSource);

                            //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
                            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + "/" + fileName);
                            // Inform worker processed
                            // Check if pause / stop is requested
                            // 
            
                            // Save the detail
                            processDetail.setProcessDetailState(ProcessDetailStateEnum.PROCESSED);
                            jsonObject.put("status", 1);
                            
                        } catch (Exception e) {
                            // Get worker detail and save result
                            processDetail.setProcessDetailState(ProcessDetailStateEnum.ERROR);
                            jsonObject.put("status", 2);
                            System.out.println("Error Detail: " + e.getMessage());
                        }
            
                        // Save the detail of the worker
                        processDetail.setDetail(jsonObject.toString());
                        processDetail.setCreated(new Date());
                        processDetailRepository.saveAndFlush(processDetail);

                        workersProcessed++;
                    }

                    // Get the next list of workers
                    account = workerList.getLast().getDatClave();
                    workerList = legacyService.getWorkerList(1045, account, RECORDS_PER_CYCLE);
                    
                    // Update the main process
                    processPercentage = (workersProcessed * 100) / totalWorkers;
                    process.setProcessPercentage(processPercentage);
                    process.setElementsProcessed(workersProcessed);
                    processRepository.saveAndFlush(process);
                }
                
                // End of process
                process.setProcessState(ProcessStateEnum.FINISHED);
                processRepository.saveAndFlush(process);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void exportPDF(String outputPath, String account) {
        try {
            //String outputPath = "./";
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddhhmmss");
            
            Connection connection = namedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
            
            File reportTemplate = ResourceUtils.getFile("classpath:Template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());

            //Map<String, Object> parameters = getParameters("1000166050");
            //parameters.put("ex", "value");
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            String fileName = account + dateFormat.format(now) + ".pdf";
            System.out.println("Exportando en: " + outputPath + fileName);
            //JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + fileName);
            System.out.println("Exportado");
            //return jasperPrint;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            //return null;
        } 
    }

    /**
     * 
     * @param WorkerDetail workerDetail
     * @return
     * @throws ParseException 
     */
    @SuppressWarnings("deprecation")
    private Map<String, Object> getParameters(WorkerDetail workerDetail) throws ParseException {

        String subaccount = workerDetail.getDatClave();
        String subaccountPreviousBalance = subaccount.substring(0, 9);
        Double grandTotalPreviousBalance = 0D;

        Double totalDepositsWorker = 0D;
        Double totalDepositsTownship = 0D;
        Double grandTotalDeposits = 0D;

        Double totalWithdrawsWorker = 0D;
        Double totalWithdrawsTownship = 0D;
        Double grandTotalWithdraws = 0D;

        Double totalTransfersWorker = 0D;
        Double totalTransfersTownship = 0D;
        Double grandTotalTransfers = 0D;

        Double totalInterestWorker = 0D;
        Double totalInterestTownship = 0D;
        Double grandTotalInterest = 0D;

        Integer yearsWorked;
        String rangeYearsWorked;
        Double percentageRightsAcquired;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        DecimalFormat decimalFormat = new DecimalFormat("#.00"); 

        
        String sql;
        List<Double> totalsList;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("subaccount", subaccount);
        sqlParameterSource.addValue("p1", "DEPOSITO TRABAJADOR");
        sqlParameterSource.addValue("p2", "DEPOSITO H. AYUNTAMIENTO");
        sqlParameterSource.addValue("p3", "RETIRO TRABAJADOR");
        sqlParameterSource.addValue("p4", "RETIRO H. AYUNTAMIENTO");
        sqlParameterSource.addValue("p5", "TRASPASO FONDO ESTABILIZADOR TRABAJADOR");
        sqlParameterSource.addValue("p6", "TRASPASO FONDO ESTABILIZADOR H. AYUNTAMIENTO");
        sqlParameterSource.addValue("p7", "INTERES TRABAJADOR");
        sqlParameterSource.addValue("p8", "INTERES H. AYUNTAMIENTO");

        sql = "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p1 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p2 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p3 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p4 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p5 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_RETIROS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p6 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p7 AND RCI_NUM_N2 = :subaccount UNION ALL ";
        sql += "SELECT COALESCE(SUM(RCI_DEPOSITOS), 0) AS TOTAL FROM REPCTAIND WHERE RCI_NOM_INVERS = :p8 AND RCI_NUM_N2 = :subaccount";

        totalsList = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource, Double.class);

        totalDepositsWorker = totalsList.get(0);
        totalDepositsTownship = totalsList.get(1);
        totalWithdrawsWorker = totalsList.get(2);
        totalWithdrawsTownship = totalsList.get(3);
        totalTransfersWorker = totalsList.get(4);
        totalTransfersTownship = totalsList.get(5);
        totalInterestWorker = totalsList.get(6);
        totalInterestTownship = totalsList.get(7);

        grandTotalDeposits = totalDepositsWorker + totalDepositsTownship;
        grandTotalInterest = totalInterestWorker + totalInterestTownship;
        grandTotalWithdraws = totalWithdrawsWorker + totalWithdrawsTownship;
        grandTotalTransfers = totalTransfersWorker + totalTransfersTownship;
        // Sum Transfers to Withdraws
        grandTotalWithdraws += grandTotalTransfers;

        // Calculate years worked an rights percentage
        sql = "SELECT RCI_PERIODO FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount LIMIT 1";
        rangeYearsWorked = (String) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("subaccount", subaccount), String.class);
        
        LocalDate dateStartWork = Instant.ofEpochMilli(workerDetail.getDatFechaAlta().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateEndWork = LocalDate.parse(rangeYearsWorked.split("[^\\{alpha}]al[^\\{alpha}]")[1], mexFormatter);
        
        
        Period period = Period.between(dateStartWork, dateEndWork);
        yearsWorked = period.getYears();

        if (yearsWorked >= maxYearsRightsAcquired) {
            percentageRightsAcquired = 100D;
        } else {
            sql = "SELECT DER_PCJ FROM FID_DER_ADQ WHERE DER_ANT = :yearsWorked LIMIT 1";
            percentageRightsAcquired = (Double) namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("yearsWorked", yearsWorked), Double.class);
        }
        // TODO parametrizar el valor de inicio de 
        sqlParameterSource = new MapSqlParameterSource();
        
        sqlParameterSource.addValue("fechOper", this.previousBalanceDate);
        sqlParameterSource.addValue("sub1", subaccountPreviousBalance + "1");
        sqlParameterSource.addValue("sub3", subaccountPreviousBalance + "3");
        sqlParameterSource.addValue("sub5", subaccountPreviousBalance + "5");
        sqlParameterSource.addValue("sub6", subaccountPreviousBalance + "6");
        sql = "SELECT SUM(MOV_IMPORTE) AS SALDO_ANTERIOR FROM SALDOSSIRJUM WHERE MOV_CLAVE_INV IN (:sub1, :sub3, :sub5, :sub6) AND MOV_FEC_OPER <= :fechOper";
        
        grandTotalPreviousBalance = (Double) namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Double.class);
        grandTotalPreviousBalance = grandTotalPreviousBalance == null ? 0D : grandTotalPreviousBalance;

        LocalDate startPeriodDate = LocalDate.parse("2024-07-01", isoFormatter);
        grandTotalPreviousBalance = getPreviousBalance(workerDetail, startPeriodDate);

        Map<String, Object> parameters = new HashMap<>();
        // Data Report Header
        parameters.put("P_TRUST_NAME", "FIDEICOMISO SIRJUM MÉRIDA");
        parameters.put("P_WORKER_FULLNAME", workerDetail.getDatDato());
        parameters.put("P_WORKER_WORKCENTER", workerDetail.getDatDescripcion());
        parameters.put("P_REGISTRATION_TRUST_DATE", dateFormat.format(workerDetail.getDatFechaAlta())); // Cambio de Administración 
        //parameters.put("P_DEPARTMENT_NAME", "");
        parameters.put("P_INIT_WORK_DATE", dateFormat.format(workerDetail.getDatFecUltMod()));
        parameters.put("P_FINISH_WORK_DATE", "");
        if (!workerDetail.getDatEstatus().equals("ACTIVO")) {
            parameters.put("P_FINISH_WORK_DATE", dateFormat.format(workerDetail.getDatFechaBaja()));
        }
        parameters.put("P_YEARS_WORKED", "" + yearsWorked);  // Consultar con Fiduciario: En teoria es: Hoy o FechaBaja - P_INIT_WORK_DATE: Es fecha de emision del reporte - fecha de inicio de trabajo
        parameters.put("P_RIGHTS_PERCENTAGE_ACQUIRED", decimalFormat.format(percentageRightsAcquired)); // Se calcula consultando con el valor obtenido anteriormente
        // 
        parameters.put("P_TRUST_NUMBER", "" + workerDetail.getDatContrato());
        parameters.put("P_SUBACCOUNT_NUMBER", workerDetail.getDatClave());
        parameters.put("P_GENERATION_PERIOD", rangeYearsWorked);
        parameters.put("P_CURRENCY_NAME", "Nacional");
        //
        parameters.put("P_TOTAL_PREVIOUS_BALANCE_WORKER", "$ 0.00");
        parameters.put("P_TOTAL_PREVIOUS_BALANCE_TOWNSHIP", "$ 0.00");
        //parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", "$ 307,936.00"); // Consultar a Fiduciario (Saldo inicial de Banamex, esta a 1 día antes de la fecha de adscripción)
        parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", currencyFormatter.format(grandTotalPreviousBalance)); // Consultar a Fiduciario (Saldo inicial de Banamex, esta a 1 día antes de la fecha de adscripción)
        parameters.put("GRANDTOTAL_PREVIOUS_BALANCE", grandTotalPreviousBalance);
        
        parameters.put("P_TOTAL_DEPOSITS_WORKER", currencyFormatter.format(totalDepositsWorker));
        parameters.put("P_TOTAL_DEPOSITS_TOWNSHIP", currencyFormatter.format(totalDepositsTownship));
        parameters.put("P_GRANDTOTAL_DEPOSITS", currencyFormatter.format(grandTotalDeposits));
        
        parameters.put("P_TOTAL_WITHDRAWS_WORKER", currencyFormatter.format(totalWithdrawsWorker));
        parameters.put("P_TOTAL_WITHDRAWS_TOWNSHIP", currencyFormatter.format(totalWithdrawsTownship));
        parameters.put("P_GRANDTOTAL_WITHDRAWS", currencyFormatter.format(grandTotalWithdraws));
        
        parameters.put("P_TOTAL_INTEREST_WORKER", currencyFormatter.format(totalInterestWorker));
        parameters.put("P_TOTAL_INTEREST_TOWNSHIP", currencyFormatter.format(totalInterestTownship));
        parameters.put("P_GRANDTOTAL_INTEREST", currencyFormatter.format(grandTotalInterest));

        Double totalCurrentBalanceWorker = totalDepositsWorker + totalInterestWorker - totalWithdrawsWorker - totalTransfersWorker;
        Double totalCurrentBalanceTownship = totalDepositsTownship + totalInterestTownship - totalWithdrawsTownship - totalTransfersTownship;
        Double grandTotalCurrentBalance = totalCurrentBalanceWorker + totalCurrentBalanceTownship;

        parameters.put("P_TOTAL_CURRENT_BALANCE_WORKER", currencyFormatter.format(totalCurrentBalanceWorker));
        parameters.put("P_TOTAL_CURRENT_BALANCE_TOWNSHIP", currencyFormatter.format(totalCurrentBalanceTownship));
        parameters.put("P_GRANDTOTAL_CURRENT_BALANCE", currencyFormatter.format(grandTotalCurrentBalance));
        // Rights Acquired Percentage
        Double townshipRightsPercentage = (percentageRightsAcquired / 100) * totalCurrentBalanceTownship;
        Double grandTotalRightsPercentage = totalCurrentBalanceWorker + townshipRightsPercentage;
        parameters.put("P_TOWNSHIP_RIGHTS_PERCENTAGE", currencyFormatter.format(townshipRightsPercentage));
        parameters.put("P_GRAND_TOTAL_RIGHTS_PERCENTAGE", currencyFormatter.format(grandTotalRightsPercentage));

        return parameters;
    }
    

    private List<IndividualReportAcount> getData(WorkerDetail workerDetail, Double previousBalance) throws ParseException {
        String sql;
        Double generalBalance = 0D;
        
        List<IndividualReportAcount> list = new ArrayList<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        
        String subaccount = workerDetail.getDatClave();
        parameterSource.addValue("subaccount", subaccount);
        /*
        if (previousBalance != 0D) { // Mas bien bandera preguntando si viene desde Banamex el trabajador
            List<Double> startTotalsList;
            List<Double> firstTotalsList;

            String subaccountBase = subaccount.substring(0, 9); // Ej: 1000000140 -> 100000014
            String subaccountWorkerDeposits = subaccountBase + "1"; // Depositos trabajador
            String subaccountWorkerInterest = subaccountBase + "5"; // Intereses trabajador
            String subaccountTownshipDeposits = subaccountBase + "3"; // Depositos Ayuntamiento
            String subaccountTownshipInterest = subaccountBase + "6"; // Intereses Ayuntamiento

            LocalDate startDate = LocalDate.parse("2021-07-15", isoFormatter); // Fixed to 2021-07-15
            LocalDate endDate = LocalDate.parse("2021-12-31", isoFormatter); // Fixed to 2021-12-31
            /*
            // En la tabla SALDOSSIRJUM se encuentra el detalle de los saldos de los trabajadores cuando se realizo el cambio de fideicomiso
            // Fecha 15-07-2021 Cambio Fideicomiso anterior a BCB
            // Fechas posteriores: Aportaciones e Intereses ya con BCB
            // 1 Y 5: Deposito e Interes de Trabajador
            // 3 y 6: Deposito e Interes de Ayuntamiento
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("contractNumber", workerDetail.getDatContrato(), Types.NUMERIC);
            parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits, Types.NUMERIC);
            parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest, Types.NUMERIC);
            parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits, Types.NUMERIC);
            parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest, Types.NUMERIC);
            parameters.addValue("startDate", startDate, Types.DATE);
            parameters.addValue("endDate", endDate, Types.DATE);

            sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER <= :startDate";

            startTotalsList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);
            IndividualReportAcount startWorkerDepositsIra = new IndividualReportAcount();
            startWorkerDepositsIra.setNameInversIra("DEPOSITO TRABAJADOR");
            startWorkerDepositsIra.setDepositsIra(startTotalsList.get(0));
            startWorkerDepositsIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount startWorkerInterestIra = new IndividualReportAcount();
            startWorkerInterestIra.setNameInversIra("INTERES TRABAJADOR");
            startWorkerInterestIra.setDepositsIra(startTotalsList.get(1));
            startWorkerInterestIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount startTownshipDepositsIra = new IndividualReportAcount();
            startTownshipDepositsIra.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
            startTownshipDepositsIra.setDepositsIra(startTotalsList.get(2));
            startTownshipDepositsIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount startTownshipInterestIra = new IndividualReportAcount();
            startTownshipInterestIra.setNameInversIra("INTERES H. AYUNTAMIENTO");
            startTownshipInterestIra.setDepositsIra(startTotalsList.get(2));
            startTownshipInterestIra.setDateIra("TRIMESTRE 3 2021");

            list.add(startWorkerDepositsIra);
            list.add(startWorkerInterestIra);
            list.add(startTownshipDepositsIra);
            list.add(startTownshipInterestIra);
            
            // Get List            
            sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL ";
            sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate ";

            firstTotalsList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);

            IndividualReportAcount firstWorkerDepositsIra = new IndividualReportAcount();
            firstWorkerDepositsIra.setNameInversIra("DEPOSITO TRABAJADOR");
            firstWorkerDepositsIra.setDepositsIra(firstTotalsList.get(0));
            firstWorkerDepositsIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount firstWorkerInterestIra = new IndividualReportAcount();
            firstWorkerInterestIra.setNameInversIra("INTERES TRABAJADOR");
            firstWorkerInterestIra.setDepositsIra(firstTotalsList.get(1));
            firstWorkerInterestIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount firstTownshipDepositsIra = new IndividualReportAcount();
            firstTownshipDepositsIra.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
            firstTownshipDepositsIra.setDepositsIra(firstTotalsList.get(2));
            firstTownshipDepositsIra.setDateIra("TRIMESTRE 3 2021");

            IndividualReportAcount firstTownshipInterestIra = new IndividualReportAcount();
            firstTownshipInterestIra.setNameInversIra("INTERES H. AYUNTAMIENTO");
            firstTownshipInterestIra.setDepositsIra(firstTotalsList.get(2));
            firstTownshipInterestIra.setDateIra("TRIMESTRE 3 2021");

            list.add(firstWorkerDepositsIra);
            list.add(firstWorkerInterestIra);
            list.add(firstTownshipDepositsIra);
            list.add(firstTownshipInterestIra);
            */
            /*
            sql = "SELECT * FROM SALDOSSIRJUM WHERE MOV_CLAVE_INV IN (:s1, :s2, :s3, :s4) AND MOV_FEC_OPER = :fechOper ORDER BY MOV_SECUENCIAL";
            IndividualReportAcount individualReportAcount = new IndividualReportAcount();
            individualReportAcount.setNameInversIra("SALDO ANTERIOR");
            individualReportAcount.setPartialBalanceIra(previousBalance);
            */

            // Segunda ronda 
            //sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = '1045' AND MOV_CLAVE_INV = '1000170111'";

            //list.add(individualReportAcount);
            // Si existe saldo anterior se encuentra dentro del primer trimestre por lo que no hay que considerar los movimientos de dicho periodo
            // Hay que integrar los saldos obtenidos de la tabla saldos sirjum y dividirlos por concepto
            // Hay que obtener los primeros registros del primer trimestre por concepto y restarle los conceptos iniciales

            //IndividualReportAcount record = getPreviousBalanceRecord(workerDetail, startDate);
            //generalBalance = record.getPartialBalanceIra();
            //list.add(getPreviousBalanceRecord(workerDetail, startDate));
        //}
        LocalDate startDate = LocalDate.parse("2024-07-01", isoFormatter); // Fixed to 2021-12-31
        IndividualReportAcount record = getPreviousBalanceRecord(workerDetail, startDate);
        generalBalance = record.getPartialBalanceIra();
        list.add(getPreviousBalanceRecord(workerDetail, startDate));

        sql = "SELECT * FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount ORDER BY RCI_SECUENCIAL ASC";
        
        for (IndividualReportAcount individualReportAcount2 : namedParameterJdbcTemplate.query(sql,parameterSource, new IndividualReportAccountRowMapper())) {
            generalBalance = generalBalance + individualReportAcount2.getDepositsIra() - individualReportAcount2.getWithdrawsIra();
            individualReportAcount2.setPartialBalanceIra(generalBalance);

            list.add(individualReportAcount2);
        }

        return list;
    }

    private IndividualReportAcount getPreviousBalanceRecord(WorkerDetail workerDetail, LocalDate startDate) {
        IndividualReportAcount record = new IndividualReportAcount();
        Double previousBalance = getPreviousBalance(workerDetail, startDate);
        record.setDateIra("-");
        record.setNameInversIra("SALDO ANTERIOR");
        record.setDepositsIra(0D);
        record.setWithdrawsIra(0D);
        record.setPartialBalanceIra(previousBalance);

        return record;
    }

    private Double getPreviousBalance(WorkerDetail workerDetail, LocalDate startDate) {
        String sql;
        List<Double> previousTotalBalanceList;
        String subaccountBase = workerDetail.getDatClave().substring(0, 9); // Ej: 1000000140 -> 100000014
        String subaccountWorkerDeposits = subaccountBase + "1"; // Depositos trabajador
        String subaccountWorkerInterest = subaccountBase + "5"; // Intereses trabajador
        String subaccountTownshipDeposits = subaccountBase + "3"; // Depositos Ayuntamiento
        String subaccountTownshipInterest = subaccountBase + "6"; // Intereses Ayuntamiento

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("contractNumber", workerDetail.getDatContrato(), Types.NUMERIC);
        parameters.addValue("startDate", startDate, Types.DATE);
        parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits, Types.NUMERIC);
        parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest, Types.NUMERIC);
        parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits, Types.NUMERIC);
        parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest, Types.NUMERIC);
        
        sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
        sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER <= :startDate UNION ALL ";
        sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER <= :startDate UNION ALL ";
        sql += "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER <= :startDate";

        previousTotalBalanceList = namedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);

        Double balance = 0D;
        for (Double partialBalance : previousTotalBalanceList) {
            balance += partialBalance;
        }

        return balance;
    }

    public static String convertListToJson(List<?> list) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }
}