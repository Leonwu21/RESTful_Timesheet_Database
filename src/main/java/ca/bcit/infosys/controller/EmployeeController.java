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
     * If an admin user is logged in, direct the user to the list of employees page. Otherwise, do nothing.
     *
     * @return The path to the list of employees page.
     */
    public String prepareList() {
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        return "/employee/list";
    }

    /**
     * If an admin user is logged in, create the list of employees page. Otherwise, do nothing.
     *
     * @return The path to create the list of employees page.
     */
    public String prepareCreate() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (!employeeManager.isAdminLogin()) {
            return null;
        }
        editableEmployee = new EditableEmployee(true);
        return "/employee/create";
    }

    /**
     * If an admin user is logged in, direct the user to the edit employees page. Otherwise, do nothing.
     *
     * @return The path to the edit employees page.
     */
    public String prepareEdit(String username) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return null;
        }
        editableEmployee = new EditableEmployee(employee, true);
        return "/employee/edit";
    }

    /**
     * If an admin user is logged in, direct the user to the view employees page. Otherwise, do nothing.
     *
     * @return The path to the view employees page.
     */
    public String prepareView(String username) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return null;
        }
        editableEmployee = new EditableEmployee(employee, false);
        return "/employee/view";
    }

    public String prepareDelete(String username) {
        final Employee employee = employeeManager.getEmployeeByUserName(username);
        if (employee == null) {
            return null;
        }
        employeeManager.deleteEmployee(employee);
        return null;
    }

    /**
     * If the employee is unique, store in the list of employees. Otherwise, do nothing.
     *
     * @return The path to the list of employees page.
     */
    public String onCreate() {
        try {
            employeeManager.addEmployee(editableEmployee.getEmployee());
        } catch (final IllegalArgumentException ex) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(ex.getLocalizedMessage()));
            return null;
        }

        Credentials credentials = editableEmployee.getCredentials();
        int employeeNumber = editableEmployee.getEmployee().getEmpNumber();
        String employeeId = editableEmployee.getEmployee().getUserName();
        
        credentials.setEmpNumber(employeeNumber);
        credentials.setUserName(employeeId);

        credentialsManager.add(credentials);
        editableEmployee = null;
        conversation.end();
        return "/employee/list";
    }

    /**
     * Edits an employee.
     *
     * @return The path to the list of employees page.
     */
    public String onEdit() {
        Credentials credentials = editableEmployee.getCredentials();
        int employeeNumber = editableEmployee.getEmployee().getEmpNumber();
        String employeeId = editableEmployee.getEmployee().getUserName();
        
        credentials.setEmpNumber(employeeNumber);
        credentials.setUserName(employeeId);
        editableEmployee = null;
        conversation.end();
        return "/employee/list";
    }

    /**
     * Gets the editable employee.
     *
     * @return The editable employee.
     */
    public EditableEmployee getEditEmployee() {
        return editableEmployee;
    }

    /**
     * Sets the editable employee.
     *
     * @param editEmployee The editable employee.
     */
    public void setEditEmployee(EditableEmployee editEmployee) {
        editableEmployee = editEmployee;
    }

}

