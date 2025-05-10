package com.bcb.trust.front.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.entity.IndividualReportAcount;
import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;
import com.bcb.trust.front.model.bmtkfweb.service.WorkerMovementService;
import com.bcb.trust.front.model.dto.WorkerDetail;

@Service
public class IndividualReportService {

    @Autowired
    private WorkerMovementService workerMovementService;
    
    // Input information

    private WorkerDetail workerDetail;

    private List<LocalDate> periodList;

    private List<PercentageRightsAcquired> percentageRightsAcquiredList;

    private Integer trustNumber;

    private String subaccountWorker;

    private LocalDate startDate;

    private LocalDate endDate;

    // Output information

    private Map<String, Object> parameters;

    List<IndividualReportAcount> workerMovementList;

    // Formatters

    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    private DateTimeFormatter filedateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeFormatter reportDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    private DecimalFormat decimalFormat = new DecimalFormat("#.00"); 

    //public IndividualReportService() { }

    public void generate() {
        List<Double> transitionBalanceList = new ArrayList<>();
        List<Double> workerBalanceList = new ArrayList<>();
        List<Double> workerMovementsBalanceList = new ArrayList<>();

        try {
            if (trustNumber == null || subaccountWorker == null || startDate == null || (percentageRightsAcquiredList == null || percentageRightsAcquiredList.isEmpty())) {
                throw new Exception("Verificar dependencias del Servicio");
            } else {
                // From FID_MOV_CTAS_IND Previous to 2021-07-15
                transitionBalanceList = workerMovementService.getTransitionBalanceList(trustNumber, subaccountWorker);
                // From FID_MOV_CTAS_IND StartDate = 2021-07-15 EndDate - Start Date of period requested Example: 2024-07-01 
                workerBalanceList = workerMovementService.getWorkerBalanceList(trustNumber, subaccountWorker, workerMovementService.getTransitionDate(), startDate);
                // From REPCTAIND
                workerMovementsBalanceList = workerMovementService.getWorkerMovementsBalanceList(trustNumber, subaccountWorker);

                // Generate Report Header Data
                initializeReportParameters();
                generateHeaderData(transitionBalanceList, workerBalanceList, workerMovementsBalanceList);
                // Generate Report Data
                workerMovementList = workerMovementService.getWorkerMovements(trustNumber, subaccountWorker, (Double) parameters.get("GRANDTOTAL_PREVIOUS_BALANCE"));
            }
        } catch (Exception e) {
            System.out.println("Error en IndividualReportService::generate " + e.getLocalizedMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    private void initializeReportParameters() throws Exception {
        if (this.workerDetail == null) {
            throw new Exception("Error on IndividualReportService::initializeReportParameters");
        } else {
            // Prepare the data
            parameters = (parameters == null || parameters.isEmpty()) ? new HashMap<>() : parameters;
            Integer yearsWorked;
            Double percentageRightsAcquired;
            LocalDate dateStartWork = Instant.ofEpochMilli(workerDetail.getDatFechaAlta().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            Period period = Period.between(dateStartWork, this.endDate);
            String rangeYearsWorked = startDate.format(reportDateFormatter) + " al " + endDate.format(reportDateFormatter);
            
            yearsWorked = period.getYears();
            if (yearsWorked >= percentageRightsAcquiredList.getLast().getYear()) {
                percentageRightsAcquired = 100D;
            } else {
                percentageRightsAcquired = percentageRightsAcquiredList.stream()
                    .filter(pra -> pra.getYear() == yearsWorked)
                    .collect(Collectors.toList()).getFirst().getPercentage();
            }

            // Populate params
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
            parameters.put("RIGHTS_PERCENTAGE_ACQUIRED", percentageRightsAcquired); // Se calcula consultando con el valor obtenido anteriormente

            parameters.put("P_TRUST_NUMBER", "" + workerDetail.getDatContrato());
            parameters.put("P_SUBACCOUNT_NUMBER", workerDetail.getDatClave());
            parameters.put("P_GENERATION_PERIOD", rangeYearsWorked);
            parameters.put("P_CURRENCY_NAME", "NACIONAL");
        }
    }

    private void generateHeaderData(List<Double> transitionBalanceList, List<Double> workerBalanceList, List<Double> workerMovementsBalanceList) throws Exception {
        // Transition balance
        Double workerDepositsTransitionBalance = transitionBalanceList.get(0);
        Double workerInterestTransitionBalance = transitionBalanceList.get(1);
        Double townshipDepositsTransitionBalance = transitionBalanceList.get(2);
        Double townshipInterestTransitionBalance = transitionBalanceList.get(3);

        Double totalTransitionBalance = workerDepositsTransitionBalance + workerInterestTransitionBalance + townshipDepositsTransitionBalance + townshipInterestTransitionBalance;
        if (false) {
            System.out.println("DepositosTrabajador: " + workerDepositsTransitionBalance);
            System.out.println("DepositosAyuntamiento: " + townshipDepositsTransitionBalance);
            System.out.println("InteresTrabajador: " + workerInterestTransitionBalance);
            System.out.println("InteresAyuntamiento: " + townshipInterestTransitionBalance);
            System.out.println("===========");
        }
        // Intermediate balance
        // Withdraws & Transfers not visualised
        Double workerDepositIntermediateBalance = workerBalanceList.get(0) - workerBalanceList.get(4);
        Double workerInterestIntermediateBalance = workerBalanceList.get(1) - workerBalanceList.get(5);
        Double townshipDepositIntermediateBalance = workerBalanceList.get(2) - workerBalanceList.get(6);
        Double townshipInterestIntermediateBalance = workerBalanceList.get(3) - workerBalanceList.get(7);

        if (false) {
            System.out.println("DepositosTrabajador: " + workerBalanceList.get(0) + " - " + workerBalanceList.get(4));
            System.out.println("DepositosAyuntamiento: " + workerBalanceList.get(1) + " - " + workerBalanceList.get(5));
            System.out.println("Interes Trabajador: " + workerBalanceList.get(2) + " - " + workerBalanceList.get(6));
            System.out.println("Interes Ayuntamiento: " + workerBalanceList.get(3) + " - " + workerBalanceList.get(7));
            System.out.println("===========");
        }

        Double grandTotalIntermediateBalance = workerDepositIntermediateBalance + townshipDepositIntermediateBalance + workerInterestIntermediateBalance + townshipInterestIntermediateBalance;

        // Period balance
        // Income movements
        Double workerDeposits = workerMovementsBalanceList.get(0);
        Double townshipDeposits = workerMovementsBalanceList.get(1);
        Double workerInterest = workerMovementsBalanceList.get(2);
        Double townshipInterest = workerMovementsBalanceList.get(3);
        // Outcome movements
        Double negativeWorkerInterest = workerMovementsBalanceList.get(4);
        Double negativeTownshipInterest = workerMovementsBalanceList.get(5);
        Double withdrawWorker = workerMovementsBalanceList.get(6);
        Double withdrawTownship = workerMovementsBalanceList.get(7);
        Double transferWorker = workerMovementsBalanceList.get(8);
        Double transferTownship = workerMovementsBalanceList.get(9);

        if (true) {
            System.out.println("DepositosTrabajador: " + workerDeposits);
            System.out.println("DepositosAyuntamiento: " + townshipDeposits);
            System.out.println("InteresTrabajador: " + workerInterest);
            System.out.println("InteresAyuntamiento: " + townshipInterest);
            System.out.println("InteresNegativoTrabajador: " + negativeWorkerInterest);
            System.out.println("InteresNegativoAyuntamiento: " + negativeTownshipInterest);
            System.out.println("RetiroTrabajador: " + withdrawWorker);
            System.out.println("RetiroAyuntamiento: " + withdrawTownship);
            System.out.println("TraspasoTrabajador: " + transferWorker);
            System.out.println("TraspasoAyuntamiento: " + transferTownship);
        }

        // Previous Balance 
        // Sugerencia Conrado
        Double totalPreviousBalanceWorker = workerDepositsTransitionBalance + workerInterestTransitionBalance + workerDepositIntermediateBalance + workerInterestIntermediateBalance;
        // Sugerencia Conrado
        Double totalPreviousBalanceTownship = townshipDepositsTransitionBalance + townshipInterestTransitionBalance + townshipDepositIntermediateBalance + townshipInterestIntermediateBalance; 
        Double grandTotalPreviousBalance = totalTransitionBalance + grandTotalIntermediateBalance;
        // Original
        // Double totalDepositsWorker = workerDepositsTransitionBalance + workerDepositIntermediateBalance + workerDeposits;
        // Sugerencia Conrado
        Double totalDepositsWorker = workerDeposits;
        // Original
        // Double totalDepositsTownship = townshipDepositsTransitionBalance + townshipDepositIntermediateBalance + townshipDeposits;
        // Sugerencia Conrado
        Double totalDepositsTownship = townshipDeposits; // Sugerencia Conrado
        Double grandTotalDeposits = totalDepositsWorker + totalDepositsTownship;
        // Original
        //Double totalInterestWorker = workerInterestTransitionBalance + workerInterestIntermediateBalance + workerInterest - negativeWorkerInterest - withdrawWorker - transferWorker;
        // Sugerencia Conrado
        Double totalInterestWorker = workerInterest - negativeWorkerInterest - withdrawWorker - transferWorker;
        // Original
        //Double totalInterestTownship = townshipInterestTransitionBalance + townshipInterestIntermediateBalance + townshipInterest - negativeTownshipInterest - withdrawTownship - transferTownship;
        // Sugerencia Conrado
        Double totalInterestTownship = townshipInterest - negativeTownshipInterest - withdrawTownship - transferTownship;
        // Original
        //Double grandTotalInterest = (workerInterestTransitionBalance + workerInterestIntermediateBalance + workerInterest - negativeWorkerInterest - withdrawWorker - transferWorker) + (townshipInterestTransitionBalance + townshipInterestIntermediateBalance + townshipInterest - negativeTownshipInterest - withdrawTownship - transferTownship);
        // Sugerencia Conrado
        Double grandTotalInterest = totalInterestWorker + totalInterestTownship;

        Double totalWithdrawsWorker = 0D;
        Double totalWithdrawsTownship = 0D;
        Double grandTotalWithdraws = 0D;

        Double totalTransfersWorker = 0D;
        Double totalTransfersTownship = 0D;
        Double grandTotalTransfers = 0D;

        Double totalCurrentBalanceWorker = (workerDepositsTransitionBalance + workerDepositIntermediateBalance + workerDeposits) + (workerInterestTransitionBalance + workerInterestIntermediateBalance + workerInterest - negativeWorkerInterest - withdrawWorker - transferWorker);
        Double totalCurrentBalanceTownship = (townshipDepositsTransitionBalance + townshipDepositIntermediateBalance + townshipDeposits) + (townshipInterestTransitionBalance + townshipInterestIntermediateBalance + townshipInterest - negativeTownshipInterest - withdrawTownship - transferTownship);
        Double grandTotalCurrentBalance = totalCurrentBalanceWorker + totalCurrentBalanceTownship;

        Double townshipRightsPercentage = 0D;
        Double grandTotalRightsPercentage = 0D;

        
        Double percentageRightsAcquired = (Double) parameters.get("RIGHTS_PERCENTAGE_ACQUIRED");
        
        // Worker Deposits 
        //totalDepositsWorker += transitionBalanceList.get(0) + workerBalanceList.get(0)+ workerMovementsBalanceList.get(0) - workerBalanceList.get(4);
        // Worker Interest
        //totalInterestWorker += transitionBalanceList.get(1) + workerBalanceList.get(1) + workerMovementsBalanceList.get(1) - workerBalanceList.get(5);
        // Township Deposits
        //totalDepositsTownship += transitionBalanceList.get(2) + workerBalanceList.get(2) + workerMovementsBalanceList.get(2) - workerBalanceList.get(6);
        // Township Interest
        //totalInterestTownship += transitionBalanceList.get(3) + workerBalanceList.get(3) + workerMovementsBalanceList.get(2) - workerBalanceList.get(7);

        //grandTotalDeposits += totalDepositsWorker + totalDepositsTownship;
        //grandTotalInterest += totalInterestWorker + totalInterestTownship;

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

        parameters.put("P_TOTAL_WITHDRAWS_WORKER", currencyFormatter.format(0D));
        parameters.put("P_TOTAL_WITHDRAWS_TOWNSHIP", currencyFormatter.format(0D));
        parameters.put("P_GRANDTOTAL_WITHDRAWS", currencyFormatter.format(0D));

        parameters.put("P_TOTAL_INTEREST_WORKER", currencyFormatter.format(totalInterestWorker));
        parameters.put("P_TOTAL_INTEREST_TOWNSHIP", currencyFormatter.format(totalInterestTownship));
        parameters.put("P_GRANDTOTAL_INTEREST", currencyFormatter.format(grandTotalInterest));

        parameters.put("P_TOTAL_CURRENT_BALANCE_WORKER", currencyFormatter.format(totalCurrentBalanceWorker));
        parameters.put("P_TOTAL_CURRENT_BALANCE_TOWNSHIP", currencyFormatter.format(totalCurrentBalanceTownship));
        parameters.put("P_GRANDTOTAL_CURRENT_BALANCE", currencyFormatter.format(grandTotalCurrentBalance));

        parameters.put("P_TOWNSHIP_RIGHTS_PERCENTAGE", currencyFormatter.format(townshipRightsPercentage));
        parameters.put("P_GRAND_TOTAL_RIGHTS_PERCENTAGE", currencyFormatter.format(grandTotalRightsPercentage));
    }

    public Integer getTrustNumber() {
        return trustNumber;
    }

    public void setTrustNumber(Integer contractNumber) {
        this.trustNumber = contractNumber;
    }

    public String getSubaccountWorker() {
        return subaccountWorker;
    }

    public void setSubaccountWorker(String subaccountWorkerBase) {
        this.subaccountWorker = subaccountWorkerBase;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public WorkerMovementService getWorkerMovementService() {
        return workerMovementService;
    }

    public void setWorkerMovementService(WorkerMovementService workerMovementService) {
        this.workerMovementService = workerMovementService;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public NumberFormat getCurrencyFormatter() {
        return currencyFormatter;
    }

    public void setCurrencyFormatter(NumberFormat currencyFormatter) {
        this.currencyFormatter = currencyFormatter;
    }

    public WorkerDetail getWorkerDetail() {
        return workerDetail;
    }

    public void setWorkerDetail(WorkerDetail workerDetail) {
        this.workerDetail = workerDetail;
    }

    public List<LocalDate> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<LocalDate> periodList) {
        this.periodList = periodList;
    }

    public List<PercentageRightsAcquired> getPercentageRightsAcquiredList() {
        return percentageRightsAcquiredList;
    }

    public void setPercentageRightsAcquiredList(List<PercentageRightsAcquired> percentageRightsAcquiredList) {
        this.percentageRightsAcquiredList = percentageRightsAcquiredList;
    }

    public List<IndividualReportAcount> getWorkerMovementList() {
        return workerMovementList;
    }

    public void setWorkerMovementList(List<IndividualReportAcount> workerMovementList) {
        this.workerMovementList = workerMovementList;
    }

    public DateTimeFormatter getFiledateFormatter() {
        return filedateFormatter;
    }

    public void setFiledateFormatter(DateTimeFormatter filedateFormatter) {
        this.filedateFormatter = filedateFormatter;
    }

    public DateTimeFormatter getReportDateFormatter() {
        return reportDateFormatter;
    }

    public void setReportDateFormatter(DateTimeFormatter reportDateFormatter) {
        this.reportDateFormatter = reportDateFormatter;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }
}
