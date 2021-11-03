package ca.bcit.infosys.timesheet;

import java.io.Serializable;
import java.util.List;

import ca.bcit.infosys.employee.Employee;

/**
 * Interface to access all existing Timesheets.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
public interface TimesheetCollection extends Serializable {
    
    /**
     * Gets all of the timesheets.
     * @return The list of all the timesheets.
     */
    List<Timesheet> getTimesheets();

    /**
     * Gets a list of timesheets for an employee of interest.
     * @param employee The employee of interest.
     * @return The list of timesheets for the employee of interest.
     */
    List<Timesheet> getTimesheets(Employee employee);

    /**
     * Gets the current timesheet for an employee of interest.
     * @param employee The employee of interest.
     * @return The current timesheet for the employee of interest.
     */
    Timesheet getCurrentTimesheet(Employee employee);

    /**
     * Adds timesheet to the database.
     * 
     * @param timesheet The timesheet to be added.
     */
    void addTimesheet(Timesheet timesheet);
}
