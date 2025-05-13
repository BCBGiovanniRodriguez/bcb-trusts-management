CREATE TABLE `IntermediateBalance` (
    `TrustNumber` INT NOT NULL,
    `WorkerAccount` VARCHAR(10) NOT NULL,
    `StartDate` DATE NOT NULL,
    `EndDate` DATE NOT NULL,
    `WorkerDeposits` DECIMAL(20,4) NOT NULL,
    `WorkerInterest` DECIMAL(20,4) NOT NULL,
    `TownshipDeposits` DECIMAL(20,4) NOT NULL,
    `TownshipInterest` DECIMAL(20,4) NOT NULL,
    `WithdrawWorker` DECIMAL(20,4) NOT NULL,
    `NegativeWorkerInterest` DECIMAL(20,4) NOT NULL,
    `WithdrawTownship` DECIMAL(20,4) NOT NULL,
    `NegativeTownshipInterest` DECIMAL(20,4) NOT NULL,
    `Created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);