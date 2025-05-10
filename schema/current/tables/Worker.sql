
CREATE TABLE `Worker` (
    `WorkerId` INT NOT NULL AUTO_INCREMENT,
    `TrustNumber` INT NOT NULL,
    `WorkerAccount` VARCHAR(10) NOT NULL UNIQUE,
    `Month` TINYINT NOT NULL,
    `Year` TINYINT NOT NULL,
    `StartDate` DATE NOT NULL,
    `EndDate` DATE NOT NULL,
    
    `WorkerDeposits` DECIMAL(20,4) NOT NULL,
    `WorkerInterest` DECIMAL(20,4) NOT NULL,
    `TownshipDeposits` DECIMAL(20,4) NOT NULL,
    `TownshipInterest` DECIMAL(20,4) NOT NULL,
    `negativeWorkerInterest` DECIMAL(20,4) NOT NULL,
    `negativeTownshipInterest` DECIMAL(20,4) NOT NULL,
    `WithdrawWorker` DECIMAL(20,4) NOT NULL,
    `WithdrawTownship` DECIMAL(20,4) NOT NULL,
    `TransferWorker` DECIMAL(20,4) NOT NULL,
    `TransferTownship` DECIMAL(20,4) NOT NULL,
    `Created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);