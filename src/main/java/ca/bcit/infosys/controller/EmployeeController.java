package ca.bcit.infosys.controller;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.manager.CredentialsManager;
import ca.bcit.infosys.manager.EmployeeManager;
import ca.bcit.infosys.editable.EditableEmployee;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * A class to control the employees.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@ConversationScoped
@Named("employeeController")
public class EmployeeController implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Injected EmployeeManager. Provides access to employees.
     */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Injected CredentialsManager. Provides access to credentials.
     */
    @Inject
    private CredentialsManager credentialsManager;

    /**
     * Injected Conversation to control the context.
     */
    @Inject
    private Conversation conversation;

    /**
     * Provides access to edit an employee.
     */
    private EditableEmployee editableEmployee;

    /**
     * Directs the user to the employee list page.
     * @return The path to the employee list page.
     */
    public String goToEmployeeListPage() {
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        return "employeeListPage";
    }

    /**
     * Directs the user to the employee create page.
     * @return The path to the employee create page.
     */
    public String goToEmployeeCreatePage() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        editableEmployee = new EditableEmployee(true);
        return "employeeCreatePage";
    }
    
    /**
     * Direct the user to the employee edit page.
     * @return The path to the employee edit page.
     */
    public String goToEmployeeEditPage(String username) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return null;
        }
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        editableEmployee = new EditableEmployee(employee, true);
        return "employeeEditPage";
    }

    /**
     * Direct the user to the employee view page.
     * @return The path to the employee view page.
     */
    public String goToEmployeeViewPage(String username) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return null;
        }
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        editableEmployee = new EditableEmployee(employee, false);
        return "employeeViewPage";
    }

    /**
     * Deletes an employee.
     * @param username The employee's login ID.
     */
    public void deleteEmployee(String username) {
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return;
        }
        if (!employeeManager.isAdminLogin()) {
            return;
        }
        employeeManager.deleteEmployee(employee);
    }

    /**
     * If the employee is unique, store in the list of employees. Otherwise, do nothing.
     * @return The path to the list of employees page.
     */
    public String onEmployeeCreate() {
        try {
            employeeManager.addEmployee(editableEmployee.getEmployee());
        } catch (final IllegalArgumentException ex) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(ex.getLocalizedMessage()));
            return null;
        }

        Credentials credentials = editableEmployee.getCredentials();
        int employeeNumber = editableEmployee.getEmployee().getEmployeeNumber();
        String employeeId = editableEmployee.getEmployee().getUserName();
        
        credentials.setEmployeeNumber(employeeNumber);
        credentials.setUserName(employeeId);

        credentialsManager.addCredentials(credentials);
        editableEmployee = null;
        conversation.end();
        return "employeeListPage";
    }

    /**
     * Edits an employee.
     * @return The path to the list of employees page.
     */
    public String onEmployeeEdit() {
        Credentials credentials = editableEmployee.getCredentials();
        int employeeNumber = editableEmployee.getEmployee().getEmployeeNumber();
        String employeeId = editableEmployee.getEmployee().getUserName();
        
        credentials.setEmployeeNumber(employeeNumber);
        credentials.setUserName(employeeId);
        
        editableEmployee = null;
        conversation.end();
        return "employeeListPage";
    }

    /**
     * Gets the editable employee.
     * @return The editable employee.
     */
    public EditableEmployee getEditableEmployee() {
        return editableEmployee;
    }

    /**
     * Sets the editable employee.
     * @param editEmployee The editable employee.
     */
    public void setEditableEmployee(EditableEmployee editEmployee) {
        editableEmployee = editEmployee;
    }

}

