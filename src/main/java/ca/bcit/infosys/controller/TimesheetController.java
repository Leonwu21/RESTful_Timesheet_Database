package ca.bcit.infosys.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.manager.EmployeeManager;
import ca.bcit.infosys.manager.TimesheetManager;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.editable.EditableTimesheet;
import ca.bcit.infosys.employee.Employee;

/**
 * A class to control the timesheets.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("timesheetController")
@ConversationScoped
public class TimesheetController implements Serializable {
    
    private static final long serialVersionUID = 4L;

    /**
     * Injected TimesheetManager. Provides access to timesheets.
     */
    @Inject
    private TimesheetManager timesheetManager;

    /**
     * Injected EmployeeManager. Provides access to employees.
     */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Injected Conversation to control the context.
     */
    @Inject
    private Conversation conversation;

    /**
     * The editable timesheet.
     */
    private EditableTimesheet editableTimesheet;

    /**
     * A list of timesheets.
     */
    private List<Timesheet> timesheetList;

    /**
     * Constructor for TimesheetController.
     */
    public TimesheetController() {
    }

    /**
     * Creates a new editable timesheet and directs user to the timesheet create page.
     * @return The path to the timesheet create page.
     * 
     */
    public String goToTimesheetCreatePage() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        editableTimesheet = new EditableTimesheet(true);
        return "timesheetCreatePage";
    }

    /**
     * Directs user to the timesheet edit page.
     * @param timesheet The timesheet to be edited.
     * @return The path to the timesheet edit page.
     */
    public String goToTimesheetEditPage(Timesheet timesheet) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        
        int empNumber = timesheet.getEmployee().getEmployeeNumber();
        int currEmpNumber = employeeManager.getCurrentEmployee().getEmployeeNumber();
        
        if (empNumber != currEmpNumber) {
            if (employeeManager.isAdminLogin()) {
                editableTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editableTimesheet = new EditableTimesheet(timesheet, true);
        return "timesheetEditPage";
    }

    /**
     * Directs user to timesheet view page.
     * @param timesheet The timesheet to be edited.
     * @return The path to the timesheet edit page.
     */
    public String goToTimesheetViewPage(Timesheet timesheet) {
        conversation.end();
        
        Employee employee = timesheet.getEmployee();
        int empNumber = employee.getEmployeeNumber();
        Employee currEmployee = employeeManager.getCurrentEmployee();
        int currEmpNumber = currEmployee.getEmployeeNumber();
        
        if (empNumber != currEmpNumber) {
            if (employeeManager.isAdminLogin()) {
                editableTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editableTimesheet = new EditableTimesheet(timesheet, false);
        return "timesheetViewPage";
    }

    /**
     * Directs user to timesheet list page.
     * @return The path to the timesheet list page.
     */
    public String goToTimesheetListPage() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (employeeManager.isAdminLogin()) {
            timesheetList = timesheetManager.getTimesheets();
        } else {
            Employee currentEmployee = employeeManager.getCurrentEmployee();
            timesheetList = timesheetManager.getTimesheetList(currentEmployee);
        }
        return "timesheetListPage";
    }

    /**
     * Adds a new row to the timesheet.
     */
    public void onAddRow() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        int sizeOfTimesheet = editableTimesheet.getTimesheet().getDetails().size();
        if (sizeOfTimesheet != 7) {
            editableTimesheet.getTimesheet().addRow();
        }
    }

    /**
     * Creates a timesheet.
     * @return The path to the timesheet list page.
     */
    public String onTimesheetCreate() {
        Employee currentEmployee = employeeManager.getCurrentEmployee();
        Timesheet editTimesheet = editableTimesheet.getTimesheet();
        List<Timesheet> currEmpTimesheetList = timesheetManager.getTimesheetList(currentEmployee);
        
        editTimesheet.setEmployee(currentEmployee);

        for (final Timesheet timesheet : currEmpTimesheetList) {
            LocalDate timesheetEndDate = timesheet.getEndDate();
            LocalDate editableTimesheetEndDate = editTimesheet.getEndDate();
            if (timesheetEndDate.equals(editableTimesheetEndDate)) {
                final FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage("Error: A timesheet with same end date already exists."));
                return null;
            }
        }

        timesheetManager.addTimesheet(editTimesheet);
        editableTimesheet = null;
        conversation.end();
        return goToTimesheetListPage();
    }

    /**
     * Edits the timesheet.
     * @return The path to the timesheet list page.
     */
    public String onTimesheetEdit() {
        editableTimesheet = null;
        conversation.end();
        return goToTimesheetListPage();
    }
    
    /**
     * Gets the EditableTimesheet.
     * @return The EditableTimesheet.
     */
    public EditableTimesheet getEditableTimesheet() {
        return editableTimesheet;
    }
    
    /**
     * Sets the EditableTimesheet.
     * @param editTimesheet The editable timesheet.
     */
    public void setEditableTimesheet(EditableTimesheet editTimesheet) {
        editableTimesheet = editTimesheet;
    }

    /**
     * Gets the list of timesheets.
     * @return The list of timesheets.
     */
    public List<Timesheet> getTimesheetList() {
        return timesheetList;
    }
    
    /**
     * Sets the list of timesheets.
     * @param tsList The list of timesheets.
     */
    public void setTimesheetList(List<Timesheet> tsList) {
        timesheetList = tsList;
    }
}