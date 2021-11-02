package ca.bcit.infosys.manager;

import java.io.Serializable;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * Interface to access all existing Timesheets.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
public interface TimesheetCollection extends Serializable {
    /**
     * Gets the list of timesheets.
     * @return The list of timesheets.
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
     * Creates a Timesheet object and adds it to the database.
     * @return The path to a new timesheet page.
     */
    String addTimesheet();
}
