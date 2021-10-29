package ca.bcit.infosys.controller;

import java.io.Serializable;
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
    //TODO: Refactor to employeeManager.
    @Inject
    private EmployeeManager empManager;

    /**
     * Injected Conversation to control the context.
     */
    @Inject
    private Conversation conversation;

    /**
     * Provides access to edit a timesheet.
     */
    //TODO: Refactor to editableTimesheet.
    //TODO: Needs a setter.
    private EditableTimesheet editTimesheet;

    /**
     * A list of timesheets.
     */
    //TODO: Needs a setter.
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
    public EditableTimesheet getEditTimesheet() {
        return editTimesheet;
    }

    /**
     * Gets the list of timesheets.
     *
     * @return The list of timesheets.
     */
    public List<Timesheet> getTimesheets() {
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
        editTimesheet = new EditableTimesheet(true);
        return "/timesheet/create";
    }

    /**
     * Prepares to edit a new editable timesheet and starts the conversation.
     *
     * @param timesheet The timesheet to be edited.
     * @return The path to edit a timesheet page.
     */
    //TODO: Refactor if-else statement.
    public String prepareEdit(Timesheet timesheet) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (timesheet.getEmployee().getEmpNumber() != empManager.getCurrentEmployee().getEmpNumber()) {
            if (empManager.isAdminLogin()) {
                editTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editTimesheet = new EditableTimesheet(timesheet, true);
        return "/timesheet/edit";
    }

    /**
     * Prepares to view the timesheet of an employee and starts the conversation.
     *
     * @param timesheet The timesheet to be viewed.
     * @return The path to view a timesheet page.
     */
    //TODO: Refactor if-else statement.
    //TODO: Refactor method chaining.
    public String prepareView(Timesheet timesheet) {
        conversation.end();
        if (timesheet.getEmployee().getEmpNumber() != empManager.getCurrentEmployee().getEmpNumber()) {
            if (empManager.isAdminLogin()) {
                editTimesheet = new EditableTimesheet(timesheet, true);
            } else {
                return null;
            }
        }
        editTimesheet = new EditableTimesheet(timesheet, false);
        return "/timesheet/view";
    }

    /**
     * Prepares a list of timesheets.
     *
     * @return The path to the list of timesheets page.
     */
    //TODO: Refactor if-else statement.
    //TODO: Refactor method chaining.
    //TODO: Why use timesheetManager.getTimesheets() instead of timesheetDatabase.getAllTimesheets()?
    public String prepareList() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (empManager.isAdminLogin()) {
            timesheetList = timesheetManager.getTimesheets();
        } else {
            timesheetList = timesheetManager.getTimesheets(empManager.getCurrentEmployee());
        }
        return "/timesheet/list";
    }

    /**
     * Adds a new row to the timesheet.
     *
     * @return Null.
     */
    //TODO: Refactor == to !=
    //TODO: Refactor method chaining.
    public String onAddRow() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (!(editTimesheet.getTimesheet().getDetails().size() == 7)) {
            editTimesheet.getTimesheet().addRow();
        }
        return null;
    }

    /**
     * Creates a timesheet.
     *
     * @return The list of timesheets.
     */
    //TODO: Refactor method chaining.
    public String onCreate() {
        editTimesheet.getTimesheet().setEmployee(empManager.getCurrentEmployee());

        for (final Timesheet timesheet : timesheetManager.getTimesheets(empManager.getCurrentEmployee())) {
            if (timesheet.getEndDate().equals(editTimesheet.getTimesheet().getEndDate())) {
                final FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage("A timesheet with same end week already exists. Please try again"));
                return null;
            }
        }

        timesheetManager.addTimesheet();
        editTimesheet = null;
        conversation.end();
        return prepareList();
    }

    /**
     * Edits the timesheets.
     *
     * @return The list of timesheets.
     */
    //TODO: Understand what this is doing.
    public String onEdit() {
        editTimesheet = null;
        conversation.end();
        return prepareList();
    }

}