package com.bcb.trust.front.service;

import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialRightsAcquiredRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialSelectedWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerBalanceRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerRepository;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialWorkerYearBalanceRepository;

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
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bcb.trust.front.entity.IndividualReportAcount;
import com.bcb.trust.front.entity.enums.ProcessDetailStateEnum;
import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;
import com.bcb.trust.front.model.bmtkfweb.mapper.PercentageRightsAcquiredMapper;
import com.bcb.trust.front.model.dto.WorkerDetail;
import com.bcb.trust.front.model.mapper.IndividualReportAccountRowMapper;
import com.bcb.trust.front.model.trusts.entity.ProcessDetailEntity;
import com.bcb.trust.front.model.trusts.entity.ProcessEntity;
import com.bcb.trust.front.model.trusts.enums.ProcessStateEnum;
import com.bcb.trust.front.model.trusts.enums.ProcessTypeEnum;
import com.bcb.trust.front.model.trusts.repository.ProcessDetailRepository;
import com.bcb.trust.front.model.trusts.repository.ProcessRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialPeriodEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialRightsAcquiredEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialSelectedWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerBalanceEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerYearBalanceEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustRepository;
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
    private TrustTrustRepository trustTrustRepository;
    
    @Autowired
    private TrustSpecialWorkerRepository trustSpecialWorkerRepository;

    @Autowired
    private TrustSpecialSelectedWorkerRepository trustSpecialSelectedWorkerRepository;

    @Autowired
    private TrustSpecialWorkerYearBalanceRepository trustSpecialWorkerYearBalanceRepository;

    @Autowired
    private TrustSpecialWorkerBalanceRepository trustSpecialWorkerBalanceRepository;

    @Autowired
    private TrustSpecialRightsAcquiredRepository rightsAcquiredRepository;

    private Map<Integer, Integer> trimesters = Map.ofEntries(
        entry(1, 1),
        entry(2, 1),
        entry(3, 1),
        entry(4, 2),
        entry(5, 2),
        entry(6, 2),
        entry(7, 3),
        entry(8, 3),
        entry(9, 3),
        entry(10, 4),
        entry(11, 4),
        entry(12, 4)
    );

    @Autowired
    @Qualifier("bmtkfwebNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate;

    // private int RECORDS_PER_CYCLE = 1000;
    private int RECORDS_PER_CYCLE = 4;

    private String primaryOutputPath = "./trusts/trust";

    private String secondaryOutputPath = "/reports/massive/";

    private Integer maxYearsRightsAcquired;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Date previousBalanceDate;

    private LocalDate startDate;

    private LocalDate endDate;

    DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter mexFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    DateTimeFormatter filedateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private DecimalFormat decimalFormat = new DecimalFormat("#.00");

    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    private ObjectMapper mapper = new ObjectMapper();

    private Double grandTotalPreviousBalance = 0D;

    private List<TrustSpecialWorkerBalanceEntity> currentPeriodBalanceList = new ArrayList<>();

    private TrustSpecialPeriodEntity startIntermediatePeriod;

    private TrustSpecialPeriodEntity endIntermediatePeriod;

    private TrustSpecialPeriodEntity startReportPeriod;

    private TrustSpecialPeriodEntity endReportPeriod;

    public void setStartIntermediatePeriod(TrustSpecialPeriodEntity startIntermediatePeriod) {
        this.startIntermediatePeriod = startIntermediatePeriod;
    }

    public void setEndIntermediatePeriod(TrustSpecialPeriodEntity endIntermediatePeriod) {
        this.endIntermediatePeriod = endIntermediatePeriod;
    }

    public void setStartReportPeriod(TrustSpecialPeriodEntity startReportPeriod) {
        this.startReportPeriod = startReportPeriod;
    }

    public void setEndReportPeriod(TrustSpecialPeriodEntity endReportPeriod) {
        this.endReportPeriod = endReportPeriod;
    }

    public void generateReport2(Integer trustNumber) {
        int workersProcessed = 0;
        double processPercentage = 0;
        String outputPath = primaryOutputPath + trustNumber + secondaryOutputPath;
        String fileName;
        Long totalWorkers = 0L;
        Long totalPages = 0L;
        List<TrustSpecialSelectedWorkerEntity> selectedWorkerList = new ArrayList<>();
        Page<TrustSpecialSelectedWorkerEntity> workerPage;
        List<TrustSpecialRightsAcquiredEntity> rightsAcquiredList = new ArrayList<>();
        
        try {
            TrustTrustEntity trustEntity = trustTrustRepository.findOneByNumber(trustNumber).orElse(null);
            
            ClassPathResource resource = new ClassPathResource("TemplateJson.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());

            rightsAcquiredList = rightsAcquiredRepository.findAll();

            outputPath += LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
            System.out.println("OutputPath: " + outputPath);

            Path path = Paths.get(outputPath);
            Files.createDirectories(path);
            
            totalWorkers = trustSpecialSelectedWorkerRepository.countByContractNumber(trustNumber);
            totalPages = (totalWorkers / RECORDS_PER_CYCLE) + 1;
            
            System.out.println("Workers for process: " + totalWorkers);
            System.out.println("Pages: " + totalPages);
            
            if (trustEntity != null && totalPages > 0) {
                /*
                ProcessEntity process = new ProcessEntity();
                process.setType(ProcessTypeEnum.MASSIVE_REPORT_GENERATION);
                process.setState(ProcessStateEnum.STARTED);
                process.setTotalElements(totalWorkers);
                process.setElementsProcessed(0L);
                process.setProcessPercent(processPercentage);
                process.setCreatedAt(new Date());
                // Save the main process
                processRepository.saveAndFlush(process);
                
                // The process
                
                // End of process
                process.setState(ProcessStateEnum.FINISHED);
                processRepository.saveAndFlush(process);
                */
                
                Pageable pageable = PageRequest.of(0, RECORDS_PER_CYCLE);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("P_TRUST_NAME", "FIDEICOMISO SIRJUM MÉRIDA");
                List<IndividualReportAcount> reportDetail;
                Double previousBalance = 0D;
                //for (int i = 0; i < totalPages; i++) {
                for (int i = 0; i < 2; i++) {
                    // Get the next list of workers
                    workerPage = trustSpecialSelectedWorkerRepository.findByContractNumber(trustNumber, pageable);
                    selectedWorkerList = workerPage.getContent();

                    // Do the process with the list of workers
                    for (TrustSpecialSelectedWorkerEntity selectedWorkerEntity : workerPage.getContent()) {
                        //System.out.println(selectedWorkerEntity.toString());
                        if (selectedWorkerEntity.getAccount().equals("1000000270")) {
                            
                            // Get worker entity bean
                            TrustSpecialWorkerEntity workerEntity = trustSpecialWorkerRepository.findByTrustEntityAndAccount(trustEntity, selectedWorkerEntity.getAccount()).orElse(null);
                            // Get annual balances for worker
                            //List<TrustSpecialWorkerYearBalanceEntity> workerYearBalanceList = trustSpecialWorkerYearBalanceRepository.findByWorkerEntity(workerEntity); //.stream().map(balance -> balance.getAmount()).toList();
    
                            // Get monthly balances for worker
                            List<TrustSpecialWorkerBalanceEntity> workerBalanceList = trustSpecialWorkerBalanceRepository.findByWorkerEntity(workerEntity);
                            // Prepare data for report
                            initializeReportHeader(workerEntity, parameters, rightsAcquiredList);
                            generateHeaderData(parameters, workerBalanceList);
                            reportDetail = getWorkerMovements(currentPeriodBalanceList, 0D);
                            // Fill & save report
                            String jsonData = mapper.writeValueAsString(reportDetail);
                            ByteArrayInputStream jsonDataInputStream = new ByteArrayInputStream(jsonData.getBytes());
                            JsonDataSource jsonDataSource = new JsonDataSource(jsonDataInputStream);
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jsonDataSource);
    
                            fileName = workerEntity.getAccount() + "_" + workerEntity.getName().replace(" ", "_") + "_" + LocalDateTime.now().format(filedateFormatter) + ".pdf";
    
                            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + "/" + fileName);
                            System.out.println("Filename: " + fileName + " generated");
                        }
                    }

                    // Update pageable for next page
                    pageable = PageRequest.of(pageable.getPageNumber() + 1, RECORDS_PER_CYCLE);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Proceso terminado");
    }

    private List<IndividualReportAcount> getWorkerMovements(List<TrustSpecialWorkerBalanceEntity> workerBalanceList, Double previousBalance) {
        List<IndividualReportAcount> dataList = new ArrayList<>();
        Integer count = 0;
        Integer monthInteger = 0;
        String periodLegend = "TRIMESTRE %s %s";

        Double workerDeposits = 0D;
        Double workerInterest = 0D;
        Double townshipDeposits = 0D;
        Double townshipInterest = 0D;

        Double workerWithdraws = 0D;
        Double workerInterestWithdraw = 0D;
        Double townshipWithdraws = 0D;
        Double townshipInterestWithdraw = 0D;

        Double generalBalance = 0D;
        String previousPeriod;
        List<TrustSpecialWorkerBalanceEntity> sublistOne = workerBalanceList.subList(0, 3);
        List<TrustSpecialWorkerBalanceEntity> sublistTwo = workerBalanceList.subList(3, 6);

        IndividualReportAcount rowWorkerDepositsT1 = new IndividualReportAcount();
        IndividualReportAcount rowWorkerInterestT1 = new IndividualReportAcount();
        IndividualReportAcount rowTownshipDepositsT1 = new IndividualReportAcount();
        IndividualReportAcount rowTownshipInterestT1 = new IndividualReportAcount();
        
        IndividualReportAcount rowWorkerDepositsT2 = new IndividualReportAcount();
        IndividualReportAcount rowWorkerInterestT2 = new IndividualReportAcount();
        IndividualReportAcount rowTownshipDepositsT2 = new IndividualReportAcount();
        IndividualReportAcount rowTownshipInterestT2 = new IndividualReportAcount();


        System.out.println("TamanioS1: " + sublistOne.size());
        System.out.println("TamanioS2: " + sublistTwo.size());

        try {
            // Insert first row
            generalBalance = grandTotalPreviousBalance;
            previousPeriod = workerBalanceList.getFirst().getWorkerEntity().getStartWorkDate().format(mexFormatter) + " al " + startReportPeriod.getStartDate().format(mexFormatter);
            IndividualReportAcount rowInitialBalance = new IndividualReportAcount();
            rowInitialBalance.setDateIra(previousPeriod);
            rowInitialBalance.setNameInversIra("SALDO ANTERIOR");
            rowInitialBalance.setDepositsIra(0.0);
            rowInitialBalance.setWithdrawsIra(0.0);
            rowInitialBalance.setPartialBalanceIra(grandTotalPreviousBalance);

            dataList.add(rowInitialBalance);
            // T1
            for (TrustSpecialWorkerBalanceEntity workerBalanceEntity : sublistOne) {
                workerDeposits += workerBalanceEntity.getWorkerDeposits();
                workerInterest += workerBalanceEntity.getWorkerInterest();
                townshipDeposits += workerBalanceEntity.getTownshipDeposits();
                townshipInterest += workerBalanceEntity.getTownshipInterest();

                workerWithdraws += workerBalanceEntity.getWorkerWithdraw();
                workerInterestWithdraw += workerBalanceEntity.getNegativeWorkerInterest();
                townshipWithdraws += workerBalanceEntity.getTownshipWithdraw();
                townshipInterestWithdraw += workerBalanceEntity.getNegativeTownshipInterest();

                System.out.println("Anio: " + workerBalanceEntity.getPeriodEntity().getYearEntity().getYear() + ", FINI: " + workerBalanceEntity.getPeriodEntity().getStartDate().format(mexFormatter) + ", FFIN: " + workerBalanceEntity.getPeriodEntity().getEndDate().format(mexFormatter));
                System.out.println(workerBalanceEntity.toString());
                monthInteger = workerBalanceEntity.getPeriodEntity().getStartDate().getMonthValue();
            }

            // DEPOSITO TRABAJADOR
            rowWorkerDepositsT1.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowWorkerDepositsT1.setNameInversIra("DEPOSITO TRABAJADOR");
            rowWorkerDepositsT1.setDepositsIra(workerDeposits);
            rowWorkerDepositsT1.setWithdrawsIra(workerWithdraws);
            generalBalance = generalBalance + workerDeposits - workerWithdraws;
            rowWorkerDepositsT1.setPartialBalanceIra(generalBalance);
            // INTERES TRABAJADOR
            rowWorkerInterestT1.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowWorkerInterestT1.setNameInversIra("INTERES TRABAJADOR");
            rowWorkerInterestT1.setDepositsIra(workerInterest);
            rowWorkerInterestT1.setWithdrawsIra(workerInterestWithdraw);
            generalBalance = generalBalance + workerInterest - workerInterestWithdraw;
            rowWorkerInterestT1.setPartialBalanceIra(generalBalance);
            // DEPOSITO H.AYUNTAMIENTO
            rowTownshipDepositsT1.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowTownshipDepositsT1.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
            rowTownshipDepositsT1.setDepositsIra(townshipDeposits);
            rowTownshipDepositsT1.setWithdrawsIra(townshipWithdraws);
            generalBalance = generalBalance + townshipDeposits - townshipWithdraws;
            rowTownshipDepositsT1.setPartialBalanceIra(generalBalance);
            // INTERES H. AYUNTAMIENTO
            rowTownshipInterestT1.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowTownshipInterestT1.setNameInversIra("INTERES H. AYUNTAMIENTO");
            rowTownshipInterestT1.setDepositsIra(townshipInterest);
            rowTownshipInterestT1.setWithdrawsIra(townshipInterestWithdraw);

            generalBalance = (generalBalance + townshipInterest) - townshipInterestWithdraw;
            rowTownshipInterestT1.setPartialBalanceIra(generalBalance);

            // Add all rows
            dataList.add(rowWorkerDepositsT1);
            dataList.add(rowWorkerInterestT1);
            dataList.add(rowTownshipDepositsT1);
            dataList.add(rowTownshipInterestT1);

            workerDeposits = 0D;
            workerInterest = 0D;
            townshipDeposits = 0D;
            townshipInterest = 0D;

            workerWithdraws = 0D;
            workerInterestWithdraw = 0D;
            townshipWithdraws = 0D;
            townshipInterestWithdraw = 0D;

            // T2
            for (TrustSpecialWorkerBalanceEntity workerBalanceEntity : sublistTwo) {
                workerDeposits += workerBalanceEntity.getWorkerDeposits();
                workerInterest += workerBalanceEntity.getWorkerInterest();
                townshipDeposits += workerBalanceEntity.getTownshipDeposits();
                townshipInterest += workerBalanceEntity.getTownshipInterest();

                workerWithdraws += workerBalanceEntity.getWorkerWithdraw();
                workerInterestWithdraw += workerBalanceEntity.getNegativeWorkerInterest();
                townshipWithdraws += workerBalanceEntity.getTownshipWithdraw();
                townshipInterestWithdraw += workerBalanceEntity.getNegativeTownshipInterest();

                System.out.println("Anio: " + workerBalanceEntity.getPeriodEntity().getYearEntity().getYear() + ", FINI: " + workerBalanceEntity.getPeriodEntity().getStartDate().format(mexFormatter) + ", FFIN: " + workerBalanceEntity.getPeriodEntity().getEndDate().format(mexFormatter));
                System.out.println(workerBalanceEntity.toString());
                monthInteger = workerBalanceEntity.getPeriodEntity().getStartDate().getMonthValue();
            }

            // DEPOSITO TRABAJADOR
            rowWorkerDepositsT2.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowWorkerDepositsT2.setNameInversIra("DEPOSITO TRABAJADOR");
            rowWorkerDepositsT2.setDepositsIra(workerDeposits);
            rowWorkerDepositsT2.setWithdrawsIra(workerWithdraws);
            generalBalance = generalBalance + workerDeposits - workerWithdraws;
            rowWorkerDepositsT2.setPartialBalanceIra(generalBalance);
            // INTERES TRABAJADOR
            rowWorkerInterestT2.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowWorkerInterestT2.setNameInversIra("INTERES TRABAJADOR");
            rowWorkerInterestT2.setDepositsIra(workerInterest);
            rowWorkerInterestT2.setWithdrawsIra(workerInterestWithdraw);
            generalBalance = generalBalance + workerInterest - workerInterestWithdraw;
            rowWorkerInterestT2.setPartialBalanceIra(generalBalance);
            // DEPOSITO H.AYUNTAMIENTO
            rowTownshipDepositsT2.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowTownshipDepositsT2.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
            rowTownshipDepositsT2.setDepositsIra(townshipDeposits);
            rowTownshipDepositsT2.setWithdrawsIra(townshipWithdraws);
            generalBalance = generalBalance + townshipDeposits - townshipWithdraws;
            rowTownshipDepositsT2.setPartialBalanceIra(generalBalance);
            // INTERES H. AYUNTAMIENTO
            rowTownshipInterestT2.setDateIra(String.format(periodLegend, trimesters.get(monthInteger), endReportPeriod.getEndDate().getYear()));
            rowTownshipInterestT2.setNameInversIra("INTERES H. AYUNTAMIENTO");
            rowTownshipInterestT2.setDepositsIra(townshipInterest);
            rowTownshipInterestT2.setWithdrawsIra(townshipInterestWithdraw);

            generalBalance = (generalBalance + townshipInterest) - townshipInterestWithdraw;
            rowTownshipInterestT2.setPartialBalanceIra(generalBalance);

            // Add all rows
            dataList.add(rowWorkerDepositsT2);
            dataList.add(rowWorkerInterestT2);
            dataList.add(rowTownshipDepositsT2);
            dataList.add(rowTownshipInterestT2);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }

        return dataList;
    }

    private void generateReports() {

    }

    private void initializeReportHeader(TrustSpecialWorkerEntity workerEntity, Map<String, Object> parameters, List<TrustSpecialRightsAcquiredEntity> rightsAcquiredList) {
        Integer yearsWorked;
        Double percentageRightsAcquired;
        Period period;
        String generationPeriod;
        
        try {
            generationPeriod = this.startReportPeriod.getStartDate().format(mexFormatter) + " al " + this.endReportPeriod.getEndDate().format(mexFormatter);
            period = Period.between(workerEntity.getStartWorkDate(), this.endReportPeriod.getEndDate());
            yearsWorked = period.getYears();

            if (yearsWorked >= rightsAcquiredList.getLast().getYear()) {
                percentageRightsAcquired = 100D;
            } else {
                percentageRightsAcquired = rightsAcquiredList.stream()
                    .filter(ra -> ra.getYear() == yearsWorked)
                    .findFirst().get().getPercent();
            }
            
            parameters.put("P_WORKER_FULLNAME", workerEntity.getName());
            parameters.put("P_WORKER_WORKCENTER", workerEntity.getDepartment());
            parameters.put("P_REGISTRATION_TRUST_DATE", workerEntity.getStartWorkDate().format(mexFormatter)); // Cambio de Administración
            //parameters.put("P_DEPARTMENT_NAME", "");
            parameters.put("P_INIT_WORK_DATE", workerEntity.getStartWorkDate().format(mexFormatter));
            parameters.put("P_FINISH_WORK_DATE", "");
            if (!workerEntity.getStatus().equals(1)) {
                parameters.put("P_FINISH_WORK_DATE", workerEntity.getEndWorkDate().format(mexFormatter));
            }
            parameters.put("P_YEARS_WORKED", "" + yearsWorked);  // Consultar con Fiduciario: En teoria es: Hoy o FechaBaja - P_INIT_WORK_DATE: Es fecha de emision del reporte - fecha de inicio de trabajo
            parameters.put("P_RIGHTS_PERCENTAGE_ACQUIRED", decimalFormat.format(percentageRightsAcquired)); // Se calcula consultando con el valor obtenido anteriormente
            parameters.put("RIGHTS_PERCENTAGE_ACQUIRED", percentageRightsAcquired); // Se calcula consultando con el valor obtenido anteriormente

            parameters.put("P_TRUST_NUMBER", workerEntity.getTrustEntity().getNumber() + "");
            parameters.put("P_SUBACCOUNT_NUMBER", workerEntity.getAccount());
            parameters.put("P_GENERATION_PERIOD", generationPeriod);
            parameters.put("P_CURRENCY_NAME", "NACIONAL");
        } catch (Exception e) {
            System.out.println("ReportService::initializeReportHeader" + e.getLocalizedMessage());
        }
    }

    

    private void generateHeaderData(Map<String, Object> parameters, List<TrustSpecialWorkerBalanceEntity> workerBalanceList) {
        // Initial balances for transition period
        Double workerDepositsTransitionBalance = 0D;
        Double workerInterestTransitionBalance = 0D;
        Double townshipDepositsTransitionBalance = 0D;
        Double townshipInterestTransitionBalance = 0D;
        Double totalTransitionBalance = 0D;
        // Intermediate balances
        Double workerDepositIntermediateBalance = 0D;
        Double workerInterestIntermediateBalance = 0D;
        Double townshipDepositIntermediateBalance = 0D;
        Double townshipInterestIntermediateBalance = 0D;

        Double workerWithdrawIntermediateBalance = 0D;
        Double negativeWorkerInterestIntermediateBalance = 0D;
        Double townshipWithdrawIntermediateBalance = 0D;
        Double negativeTownshipInterestIntermediateBalance = 0D;
        
        Double depositIntermediateBalance = 0D;
        Double withdrawIntermediateBalance = 0D;
        Double totalIntermediateBalance = 0D;
        // Period balance
        // Income movements
        Double workerDeposits = 0D;
        Double townshipDeposits = 0D;
        Double workerInterest = 0D;
        Double townshipInterest = 0D;
        // Outcome movements
        Double workerWithdraw = 0D;
        Double negativeWorkerInterest = 0D;
        Double townshipWithdraw = 0D;
        Double negativeTownshipInterest = 0D;
        List<TrustSpecialWorkerBalanceEntity> intermediateBalanceList = new ArrayList<>();
        
        Double totalPeriodBalance = 0D;
        
        // Generals
        Double totalPreviousBalanceWorker = 0D;
        Double totalPreviousBalanceTownship = 0D;
        
        
        Double totalCurrentBalanceWorker = 0D;
        Double totalCurrentBalanceTownship = 0D;
        Double grandTotalCurrentBalance = 0D;

        

        try {
            // check if worker has balances for transition period
            TrustSpecialWorkerBalanceEntity transitionBalanceEntity = workerBalanceList.stream()
                .filter(balance -> balance.getPeriodEntity().getPeriodId().equals(1L))
                .findFirst()
                .orElse(null);

            if (transitionBalanceEntity != null) {
                workerDepositsTransitionBalance = transitionBalanceEntity.getWorkerDeposits() != null ? transitionBalanceEntity.getWorkerDeposits() : 0D;
                workerInterestTransitionBalance = transitionBalanceEntity.getWorkerInterest() != null ? transitionBalanceEntity.getWorkerInterest() : 0D;
                townshipDepositsTransitionBalance = transitionBalanceEntity.getTownshipDeposits() != null ? transitionBalanceEntity.getTownshipDeposits() : 0D;
                townshipInterestTransitionBalance = transitionBalanceEntity.getTownshipInterest() != null ? transitionBalanceEntity.getTownshipInterest() : 0D;

                totalTransitionBalance = workerDepositsTransitionBalance + workerInterestTransitionBalance + townshipDepositsTransitionBalance + townshipInterestTransitionBalance;
            }

            // Intermediate Balance
            intermediateBalanceList = workerBalanceList.stream()
                .filter(balance -> balance.getPeriodEntity().getPeriodId() >= startIntermediatePeriod.getPeriodId() && balance.getPeriodEntity().getPeriodId() <= endIntermediatePeriod.getPeriodId())
                .collect(Collectors.toList());

            for (TrustSpecialWorkerBalanceEntity trustSpecialWorkerBalanceEntity : intermediateBalanceList) {
                if (trustSpecialWorkerBalanceEntity.getPeriodEntity().getPeriodId() == 1) { // skip transition period, already calculated
                    continue;
                }

                workerDepositIntermediateBalance += trustSpecialWorkerBalanceEntity.getWorkerDeposits() != null ? trustSpecialWorkerBalanceEntity.getWorkerDeposits() : 0D;
                workerInterestIntermediateBalance += trustSpecialWorkerBalanceEntity.getWorkerInterest() != null ? trustSpecialWorkerBalanceEntity.getWorkerInterest() : 0D;
                townshipDepositIntermediateBalance += trustSpecialWorkerBalanceEntity.getTownshipDeposits() != null ? trustSpecialWorkerBalanceEntity.getTownshipDeposits() : 0D;
                townshipInterestIntermediateBalance += trustSpecialWorkerBalanceEntity.getTownshipInterest() != null ? trustSpecialWorkerBalanceEntity.getTownshipInterest() : 0D;

                workerWithdrawIntermediateBalance += trustSpecialWorkerBalanceEntity.getWorkerWithdraw() != null ? trustSpecialWorkerBalanceEntity.getWorkerWithdraw() : 0D;
                negativeWorkerInterestIntermediateBalance += trustSpecialWorkerBalanceEntity.getNegativeWorkerInterest() != null ? trustSpecialWorkerBalanceEntity.getNegativeWorkerInterest() : 0D;
                townshipWithdrawIntermediateBalance += trustSpecialWorkerBalanceEntity.getTownshipWithdraw() != null ? trustSpecialWorkerBalanceEntity.getTownshipWithdraw() : 0D;
                negativeTownshipInterestIntermediateBalance += trustSpecialWorkerBalanceEntity.getNegativeTownshipInterest() != null ? trustSpecialWorkerBalanceEntity.getNegativeTownshipInterest() : 0D;

                if (trustSpecialWorkerBalanceEntity.getPeriodEntity().getPeriodId() == 11 || trustSpecialWorkerBalanceEntity.getPeriodEntity().getPeriodId() == 28) {
                    System.out.println("Anio: " + trustSpecialWorkerBalanceEntity.getPeriodEntity().getYearEntity().getYear() + ", FINI: " + trustSpecialWorkerBalanceEntity.getPeriodEntity().getStartDate().format(mexFormatter) + ", FFIN: " + trustSpecialWorkerBalanceEntity.getPeriodEntity().getEndDate().format(mexFormatter));
                    System.out.println(trustSpecialWorkerBalanceEntity.toString());
                }
            }
            depositIntermediateBalance = workerDepositIntermediateBalance + workerInterestIntermediateBalance + townshipDepositIntermediateBalance + townshipInterestIntermediateBalance;
            withdrawIntermediateBalance = workerWithdrawIntermediateBalance + negativeWorkerInterestIntermediateBalance + townshipWithdrawIntermediateBalance + negativeTownshipInterestIntermediateBalance;
            totalIntermediateBalance = depositIntermediateBalance - withdrawIntermediateBalance;

            // Current Balance
            currentPeriodBalanceList = workerBalanceList.stream()
                .filter(balance -> balance.getPeriodEntity().getPeriodId() >= startReportPeriod.getPeriodId() && balance.getPeriodEntity().getPeriodId() <= endReportPeriod.getPeriodId())
                .collect(Collectors.toList());
            // Current Balance // Lista filtrada
            for (TrustSpecialWorkerBalanceEntity trustSpecialWorkerBalanceEntity : currentPeriodBalanceList) {
                workerDeposits += trustSpecialWorkerBalanceEntity.getWorkerDeposits() != null ? trustSpecialWorkerBalanceEntity.getWorkerDeposits() : 0D;
                workerInterest += trustSpecialWorkerBalanceEntity.getWorkerInterest() != null ? trustSpecialWorkerBalanceEntity.getWorkerInterest() : 0D;
                townshipDeposits += trustSpecialWorkerBalanceEntity.getTownshipDeposits() != null ? trustSpecialWorkerBalanceEntity.getTownshipDeposits() : 0D;
                townshipInterest += trustSpecialWorkerBalanceEntity.getTownshipInterest() != null ? trustSpecialWorkerBalanceEntity.getTownshipInterest() : 0D;
                
                workerWithdraw += trustSpecialWorkerBalanceEntity.getWorkerWithdraw() != null ? trustSpecialWorkerBalanceEntity.getWorkerWithdraw() : 0D;
                negativeWorkerInterest += trustSpecialWorkerBalanceEntity.getNegativeWorkerInterest() != null ? trustSpecialWorkerBalanceEntity.getNegativeWorkerInterest() : 0D;
                townshipWithdraw += trustSpecialWorkerBalanceEntity.getTownshipWithdraw() != null ? trustSpecialWorkerBalanceEntity.getTownshipWithdraw() : 0D;
                negativeTownshipInterest += trustSpecialWorkerBalanceEntity.getNegativeTownshipInterest() != null ? trustSpecialWorkerBalanceEntity.getNegativeTownshipInterest() : 0D;
            }
            totalPeriodBalance = (workerDeposits + workerInterest + townshipDeposits + townshipInterest) - (negativeWorkerInterest + negativeTownshipInterest + workerWithdraw + townshipWithdraw);

            // End of calculation, sum
            totalPreviousBalanceWorker = (workerDepositsTransitionBalance + workerInterestTransitionBalance + workerDepositIntermediateBalance + workerInterestIntermediateBalance) - (workerWithdrawIntermediateBalance + negativeWorkerInterestIntermediateBalance);
            totalPreviousBalanceTownship = (townshipDepositsTransitionBalance + townshipInterestTransitionBalance + townshipDepositIntermediateBalance + townshipInterestIntermediateBalance) - (townshipWithdrawIntermediateBalance + negativeTownshipInterestIntermediateBalance);
            grandTotalPreviousBalance = totalPreviousBalanceWorker + totalPreviousBalanceTownship;

            totalCurrentBalanceWorker = totalPreviousBalanceWorker + (workerDeposits + workerInterest - workerWithdraw - negativeWorkerInterest);
            totalCurrentBalanceTownship = totalPreviousBalanceTownship + (townshipDeposits + townshipInterest - townshipWithdraw - negativeTownshipInterest);
            grandTotalCurrentBalance = totalCurrentBalanceWorker + totalCurrentBalanceTownship;
            

            //Double totalPreviousBalanceWorker = workerDepositsTransitionBalance + workerInterestTransitionBalance + workerDepositIntermediateBalance + workerInterestIntermediateBalance;
            //totalPreviousBalanceWorker -= (workerWithdrawIntermediateBalance + workerNegativeInterestIntermediateBalance);
            // Sugerencia Conrado
            //Double totalPreviousBalanceTownship = townshipDepositsTransitionBalance + townshipInterestTransitionBalance + townshipDepositIntermediateBalance + townshipInterestIntermediateBalance; 
            //totalPreviousBalanceTownship -= (townshipWithdrawIntermediateBalance + townshipNegativeInterestIntermediateBalance);

            //Double grandTotalPreviousBalance = totalTransitionBalance + totalIntermediateBalance;
            // Original
            // Double totalDepositsWorker = workerDepositsTransitionBalance + workerDepositIntermediateBalance + workerDeposits;
            // Sugerencia Conrado
            Double totalDepositsWorker = workerDeposits;
            // Sugerencia Conrado
            Double totalDepositsTownship = townshipDeposits; 
            // Sugerencia Conrado
            Double grandTotalDeposits = totalDepositsWorker + totalDepositsTownship;
            // Sugerencia Conrado
            Double totalInterestWorker = workerInterest - negativeWorkerInterest - workerWithdraw;
            // Sugerencia Conrado
            Double totalInterestTownship = townshipInterest - negativeTownshipInterest - townshipWithdraw;
            // Sugerencia Conrado
            Double grandTotalInterest = totalInterestWorker + totalInterestTownship;

            //Double totalCurrentBalanceWorker = (workerDepositsTransitionBalance + workerDepositIntermediateBalance + workerDeposits) + (workerInterestTransitionBalance + workerInterestIntermediateBalance + workerInterest) - (workerWithdraw + negativeWorkerInterest);
            //Double totalCurrentBalanceTownship = (townshipDepositsTransitionBalance + townshipDepositIntermediateBalance + townshipDeposits) + (townshipInterestTransitionBalance + townshipInterestIntermediateBalance + townshipInterest) - (townshipWithdraw + negativeTownshipInterest);
            Double townshipRightsPercentage = 0D;
            Double grandTotalRightsPercentage = 0D;
            Double percentageRightsAcquired = (Double) parameters.get("RIGHTS_PERCENTAGE_ACQUIRED");

            townshipRightsPercentage = (percentageRightsAcquired / 100) * totalCurrentBalanceTownship;
            grandTotalRightsPercentage = totalCurrentBalanceWorker + townshipRightsPercentage;
    
            parameters.put("P_TOTAL_PREVIOUS_BALANCE_WORKER", currencyFormatter.format(totalPreviousBalanceWorker));
            parameters.put("P_TOTAL_PREVIOUS_BALANCE_TOWNSHIP", currencyFormatter.format(totalPreviousBalanceTownship));
            //parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", "$ 307,936.00"); // Consultar a Fiduciario (Saldo inicial de Banamex, esta a 1 día antes de la fecha de adscripción)
            parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", currencyFormatter.format(grandTotalPreviousBalance)); // Consultar a Fiduciario (Saldo inicial de Banamex, esta a 1 día antes de la fecha de adscripción)
            parameters.put("GRANDTOTAL_PREVIOUS_BALANCE", grandTotalPreviousBalance);
    
            parameters.put("P_TOTAL_DEPOSITS_WORKER", currencyFormatter.format(totalDepositsWorker));
            parameters.put("P_TOTAL_DEPOSITS_TOWNSHIP", currencyFormatter.format(totalDepositsTownship));
            parameters.put("P_GRANDTOTAL_DEPOSITS", currencyFormatter.format(grandTotalDeposits));
    
            parameters.put("P_TOTAL_WITHDRAWS_WORKER", currencyFormatter.format(workerWithdraw + negativeWorkerInterest));
            parameters.put("P_TOTAL_WITHDRAWS_TOWNSHIP", currencyFormatter.format(townshipWithdraw + negativeTownshipInterest));
            parameters.put("P_GRANDTOTAL_WITHDRAWS", currencyFormatter.format(workerWithdraw + negativeWorkerInterest + townshipWithdraw + negativeTownshipInterest));
    
            parameters.put("P_TOTAL_INTEREST_WORKER", currencyFormatter.format(totalInterestWorker));
            parameters.put("P_TOTAL_INTEREST_TOWNSHIP", currencyFormatter.format(totalInterestTownship));
            parameters.put("P_GRANDTOTAL_INTEREST", currencyFormatter.format(grandTotalInterest));
    
            parameters.put("P_TOTAL_CURRENT_BALANCE_WORKER", currencyFormatter.format(totalCurrentBalanceWorker));
            parameters.put("P_TOTAL_CURRENT_BALANCE_TOWNSHIP", currencyFormatter.format(totalCurrentBalanceTownship));
            parameters.put("P_GRANDTOTAL_CURRENT_BALANCE", currencyFormatter.format(grandTotalCurrentBalance));
    
            parameters.put("P_TOWNSHIP_RIGHTS_PERCENTAGE", currencyFormatter.format(townshipRightsPercentage));
            parameters.put("P_GRAND_TOTAL_RIGHTS_PERCENTAGE", currencyFormatter.format(grandTotalRightsPercentage));
            
        } catch (Exception e) {
            System.out.println("ReportService::generateHeaderData:" + e.getLocalizedMessage());
        }
    }

    public void generateReport() throws ParseException {
        this.previousBalanceDate = simpleDateFormat.parse("2021-07-15");
        // System.out.println("Fecha: " +
        // simpleDateFormat.format(this.previousBalanceDate));
        int workersProcessed = 0;
        double processPercentage = 0;
        String outputPath = primaryOutputPath + "1045" + secondaryOutputPath;
        String fileName;
        List<IndividualReportAcount> dataList;

        try {
            Connection connection = bmtkfwebNamedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
            File reportTemplate = ResourceUtils.getFile("classpath:TemplateJson.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());

            int totalWorkers = 4; // For development purposes
            // int totalWorkers = legacyService.getTotalWorkers(1045); // Uncomment
            System.out.println("Workers for process: " + totalWorkers);
            String account = ""; // null or empty on first iteration
            List<WorkerDetail> workerList = legacyService.getWorkerList(1045, account, RECORDS_PER_CYCLE);

            if (!workerList.isEmpty()) {

                String sql = "SELECT MAX(DER_ANT) FROM FID_DER_ADQ";
                // maxYearsRightsAcquired = (Integer)
                // namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, new
                // MapSqlParameterSource().addValue("yearsWorked", yearsWorked), Integer.class);
                maxYearsRightsAcquired = (Integer) bmtkfwebNamedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql,
                        Integer.class);

                // Define output dir for pdf reports
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddhhmmss");
                outputPath += dateFormat.format(new Date());
                System.out.println("OutputPath: " + outputPath);

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
                            fileName = workerDetail.getDatClave() + "_" + workerDetail.getDatDato().replace(" ", "_")
                                    + "_" + dateFormat.format(new Date()) + ".pdf";
                            // Get worker details
                            System.out.println(workerDetail.toString());
                            // Get worker movements

                            // Fill & save report
                            // dataList = getData(workerDetail);
                            // JRBeanCollectionDataSource dataSource = new
                            // JRBeanCollectionDataSource(dataList);

                            Map<String, Object> parameters = this.getParameters(workerDetail);
                            Double previousBalance = (Double) parameters.get("GRANDTOTAL_PREVIOUS_BALANCE");
                            dataList = getData(workerDetail, previousBalance);
                            String jsonData = convertListToJson(dataList);
                            ByteArrayInputStream jsonDataInputStream = new ByteArrayInputStream(jsonData.getBytes());
                            JsonDataSource jsonDataSource = new JsonDataSource(jsonDataInputStream);

                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jsonDataSource);

                            // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                            // parameters, connection);
                            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + "/" + fileName);
                            // Inform worker processed
                            // Check if pause / stop is requested
                            //

                            // Save the detail
                            processDetail.setState(1);
                            jsonObject.put("status", 1);

                        } catch (Exception e) {
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
                    workerList = legacyService.getWorkerList(1045, account, RECORDS_PER_CYCLE);

                    // Update the main process
                    processPercentage = (workersProcessed * 100) / totalWorkers;
                    process.setProcessPercent(processPercentage);
                    process.setElementsProcessed(workersProcessed);
                    processRepository.saveAndFlush(process);
                }

                // End of process
                process.setState(ProcessStateEnum.FINISHED);
                processRepository.saveAndFlush(process);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void exportPDF(String outputPath, String account) {
        try {
            // String outputPath = "./";
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddhhmmss");

            Connection connection = bmtkfwebNamedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection();

            File reportTemplate = ResourceUtils.getFile("classpath:Template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());

            // Map<String, Object> parameters = getParameters("1000166050");
            // parameters.put("ex", "value");
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            String fileName = account + dateFormat.format(now) + ".pdf";
            System.out.println("Exportando en: " + outputPath + fileName);
            // JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath +
            // fileName);
            System.out.println("Exportado");
            // return jasperPrint;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            // return null;
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
        Double percentageRightsAcquired = 0D;

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

        totalsList = bmtkfwebNamedParameterJdbcTemplate.queryForList(sql, sqlParameterSource, Double.class);

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
        sql = "SELECT RCI_PERIODO FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount FETCH FIRST 1 ROWS ONLY";
        rangeYearsWorked = (String) bmtkfwebNamedParameterJdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("subaccount", subaccount), String.class);

        LocalDate dateStartWork = Instant.ofEpochMilli(workerDetail.getDatFechaAlta().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateEndWork = LocalDate.parse(rangeYearsWorked.split("[^\\{alpha}]al[^\\{alpha}]")[1], mexFormatter);

        Period period = Period.between(dateStartWork, dateEndWork);
        yearsWorked = period.getYears();

        if (yearsWorked >= maxYearsRightsAcquired) {
            percentageRightsAcquired = 100D;
        } else {
            sql = "SELECT DER_PCJ FROM FID_DER_ADQ WHERE DER_ANT = :yearsWorked FETCH FIRST 1 ROWS ONLY";
            percentageRightsAcquired = (Double) bmtkfwebNamedParameterJdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource().addValue("yearsWorked", yearsWorked), Double.class);
        }
        // TODO parametrizar el valor de inicio de
        sqlParameterSource = new MapSqlParameterSource();

        sqlParameterSource.addValue("fechOper", this.previousBalanceDate);
        sqlParameterSource.addValue("sub1", subaccountPreviousBalance + "1");
        sqlParameterSource.addValue("sub3", subaccountPreviousBalance + "3");
        sqlParameterSource.addValue("sub5", subaccountPreviousBalance + "5");
        sqlParameterSource.addValue("sub6", subaccountPreviousBalance + "6");
        sql = "SELECT SUM(MOV_IMPORTE) AS SALDO_ANTERIOR FROM SALDOSSIRJUM WHERE MOV_CLAVE_INV IN (:sub1, :sub3, :sub5, :sub6) AND MOV_FEC_OPER <= :fechOper";

        grandTotalPreviousBalance = (Double) bmtkfwebNamedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource,
                Double.class);
        grandTotalPreviousBalance = grandTotalPreviousBalance == null ? 0D : grandTotalPreviousBalance;

        LocalDate startPeriodDate = LocalDate.parse("2024-07-01", isoFormatter);
        grandTotalPreviousBalance = getPreviousBalance(workerDetail, startPeriodDate);

        Map<String, Object> parameters = new HashMap<>();
        // Data Report Header
        parameters.put("P_TRUST_NAME", "FIDEICOMISO SIRJUM MÉRIDA");
        parameters.put("P_WORKER_FULLNAME", workerDetail.getDatDato());
        parameters.put("P_WORKER_WORKCENTER", workerDetail.getDatDescripcion());
        parameters.put("P_REGISTRATION_TRUST_DATE", dateFormat.format(workerDetail.getDatFechaAlta())); // Cambio de
                                                                                                        // Administración
        // parameters.put("P_DEPARTMENT_NAME", "");
        parameters.put("P_INIT_WORK_DATE", dateFormat.format(workerDetail.getDatFecUltMod()));
        parameters.put("P_FINISH_WORK_DATE", "");
        if (!workerDetail.getDatEstatus().equals("ACTIVO")) {
            parameters.put("P_FINISH_WORK_DATE", dateFormat.format(workerDetail.getDatFechaBaja()));
        }
        parameters.put("P_YEARS_WORKED", "" + yearsWorked); // Consultar con Fiduciario: En teoria es: Hoy o FechaBaja -
                                                            // P_INIT_WORK_DATE: Es fecha de emision del reporte - fecha
                                                            // de inicio de trabajo
        parameters.put("P_RIGHTS_PERCENTAGE_ACQUIRED", decimalFormat.format(percentageRightsAcquired)); // Se calcula
                                                                                                        // consultando
                                                                                                        // con el valor
                                                                                                        // obtenido
                                                                                                        // anteriormente
        //
        parameters.put("P_TRUST_NUMBER", "" + workerDetail.getDatContrato());
        parameters.put("P_SUBACCOUNT_NUMBER", workerDetail.getDatClave());
        parameters.put("P_GENERATION_PERIOD", rangeYearsWorked);
        parameters.put("P_CURRENCY_NAME", "Nacional");
        //
        parameters.put("P_TOTAL_PREVIOUS_BALANCE_WORKER", "$ 0.00");
        parameters.put("P_TOTAL_PREVIOUS_BALANCE_TOWNSHIP", "$ 0.00");
        // parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", "$ 307,936.00"); // Consultar
        // a Fiduciario (Saldo inicial de Banamex, esta a 1 día antes de la fecha de
        // adscripción)
        parameters.put("P_GRANDTOTAL_PREVIOUS_BALANCE", currencyFormatter.format(grandTotalPreviousBalance)); // Consultar
                                                                                                              // a
                                                                                                              // Fiduciario
                                                                                                              // (Saldo
                                                                                                              // inicial
                                                                                                              // de
                                                                                                              // Banamex,
                                                                                                              // esta a
                                                                                                              // 1 día
                                                                                                              // antes
                                                                                                              // de la
                                                                                                              // fecha
                                                                                                              // de
                                                                                                              // adscripción)
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

        Double totalCurrentBalanceWorker = totalDepositsWorker + totalInterestWorker - totalWithdrawsWorker
                - totalTransfersWorker;
        Double totalCurrentBalanceTownship = totalDepositsTownship + totalInterestTownship - totalWithdrawsTownship
                - totalTransfersTownship;
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

    private List<IndividualReportAcount> getData(WorkerDetail workerDetail, Double previousBalance)
            throws ParseException {
        String sql;
        Double generalBalance = 0D;

        List<IndividualReportAcount> list = new ArrayList<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        String subaccount = workerDetail.getDatClave();
        parameterSource.addValue("subaccount", subaccount);
        /*
         * if (previousBalance != 0D) { // Mas bien bandera preguntando si viene desde
         * Banamex el trabajador
         * List<Double> startTotalsList;
         * List<Double> firstTotalsList;
         * 
         * String subaccountBase = subaccount.substring(0, 9); // Ej: 1000000140 ->
         * 100000014
         * String subaccountWorkerDeposits = subaccountBase + "1"; // Depositos
         * trabajador
         * String subaccountWorkerInterest = subaccountBase + "5"; // Intereses
         * trabajador
         * String subaccountTownshipDeposits = subaccountBase + "3"; // Depositos
         * Ayuntamiento
         * String subaccountTownshipInterest = subaccountBase + "6"; // Intereses
         * Ayuntamiento
         * 
         * LocalDate startDate = LocalDate.parse("2021-07-15", isoFormatter); // Fixed
         * to 2021-07-15
         * LocalDate endDate = LocalDate.parse("2021-12-31", isoFormatter); // Fixed to
         * 2021-12-31
         * /*
         * // En la tabla SALDOSSIRJUM se encuentra el detalle de los saldos de los
         * trabajadores cuando se realizo el cambio de fideicomiso
         * // Fecha 15-07-2021 Cambio Fideicomiso anterior a BCB
         * // Fechas posteriores: Aportaciones e Intereses ya con BCB
         * // 1 Y 5: Deposito e Interes de Trabajador
         * // 3 y 6: Deposito e Interes de Ayuntamiento
         * MapSqlParameterSource parameters = new MapSqlParameterSource();
         * parameters.addValue("contractNumber", workerDetail.getDatContrato(),
         * Types.NUMERIC);
         * parameters.addValue("subaccountWorkerDeposits", subaccountWorkerDeposits,
         * Types.NUMERIC);
         * parameters.addValue("subaccountWorkerInterest", subaccountWorkerInterest,
         * Types.NUMERIC);
         * parameters.addValue("subaccountTownshipDeposits", subaccountTownshipDeposits,
         * Types.NUMERIC);
         * parameters.addValue("subaccountTownshipInterest", subaccountTownshipInterest,
         * Types.NUMERIC);
         * parameters.addValue("startDate", startDate, Types.DATE);
         * parameters.addValue("endDate", endDate, Types.DATE);
         * 
         * sql =
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER <= :startDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER <= :startDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER <= :startDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER <= :startDate"
         * ;
         * 
         * startTotalsList = namedParameterJdbcTemplate.queryForList(sql, parameters,
         * Double.class);
         * IndividualReportAcount startWorkerDepositsIra = new IndividualReportAcount();
         * startWorkerDepositsIra.setNameInversIra("DEPOSITO TRABAJADOR");
         * startWorkerDepositsIra.setDepositsIra(startTotalsList.get(0));
         * startWorkerDepositsIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount startWorkerInterestIra = new IndividualReportAcount();
         * startWorkerInterestIra.setNameInversIra("INTERES TRABAJADOR");
         * startWorkerInterestIra.setDepositsIra(startTotalsList.get(1));
         * startWorkerInterestIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount startTownshipDepositsIra = new
         * IndividualReportAcount();
         * startTownshipDepositsIra.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
         * startTownshipDepositsIra.setDepositsIra(startTotalsList.get(2));
         * startTownshipDepositsIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount startTownshipInterestIra = new
         * IndividualReportAcount();
         * startTownshipInterestIra.setNameInversIra("INTERES H. AYUNTAMIENTO");
         * startTownshipInterestIra.setDepositsIra(startTotalsList.get(2));
         * startTownshipInterestIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * list.add(startWorkerDepositsIra);
         * list.add(startWorkerInterestIra);
         * list.add(startTownshipDepositsIra);
         * list.add(startTownshipInterestIra);
         * 
         * // Get List
         * sql =
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountWorkerInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipDeposits AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate UNION ALL "
         * ;
         * sql +=
         * "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND WHERE MOV_CONTRATO = :contractNumber AND MOV_CLAVE_INV = :subaccountTownshipInterest AND MOV_FEC_OPER > :startDate AND MOV_FEC_OPER <= :endDate "
         * ;
         * 
         * firstTotalsList = namedParameterJdbcTemplate.queryForList(sql, parameters,
         * Double.class);
         * 
         * IndividualReportAcount firstWorkerDepositsIra = new IndividualReportAcount();
         * firstWorkerDepositsIra.setNameInversIra("DEPOSITO TRABAJADOR");
         * firstWorkerDepositsIra.setDepositsIra(firstTotalsList.get(0));
         * firstWorkerDepositsIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount firstWorkerInterestIra = new IndividualReportAcount();
         * firstWorkerInterestIra.setNameInversIra("INTERES TRABAJADOR");
         * firstWorkerInterestIra.setDepositsIra(firstTotalsList.get(1));
         * firstWorkerInterestIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount firstTownshipDepositsIra = new
         * IndividualReportAcount();
         * firstTownshipDepositsIra.setNameInversIra("DEPOSITO H. AYUNTAMIENTO");
         * firstTownshipDepositsIra.setDepositsIra(firstTotalsList.get(2));
         * firstTownshipDepositsIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * IndividualReportAcount firstTownshipInterestIra = new
         * IndividualReportAcount();
         * firstTownshipInterestIra.setNameInversIra("INTERES H. AYUNTAMIENTO");
         * firstTownshipInterestIra.setDepositsIra(firstTotalsList.get(2));
         * firstTownshipInterestIra.setDateIra("TRIMESTRE 3 2021");
         * 
         * list.add(firstWorkerDepositsIra);
         * list.add(firstWorkerInterestIra);
         * list.add(firstTownshipDepositsIra);
         * list.add(firstTownshipInterestIra);
         */
        /*
         * sql =
         * "SELECT * FROM SALDOSSIRJUM WHERE MOV_CLAVE_INV IN (:s1, :s2, :s3, :s4) AND MOV_FEC_OPER = :fechOper ORDER BY MOV_SECUENCIAL"
         * ;
         * IndividualReportAcount individualReportAcount = new IndividualReportAcount();
         * individualReportAcount.setNameInversIra("SALDO ANTERIOR");
         * individualReportAcount.setPartialBalanceIra(previousBalance);
         */

        // Segunda ronda
        // sql = "SELECT COALESCE(SUM(MOV_IMPORTE), 0) AS TOTAL FROM FID_MOV_CTAS_IND
        // WHERE MOV_CONTRATO = '1045' AND MOV_CLAVE_INV = '1000170111'";

        // list.add(individualReportAcount);
        // Si existe saldo anterior se encuentra dentro del primer trimestre por lo que
        // no hay que considerar los movimientos de dicho periodo
        // Hay que integrar los saldos obtenidos de la tabla saldos sirjum y dividirlos
        // por concepto
        // Hay que obtener los primeros registros del primer trimestre por concepto y
        // restarle los conceptos iniciales

        // IndividualReportAcount record = getPreviousBalanceRecord(workerDetail,
        // startDate);
        // generalBalance = record.getPartialBalanceIra();
        // list.add(getPreviousBalanceRecord(workerDetail, startDate));
        // }
        LocalDate startDate = LocalDate.parse("2024-07-01", isoFormatter); // Fixed to 2021-12-31
        IndividualReportAcount record = getPreviousBalanceRecord(workerDetail, startDate);
        generalBalance = record.getPartialBalanceIra();
        list.add(getPreviousBalanceRecord(workerDetail, startDate));

        sql = "SELECT * FROM REPCTAIND WHERE RCI_NUM_N2 = :subaccount ORDER BY RCI_SECUENCIAL ASC";

        for (IndividualReportAcount individualReportAcount2 : bmtkfwebNamedParameterJdbcTemplate.query(sql, parameterSource,
                new IndividualReportAccountRowMapper())) {
            generalBalance = generalBalance + individualReportAcount2.getDepositsIra()
                    - individualReportAcount2.getWithdrawsIra();
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

        previousTotalBalanceList = bmtkfwebNamedParameterJdbcTemplate.queryForList(sql, parameters, Double.class);

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