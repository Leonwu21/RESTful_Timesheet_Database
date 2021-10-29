package ca.bcit.infosys.database;

import java.io.Serializable;
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
public class TimesheetDatabase implements Serializable {
	
    private static final long serialVersionUID = 8L;
	
    /**
     * A cell of the timesheets table.
     */
    private static final String CELL = "timesheet-table-cell,";
    
    /**
     * A collection of 10 cells.
     */
    private static final String CELLS = CELL.repeat(10) + CELL;
    
    /**
     * A list of all the timesheets.
     */
    private List<Timesheet> timesheetList;
    
    /**
     * Constructor for TimesheetDatabase.
     */
    public TimesheetDatabase() {
        timesheetList = new ArrayList<>();
    }
    
    /**
     * Gets the cells in a timesheet.
     * 
     * @return The collection of 10 cells.
     */
    public String getCellClasses() {
        return CELLS;
    }
    
    /**
     * Gets the list of timesheets.
     * 
     * @return The list of timesheets.
     */
    public List<Timesheet> getAllTimesheets() {
        return timesheetList;
    }
}
