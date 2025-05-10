CREATE TABLE IF NOT EXISTS Country (
    CountryId INT PRIMARY KEY, -- Unique identifier for each country
    Name VARCHAR(50) NOT NULL, -- Name of the country
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE State (
    StateId INT PRIMARY KEY, -- Unique identifier for each state
    CountryId INT NOT NULL, -- Foreign key referencing the country
    Name VARCHAR(50) NOT NULL, -- Name of the state
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE City (
    CityId INT PRIMARY
    Name VARCHAR(50) NOT NULL, -- Name of the city
    StateId INT NOT NULL, -- Foreign key referencing the state
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE IF NOT EXISTS Township (
    TownshipId INT PRIMARY KEY, -- Unique identifier for each township
    Name VARCHAR(50) NOT NULL, -- Name of the township
    CityId INT NOT NULL, -- Foreign key referencing the city
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE IF NOT EXISTS Neighborhood (
    NeighborhoodId INT PRIMARY KEY, -- Unique identifier for each neighborhood
    Name VARCHAR(50) NOT NULL, -- Name of the neighborhood
    TownshipId INT NOT NULL, -- Foreign key referencing the township
    PostalCode VARCHAR(10) NOT NULL, -- Postal/ZIP code
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE Address (
    AddressId INT PRIMARY KEY, -- Unique identifier for each address
    NeighborhoodId INT NOT NULL, -- Foreign key referencing the neighborhood
    Street VARCHAR(255) NOT NULL, -- Street address
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE PersonAddress (
    PersonId INT NOT NULL, -- Foreign key referencing the person
    AddressId INT NOT NULL, -- Foreign key referencing the address
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    PRIMARY KEY (PersonId, AddressId) -- Composite primary key
);

CREATE TABLE Emails (
    EmailId INT PRIMARY KEY, -- Unique identifier for each email
    EmailAddress VARCHAR(255) NOT NULL UNIQUE, -- Email address
);

CREATE TABLE PersonEmail (
    PersonId INT NOT NULL, -- Foreign key referencing the person
    EmailId INT NOT NULL, -- Foreign key referencing the email
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    PRIMARY KEY (PersonId, EmailId) -- Composite primary key
);

CREATE TABLE Phones (
    PhoneId INT PRIMARY KEY, -- Email address
    PhoneNumber VARCHAR(15) NOT NULL, -- Contact phone number
    PhoneType TINYINT, -- Type of phone (e.g., 'M' for Mobile, 'H' for Home, 'W' for Work)
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);

CREATE TABLE PersonPhone (
    PersonId INT NOT NULL, -- Foreign key referencing the person
    PhoneId INT NOT NULL, -- Foreign key referencing the phone
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    PRIMARY KEY (PersonId, PhoneId) -- Composite primary key
);



CREATE TABLE Person (
    PersonID INT PRIMARY KEY, -- Unique identifier for each person
    FirstName VARCHAR(50) NOT NULL, -- First name of the person
    SecondName VARCHAR(50) NOT NULL, -- First name of the person
    LastName VARCHAR(50) NOT NULL, -- Last name of the person
    SecondLastName VARCHAR(50) NOT NULL, -- Last name of the person
    BirthDate DATE, -- Date of birth
    Gender TINYINT, -- Gender (e.g., 'M' for Male, 'F' for Female)
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of creation
    Updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
);


