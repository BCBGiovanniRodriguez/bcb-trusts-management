CREATE TABLE `TransitionBalance` (
    `TrustNumber` INT NOT NULL,
    `WorkerAccount` VARCHAR(10) NOT NULL,
    `WorkerDeposits` DECIMAL(20,4) NOT NULL,
    `WorkerInterest` DECIMAL(20,4) NOT NULL,
    `TownshipDeposits` DECIMAL(20,4) NOT NULL,
    `TownshipInterest` DECIMAL(20,4) NOT NULL,
    `Created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);