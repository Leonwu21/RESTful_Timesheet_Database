package ca.bcit.infosys.database;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.timesheet.Timesheet;

/**
 * The class for the database of timesheets.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
@Named("timesheetDatabase")
@ApplicationScoped
public class TimesheetDatabase {
    
    /**
     * A list of all the timesheets.
     */
    private final List<Timesheet> timesheetList;
    
    /**
     * Constructor for TimesheetDatabase.
     */
    public TimesheetDatabase() {
        timesheetList = new ArrayList<>();
    }
    
    /**
     * Gets the list of timesheets.
     * 
     * @return The list of timesheets.
     */
    public List<Timesheet> getTimesheetList() {
        return timesheetList;
    }
}