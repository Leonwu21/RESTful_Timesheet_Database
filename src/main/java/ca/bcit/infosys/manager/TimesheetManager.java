package ca.bcit.infosys.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named("timesheetManager")
@ConversationScoped
/**
 * Class to manage timesheets.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class TimesheetManager implements TimesheetCollection {
    
    private static final long serialVersionUID = 16L;
    
    /**
     * Datasource for timesheet system
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_system")
    private DataSource dataSource;
    
    /**
     * Provides access to the timesheet row manager
     */
    @Inject
    private TimesheetRowManager tsRowManager;
    
    /**
     * Provides access to the employees table in the datasource
     */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Returns list of timesheets
     */
    @Override
    public List<Timesheet> getTimesheets() {
        ArrayList<Timesheet> timesheets = new ArrayList<Timesheet>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Timesheets ORDER BY timesheetId");
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        int id = result.getInt("timesheetId");
                        List<TimesheetRow> rows = tsRowManager
                                .getTimesheetRows(id);
                        Employee employee = employeeManager.
                                getEmployeeByNumber(result.getInt("employeeNumber"));
                        Timesheet timesheet = new Timesheet(employee,
                                result.getDate("endDate").toLocalDate(), rows);
                        timesheet.setTimesheetId(id);
                        timesheets.add(timesheet);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return timesheets;
    }
    
    /**
     * Gets a list of timesheets for an employee of interest.
     * @param employee The employee of interest.
     * @return The list of timesheets for the employee of interest.
     */
    @Override
    public List<Timesheet> getTimesheetList(Employee employee) {
        int empNo = employee.getEmployeeNumber();
        ArrayList<Timesheet> timesheets = new ArrayList<Timesheet>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Timesheets WHERE employeeNumber = ? "
                            + "ORDER BY timesheetId");
                    stmt.setInt(1, empNo);
                    final ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        int id = result.getInt("timesheetId");
                        List<TimesheetRow> rows = tsRowManager.
                                getTimesheetRows(id);
                        Timesheet timesheet = new Timesheet(employee,
                                result.getDate("endDate").toLocalDate(), rows);
                        timesheet.setTimesheetId(id);
                        timesheets.add(timesheet);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return timesheets;
    }

    /**
     * Gets the current timesheet for an employee of interest.
     * @param employee The employee of interest.
     * @return The current timesheet for the employee of interest.
     */
    @Override
    public Timesheet getCurrentTimesheet(Employee employee) {
        List<Timesheet> employeeTimesheetsList = this.getTimesheetList(employee);
        
        long maxEndDate = 0;
        int currentTimesheetIndex = 0;
        
        for(int i = 0; i < employeeTimesheetsList.size() - 1; ++i) {
            Timesheet timesheet = employeeTimesheetsList.get(i);
            
            long endDate = timesheet.getEndDate().toEpochDay();
            if (endDate > maxEndDate) {
                endDate = maxEndDate;
                currentTimesheetIndex = i;
            }
        }
        return employeeTimesheetsList.get(currentTimesheetIndex);
    }
    
    /**
     * Creates a Timesheet object and adds it to the database.
     */
    @Override
    public void addTimesheet(Timesheet timesheet) {
        int employeeNumber = 1;
        int endDate = 2;
        int tsId = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Timesheets "
                            + "VALUES (?, ?, ?)");
                    stmt.setInt(employeeNumber,
                            timesheet.getEmployee().getEmployeeNumber());
                    stmt.setDate(endDate,
                            java.sql.Date.valueOf(timesheet.getEndDate()));
                    stmt.setInt(tsId, timesheet.getTimesheetId());
                    stmt.executeUpdate();
                    List<Timesheet> timesheetList = getTimesheets();
                    Timesheet ts = timesheetList.get(timesheetList.size()-1);
                    tsRowManager.addRow(ts.getTimesheetId(),
                            timesheet.getDetails());
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Finds the timesheet for an employee of interest for a specific week.
     * 
     * @param employee The employee of interest.
     * @param endOfWeek The end of the week.
     * @return If found, the timesheet for an employee of interest for a specific week. Otherwise, null.
     */
    public Timesheet findTimesheet(Employee employee, String endOfWeek) {
        List<Timesheet> timesheetList = this.getTimesheetList(employee);
        for (Timesheet timesheet : timesheetList) {
            String timesheetEndOfWeek = timesheet.getWeekEnding();
            if (timesheetEndOfWeek.equals(endOfWeek)) {
                return timesheet;
            }
        }
        return null;
    }
    
}
