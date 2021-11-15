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
    CONSTRAINT FKCredentialEmployeeNumber
        FOREIGN KEY (employeeNumber)
            REFERENCES Employees(employeeNumber)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

INSERT INTO Credentials VALUES (1, "bhalim", "benAdmin123");
INSERT INTO Credentials VALUES (2, "lwu", "leonUser123");

-- unsure if below is correct, we don't have a timesheetID

/*
DROP TABLE IF EXISTS Timesheets;
CREATE TABLE Timesheets(
    employeeNumber INT(10) NOT NULL,
    endDate DATE NOT NULL,
    CONSTRAINT PKTimesheet PRIMARY KEY (employeeNumber, endDate),
    CONSTRAINT FKTimesheetEmployeeNumber
        FOREIGN KEY (employeeNumber)
            REFERENCES Employees (employeeNumber)
            ON UPDATE CASCADE 
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRows;
CREATE TABLE TimesheetRows(
    TimesheetID INT(5) NOT NULL, <-- we don't have this. should we create in Timesheet.java?
    ProjectID INT(5) NOT NULL,
    WorkPackage VARCHAR(10) NOT NULL,
    Notes VARCHAR(50) NOT NULL,
    HoursForWeek VARCHAR(50) NOT NULL,
    CONSTRAINT PKTimesheetRows PRIMARY KEY (TimesheetID, ProjectID, WorkPackage),
    CONSTRAINT FKTimesheetRowsTimesheetID
        FOREIGN KEY (TimesheetID)
            REFERENCES Timesheets (TimesheetID)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

INSERT INTO Timesheets VALUES (1, DATE'2020-11-13', 1);
INSERT INTO Timesheets VALUES (2, DATE'2020-11-20', 2);
INSERT INTO Timesheets VALUES (3, DATE'2020-11-27', 3);

INSERT INTO TimesheetRows VALUES (1, 142, "142", "Redo project", "1,0,5,7,0,0,0");
INSERT INTO TimesheetRows VALUES (1, 142, "143", "Excellence", "5,5,5,0,0,0,8");
INSERT INTO TimesheetRows VALUES (2, 100, "150", "Edit some parts", "8,8,8,0,0,0,4");
*/
