package ca.bcit.infosys.manager;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.database.TimesheetDatabase;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

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
     * Injected TimesheetDatabase.
     */
    @Inject private TimesheetDatabase timesheetDatabase;

    /**
     * Getting the Timesheets.
     */
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheetDatabase.getTimesheetList();
    }
    
    /**
     * Gets a list of timesheets for an employee of interest.
     * @param employee The employee of interest.
     * @return The list of timesheets for the employee of interest.
     */
    @Override
    public List<Timesheet> getTimesheets(Employee employee) {
        List<Timesheet> timesheetList = timesheetDatabase.getTimesheetList();
        List<Timesheet> newTimesheetList = new ArrayList<>();
        
        for (Timesheet timesheet : timesheetList) {
            int timesheetEmployeeNumber = timesheet.getEmployee().getEmpNumber();
            int employeeNumber = employee.getEmpNumber();
            
            if (timesheetEmployeeNumber == employeeNumber) {
                newTimesheetList.add(timesheet);
            }
        }
        return newTimesheetList;
    }

    /**
     * Gets the current timesheet for an employee of interest.
     * @param employee The employee of interest.
     * @return The current timesheet for the employee of interest.
     */
    @Override
    public Timesheet getCurrentTimesheet(Employee employee) {
        List<Timesheet> employeeTimesheetsList = this.getTimesheets(employee);
        
        long maxEndDate = 0;
        int currentTimesheetIndex = 0;
        
        for(int i = 0; i < employeeTimesheetsList.size() - 1; ++i) {
            Timesheet timesheet = employeeTimesheetsList.get(i);
            
            long endDate = timesheet.getEndWeek().toEpochDay();
            if (endDate > maxEndDate) {
                endDate = maxEndDate;
                currentTimesheetIndex = i;
            }
        }
        return employeeTimesheetsList.get(currentTimesheetIndex);
    }
    
    /**
     * Creates a Timesheet object and adds it to the database.
     * @return The path to a new timesheet page.
     */
    @Override
    public void addTimesheet(Timesheet timesheet) {
        List<Timesheet> timesheetList = timesheetDatabase.getTimesheetList();
        timesheetList.add(timesheet);
    }
    
    /**
     * Finds the timesheet for an employee of interest for a specific week.
     * 
     * @param employee The employee of interest.
     * @param endOfWeek The end of the week.
     * @return If found, the timesheet for an employee of interest for a specific week. Otherwise, null.
     */
    public Timesheet findTimesheet(Employee employee, String endOfWeek) {
        List<Timesheet> timesheetList = this.getTimesheets(employee);
        for (Timesheet timesheet : timesheetList) {
            String timesheetEndOfWeek = timesheet.getWeekEnding();
            if (timesheetEndOfWeek.equals(endOfWeek)) {
                return timesheet;
            }
        }
        return null;
    }
    
}
