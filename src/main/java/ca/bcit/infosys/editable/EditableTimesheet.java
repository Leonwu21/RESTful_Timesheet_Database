package ca.bcit.infosys.editable;

import java.io.Serializable;

import ca.bcit.infosys.timesheet.Timesheet;

/**
 * A class to edit timesheets.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class EditableTimesheet implements Serializable {
	
    private static final long serialVersionUID = 10L;
    /**
     * True if the timesheet is editable. Otherwise, false.
     */
    private Boolean isEditable = true;
    
    /**
     * The timesheet to be edited.
     */
    private Timesheet timesheet;
    
    /**
     * Constructor to create a new editable timesheet.
     * @param editable True if the employee is editable. False otherwise.
     */
    public EditableTimesheet(Boolean editable) {
        isEditable = editable;
        timesheet = new Timesheet();
    }
    
    /**
     * Constructor to edit an existing timesheet.
     * @param ts The timesheet to be edited.
     * @param editable True if the timesheet is editable. False otherwise.
     */
    public EditableTimesheet(Timesheet ts, Boolean editable) {
        timesheet = ts;
        isEditable = editable;
    }
    
    /**
     * Gets isEditable.
     * @return True if the timesheet is editable. False otherwise.
     */
    public Boolean getEditable() {
        return isEditable;
    }

    /**
     * Sets isEditable.
     * @param editable True if the timesheet is editable. False otherwise.
     */
    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    /**
     * Gets the timesheet to be edited.
     * @return The timesheet to be edited.
     */
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Sets the timesheet to be edited.
     * @param ts The timesheet to be edited.
     */
    public void setTimesheet(Timesheet ts) {
        timesheet = ts;
    }
    
}


