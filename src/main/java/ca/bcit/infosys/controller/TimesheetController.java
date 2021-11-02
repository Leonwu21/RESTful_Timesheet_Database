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
     * Provides access to edit a timesheet.
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
     * Gets the EditableTimesheet.
     *
     * @return The EditableTimesheet.
     */
    public EditableTimesheet getEditableTimesheet() {
        return editableTimesheet;
    }
    
    /**
     * Sets the EditableTimesheet.
     *
     * @param editTimesheet The editable timesheet.
     */
    public void setEditableTimesheet(EditableTimesheet editTimesheet) {
        editableTimesheet = editTimesheet;
    }

    /**
     * Gets the list of timesheets.
     *
     * @return The list of timesheets.
     */
    public List<Timesheet> getTimesheetList() {
        return timesheetList;
    }

    /**
     * Prepares to create a new editable timesheet and starts the conversation.
     *
     * @return The path to create a timesheet page.
     */
    public String prepareCreate() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        editableTimesheet = new EditableTimesheet(true);
        return "/timesheet/create";
    }

    /**
     * Prepares to edit a new editable timesheet and starts the conversation.
     *
     * @param timesheet The timesheet to be edited.
     * @return The path to edit a timesheet page.
     */
    public String prepareEdit(Timesheet timesheet) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        int empNumber = timesheet.getEmployee().getEmpNumber();
        int currEmpNumber = employeeManager.getCurrentEmployee().getEmpNumber();
        if (empNumber != currEmpNumber) {
            if (employeeManager.isAdminLogin()) {
                editableTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editableTimesheet = new EditableTimesheet(timesheet, true);
        return "/timesheet/edit";
    }

    /**
     * Prepares to view the timesheet of an employee and starts the conversation.
     *
     * @param timesheet The timesheet to be viewed.
     * @return The path to view a timesheet page.
     */
    public String prepareView(Timesheet timesheet) {
        conversation.end();
        //NULL POINTER ON LINE 138, NOT GETTING EMPLOYEE NUMBER
        Employee employee = timesheet.getEmployee();
        int empNumber = employee.getEmpNumber();
        int currEmpNumber = employeeManager.getCurrentEmployee().getEmpNumber();
        if (empNumber != currEmpNumber) {
            if (employeeManager.isAdminLogin()) {
                editableTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editableTimesheet = new EditableTimesheet(timesheet, false);
        return "/timesheet/view";
    }

    /**
     * Prepares a list of timesheets.
     *
     * @return The path to the list of timesheets page.
     */
    public String prepareList() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (employeeManager.isAdminLogin()) {
            timesheetList = timesheetManager.getTimesheets();
        } else {
            Employee currentEmployee = employeeManager.getCurrentEmployee();
            timesheetList = timesheetManager.getTimesheets(currentEmployee);
        }
        return "/timesheet/list";
    }

    /**
     * Adds a new row to the timesheet.
     *
     * @return Null.
     */
    public String onAddRow() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        int sizeOfTimesheet = editableTimesheet.getTimesheet().getDetails().size();
        if (sizeOfTimesheet != 7) {
            editableTimesheet.getTimesheet().addRow();
        }
        return null;
    }

    /**
     * Creates a timesheet.
     *
     * @return The list of timesheets.
     */
    public String onCreate() {
        Employee currentEmployee = employeeManager.getCurrentEmployee();
        editableTimesheet.getTimesheet().setEmployee(currentEmployee);

        for (final Timesheet timesheet : timesheetManager.getTimesheets(currentEmployee)) {
            LocalDate timesheetEndDate = timesheet.getEndWeek();
            LocalDate editableTimesheetEndDate = editableTimesheet.getTimesheet().getEndWeek();
            if (timesheetEndDate.equals(editableTimesheetEndDate)) {
                final FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage("Error: A timesheet with same end date already exists."));
                return null;
            }
        }

        timesheetManager.addTimesheet();
        editableTimesheet = null;
        conversation.end();
        return prepareList();
    }

    /**
     * Edits the timesheets.
     *
     * @return The list of timesheets.
     */
    public String onEdit() {
        editableTimesheet = null;
        conversation.end();
        return prepareList();
    }

}