package com.bcb.trust.front.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bcb.trust.front.entity.IndividualReportAcount;
import com.bcb.trust.front.entity.enums.ProcessDetailStateEnum;
import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;
import com.bcb.trust.front.model.dto.WorkerDetail;
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
import net.sf.jasperreports.json.data.JsonDataSource;

@Service
public class MassiveReportService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessDetailRepository processDetailRepository;

    @Autowired
    private LegacyService legacyService;

    @Autowired
    private IndividualReportService individualReportService;

    private int RECORDS_PER_CYCLE = 500;

    private String primaryOutputPath = "./trusts/trust";

    private String secondaryOutputPath = "/reports/massive/";

    DateTimeFormatter filedateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 
     * @param trustNumber
     */
    public void process(Integer trustNumber) {
        LocalDateTime now = LocalDateTime.now();
        String outputPath = primaryOutputPath + trustNumber + secondaryOutputPath + now.format(filedateFormatter);
        String fileName;
        Integer workersProcessed = 0;
        Integer totalWorkers = 0;
        individualReportService.setTrustNumber(trustNumber);
        List<LocalDate> periodList;
        double processPercentage = 0D;
        
        try {
            periodList = legacyService.getPeriodList(trustNumber);
            ClassPathResource resource = new ClassPathResource("TemplateJson.jrxml");
            //File reportTemplate = ResourceUtils.getFile("classpath:TemplateJson.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            //JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());
            
            totalWorkers = legacyService.getTotalWorkers(trustNumber); // Uncomment
            System.out.println("Workers for process: " + totalWorkers);
            String account = ""; // null or empty on first iteration
            List<WorkerDetail> workerList = legacyService.getWorkerList(trustNumber, account, RECORDS_PER_CYCLE);
            individualReportService.setPercentageRightsAcquiredList(legacyService.getRightsPercentageList());
            
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

            if (!workerList.isEmpty()) {
                
                while (workersProcessed < totalWorkers) {
                    for (WorkerDetail workerDetail : workerList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("worker", workerDetail.getDatDato());
                        jsonObject.put("workerAccount", workerDetail.getDatClave());
                        
                        ProcessDetailEntity processDetail = new ProcessDetailEntity();
                        processDetail.setProcess(process);

                        try {
                            
                            // Prepare the service
                            individualReportService.setSubaccountWorker(workerDetail.getDatClave());
                            individualReportService.setStartDate(periodList.get(0));
                            individualReportService.setEndDate(periodList.get(1));
                            individualReportService.setWorkerDetail(workerDetail);
                            
                            individualReportService.generate();
                            
                            Map<String, Object> parameters = individualReportService.getParameters();
                            String jsonData = convertListToJson(individualReportService.getWorkerMovementList());
                            ByteArrayInputStream jsonDataInputStream = new ByteArrayInputStream(jsonData.getBytes());
                            JsonDataSource jsonDataSource = new JsonDataSource(jsonDataInputStream);
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jsonDataSource);
                            
                            fileName = workerDetail.getDatClave() + "_" + workerDetail.getDatDato().replace(" ", "_") + "_" + now.format(filedateFormatter) + ".pdf";
                            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath + "/" + fileName);

                            System.out.println("Filename: " + fileName + " generated");

                            processDetail.setProcessDetailState(ProcessDetailStateEnum.PROCESSED);
                            processDetail.setFileName(fileName);
                            jsonObject.put("status", 1);
                        } catch (Exception e) {
                            System.out.println("MassiveReportService:: " + e.getLocalizedMessage());
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
                    workerList = legacyService.getWorkerList(trustNumber, account, RECORDS_PER_CYCLE);

                    // Update the main process
                    processPercentage = (workersProcessed * 100) / totalWorkers;
                    process.setProcessPercentage(processPercentage);
                    process.setElementsProcessed(workersProcessed);
                    processRepository.saveAndFlush(process);

                    //break; // Validar que se obtienen los siguientes trabajadores
                }

                // End of process
                process.setProcessState(ProcessStateEnum.FINISHED);
                processRepository.saveAndFlush(process);

            }

        } catch (Exception e) {
            System.out.println("Error on MassiveReportService::process " + e.getLocalizedMessage());
        }
        System.out.println("MassiveReportService::process finished!!!");
    }

    public static String convertListToJson(List<?> list) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }
}
