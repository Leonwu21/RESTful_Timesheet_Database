package ca.bcit.infosys.editable;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * A class to edit employees.
 * 
 * @author Benedict Halim and Leon Wu.
 * @version 1.0
 */
public class EditableEmployee {
    
    /**
     * True if an employee is editable. Otherwise, false.
     */
    private Boolean isEditable = true;
    
    /**
     * The employee to be edited.
     */
    private Employee employee;
    
    /**
     * The credentials of the employee to be edited.
     */
    private Credentials credentials;
    
    /**
     * Constructor to create a new editable employee.
     * @param editable True if the employee is editable. False otherwise.
     */
    public EditableEmployee(boolean editable) {
        isEditable = editable;
        employee = new Employee();
        credentials = new Credentials();
    }
    
    /**
     * Constructor to edit an existing employee.
     * @param emp The employee to be edited.
     * @param editable True if the employee is editable. False otherwise.
     */
    public EditableEmployee(Employee emp, boolean editable) {
        isEditable = editable;
        employee = emp;
        credentials = new Credentials();
    }
    
    /**
     * Gets isEditable.
     * @return isEditable as a Boolean.
     */
    public Boolean getEditable() {
        return isEditable;
    }

    /**
     * Sets isEditable.
     * @param editable True if the employee is editable. False otherwise.
     */
    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    /**
     * Gets the employee to be edited.
     * @return The employee to be edited.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee to be edited.
     * @param emp The employee to be edited.
     */
    public void setEmployee(Employee emp) {
        employee = emp;
    }

    /**
     * Gets the credentials of the employee to be edited.
     * @return The credentials of the employee to be edited.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the credentials of the employee to be edited.
     * @param creds The credentials of the employee to be edited.
     */
    public void setCredentials(Credentials creds) {
        credentials = creds;
    }
    
    
}

