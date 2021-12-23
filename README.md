# RESTful Timesheet Database
This is an application which allows a user to either use the UI to directly edit timesheets/profile information, or perform similar CRUD operations via URI endpoints.

## Functionality:

1. Employees and admins can log in to the application with a username & password combination.
2. Employees and admins can change their own username and password once logged into the application.
3. Employees and admins can create, read, update/edit, and view timesheets once logged into the application.
4. Admins can create new employees once logged into the application.
5. Admins can edit all employees’ usernames, passwords, and names once logged into the application.
6. Admins can delete employees once logged into the application.
7. All data related to credentials, employees, admins, and timesheets are stored in a MySQL database.
8. All the application’s input fields will be validated, with validation and error messages.
9. Application will have three layers:
- a. Middleware layer (RESTful interface).
- b. Business Objects layer (CDI Beans that implement CRUD operations on the database, data beans).
- c. Persistence layer (The database itself – provided by MySQL).
10. All messages and labels will come from the message bundle.
11. The application is thoroughly tested through either manual or unit testing.

## Instructions for building and running the project locally:

Pre-requisites:
  - MySQL
  - Eclipse with Wildfly 24.0 Runtime Server and JBoss tools
  - Postman
  
Stage 1 – Configure MySQL

1. Ensure that MySQL is pre-installed on the device
2. Download the project files and unzip the project
3. Open the command prompt and CD into the directory with timesheet_system_asn3.sql file (Located in <Repository-
    Location>\RESTful_Timesheet_Database\src\main\resources)
4. On the command prompt, run mysql -u root -p and enter your root password to connect to the local MySQL
5. Once connected to the local MySQL, run source timesheet_system_asn3.sql
6. Confirm that the database has been created by running SHOW DATABASES;
7. Confirm that the tables have been created by running USE timesheet_system_asn3; and SHOW TABLES;
8. Run exit and run mysql timesheet_system_asn3 -u admin -p with admin as the password to login again

Stage 2 – Launch Project

1. Open Eclipse then go to “File” -> “Import” -> “Existing Maven Project”
2. Click “Browse”, navigate to the unzipped project, and click “Finish”
3. Right-click the imported project on Eclipse’s Project Explorer tab then select “Run As” - > “Run on Server”
4. Choose “WildFly 24.0 Runtime Server” and click “Finish”

Stage 3 – Deploy Database as Data Source

1. Log in to your WildFly administrator console through your preferred browser. The default WildFly administrator console is at http://localhost:9990.
2. Click on “Configuration” - > “Subsystems” - > “Datasources & Drivers” - > “Datasource” - > “Add Datasource (+ button)”
3. On the "Add Datasource” pop-up window:
- “Choose Template” step:
    - Select MySQL
- “Attributes” step:
    - Name: timesheet_system_asn
    - JNDI name: java:jboss/datasources/timesheet_system_asn
- “JDBC Driver” step:
    - Driver name: “mysql-connector-java-8.0.12.jar”
    - Leave the “Driver Module Name” and “Driver Class Name” fields as their default
- “Connection” step:
    - Connection URL: jdbc:mysql://localhost:3306/timesheet_system_asn
    - User Name: admin
    - Password: admin
    - Leave the “Security Domain” field blank
- “Test Connection” step:
    - Click on “Test Connection” button – it should be successful.
- “Review” step:
    - Ensure information entered is correct and click on the “Finish” button.
4. If you do not see the “timesheet_system_asn3” datasource in the Datasource section, restart the WildFly server, log back
into your WildFly administrator console, and go to “Configuration” -> “Subsystems” -> “Datasources & Drivers” ->
“Datasource”. The “timesheet_system_asn3” datasource should now be displayed.

Stage 4 – Access the Project

1. Go to your preferred browser and type in http://localhost:8080/comp3910-assignment-3.
2. You will have to authenticate using either the bhalim (admin) user or the lwu (non-admin) user.
- Open Postman and create a new POST request with the URI: http://localhost:8080/comp3910-assignment-3/api/authentication
- Under “Body”, select “Raw” and “JSON” from the dropdown menu
- Inside the body, paste in the following JSON for the admin user:
```
    {
    "userName": "bhalim",
    "password": "benAdmin123"
    }
```

Or alternatively, for a non-admin user paste in this JSON snippet:
```
    {
    "userName": "lwu",
    "password": "leonUser123"
    }
```
- Click on “Send” and copy down the token(s).

3. You will use these tokens for all API calls by selecting the “Authorization” tab in Postman > Type “Bearer Token” > Paste in token.

## API Definition:

| Service        | HTTP Action | URI Endpoint                  | Description / Example format of body                                                                                                                       |
|----------------|-------------|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Authentication | POST        | /authentication               | Used for authentication of admin user.<br/><br/>{ "userName": "bhalim", "password": "benAdmin123" }                                                                |
| Authentication | POST        | /authentication               | Used for authentication of non-admin user.<br/><br/>{ "userName": "lwu", "password": "leonUser123" }                                                               |
| Credentials    | GET         | /credentials/<userName>       | Used to get the Credentials of a specific employee.                                                                                                        |
| Credentials    | PATCH       | /credentials/<employeeNumber> | Used to update the Credentials of a specific employee.<br/><br/>{ "employeeNumber": "2", "userName": "test1", "password": "test" }                                 |
| Credentials    | POST        | /credentials                  | Used to add Credentials.<br/><br/>{ "employeeNumber": "3", "userName": "test2", "password": "test" }                                                               |
| Employees      | GET         | /employees                    | Used to get a list of all employees.                                                                                                                       |
| Employees      | GET         | /employees/<employeeNumber>   | Used to get a specific employee.                                                                                                                           |
| Employees      | PATCH       | /employees/<employeeNumber>   | Employees PATCH /employees/ Updates a specific employee.<br/><br/>{ "employeeNumber": "2", "userName": "test3", "password": "test" }                               |
| Employees      | POST        | /employees                    | Creates an employee.<br/><br/>{ "employeeNumber": "4", "userName": "test3", "password": "test" }                                                                   |
| Timesheets     | GET         | /timesheets                   | Used to get a list of all timesheets                                                                                                                       |
| Timesheets     | PATCH       | /timesheets/<timesheetId>     | Updates a specific timesheet.<br/><br/>{ "employeeNumber": "1", "endDate": "2021-11-05", "timesheetId": "5" }                                                      |
| Timesheets     | POST        | /timesheets                   | Creates a specific timesheet.<br/><br/>{ "employeeNumber": "1", "endDate": "2021- 11 - 05", "timesheetId": "5" }                                                   |
| TimesheetRows  | GET         | /rows/<timesheetId>           | Gets all timesheetRows with specific timesheetId.                                                                                                          |
| TimesheetRows  | PATCH       | /rows/<timesheetId>           | Updates timesheetRow with specific timesheetId.<br/><br/>{ "projectId": "1", "workPackageId": "1", "totalWeekHours": [ 0 , 0 , 0 , 0 , 0 , 0 , 0 ], "notes": "1" } |
| TimesheetRows  | POST        | /rows                         | Adds timesheetRow with specific timesheetId.<br/><br/>{ "projectId": "1", "workPackageId": "1", "totalWeekHours": [ 0 , 0 , 0 , 0 , 0 , 0 , 0 ], "notes": "1" }    |
