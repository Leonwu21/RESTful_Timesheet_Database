DROP DATABASE IF EXISTS timesheet_system;
CREATE DATABASE timesheet_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON timesheet_system.* TO 'admin'@'localhost';
GRANT ALL ON timesheet_system.* TO 'admin'@'%';

USE timesheet_system;

DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees(
    employeeNumber INT(10) NOT NULL UNIQUE,
    employeeName VARCHAR(50) NOT NULL,
    userName VARCHAR(20) NOT NULL UNIQUE,
	isAdmin BIT NOT NULL,
    CONSTRAINT PKEmployee PRIMARY KEY (employeeNumber)
);

INSERT INTO Employees VALUES (1, "Benedict Halim", "bhalim", 1);
INSERT INTO Employees VALUES (2, "Leon Wu", "lwu", 0);

DROP TABLE IF EXISTS Credentials;
CREATE TABLE Credentials(
    employeeNumber INT(10) NOT NULL UNIQUE,
    userName VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    token BINARY(32) NOT NULL,
    CONSTRAINT FKCredentialEmployeeNumber FOREIGN KEY (employeeNumber)
        REFERENCES Employees(employeeNumber)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Credentials VALUES (1, "bhalim", "benAdmin123", X'10c867430ef329eed6c4996c6d0d5bbe2d2bf6f21224f270ecbb6470c595f4b3');
INSERT INTO Credentials VALUES (2, "lwu", "leonUser123", X'0ac1c6cf435f5ef7bb5fc5d79ef606a44a163578f56b0fa0f336419f594b8eac');

DROP TABLE IF EXISTS Timesheets;
CREATE TABLE Timesheets(
    employeeNumber INT(10) NOT NULL,
    endDate DATE NOT NULL,
    timesheetId INT(10) NOT NULL UNIQUE AUTO_INCREMENT,
    CONSTRAINT PKTimesheet PRIMARY KEY (timesheetId),
    CONSTRAINT FKTimesheetEmployeeNumber FOREIGN KEY (employeeNumber)
        REFERENCES Employees (employeeNumber)
        ON UPDATE CASCADE 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRows;
CREATE TABLE TimesheetRows(
	timesheetId INT(10) NOT NULL,
    projectId INT(10) NOT NULL,
    workPackageId VARCHAR(10) NOT NULL,
    totalWeekHours VARCHAR(50) NOT NULL,
    notes VARCHAR(50) NOT NULL,
    CONSTRAINT PKTimesheetRows PRIMARY KEY (timesheetId, projectId, workPackageId),
    CONSTRAINT FKTimesheetRowsTimesheetId FOREIGN KEY (TimesheetID)
        REFERENCES Timesheets (TimesheetID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Timesheets VALUES (1, DATE'2021-11-12', 1);
INSERT INTO Timesheets VALUES (1, DATE'2021-11-19', 2);

INSERT INTO TimesheetRows VALUES (1, 21, "A87", "0,0,0.00,8,7,8,7", "");
INSERT INTO TimesheetRows VALUES (1, 21, "A202", "1,2,7.5,4,5,6,7", "Sequence");
INSERT INTO TimesheetRows VALUES (2, 56, "D777", "0,0,0,0,0,0,0", "None");
INSERT INTO TimesheetRows VALUES (2, 303, "D777", "10,9.08,8.0,7.1,6,5,4", "");
