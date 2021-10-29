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
 * A class to control the profiles of the users.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("profileController")
@ConversationScoped
//TODO: Do we need to implement Serializable?
public class ProfileController implements Serializable {

    /**
     * Injected Conversation to control the context.
     */
    @Inject
    private Conversation conversation;

    /**
     * Injected EmployeeManager. Provides access to employees.
     */
    //TODO: Refactor to employeeManager.
    @Inject
    private EmployeeManager empManager;

    /**
     * Injected CredentialsManager. Provides access to credentials.
     */
    @Inject
    private CredentialsManager credentialsManager;

    /**
     * Provides access to edit an employee.
     */
    //TODO: Refactor to editableEmployee.
    private EditableEmployee editEmployee;

    /**
     * The current user's credentials.
     */
    //TODO: Need getter and setter.
    private Credentials credentials;

    /**
     * The current user's old password.
     */
    private String oldPassword;

    /**
     * The current user's new password.
     */
    private String newPassword;

    /**
     * The current user's new password confirmation.
     */
    //TODO: Refactor to a better name.
    private String confirmNewPassword;

    /**
     * Prepares the user profile page.
     *
     * @return The path to the user profile page.
     */
    public String prepareProfile() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = empManager.getCurrentEmployee();
        if (employee == null) {
            return null;
        }
        credentials = credentialsManager.getCredentialsByEmpNumber(employee.getEmpNumber());
        editEmployee = new EditableEmployee(employee, true);
        return "/employee/profile";
    }

    /**
     * Saves the current user's profile and performs validation checking.
     *
     * @return The path to the list of timesheet page.
     */
    //TODO: Replace Strings.
    public String onSaveProfile() {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (oldPassword == null || newPassword == null || confirmNewPassword == null) {
            context.addMessage(null, new FacesMessage("Please fill in the required fields"));
            return null;
        }

        if (!credentials.getPassword().equals(oldPassword)) {
            context.addMessage(null, new FacesMessage("Old password is incorrect"));
            return null;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            context.addMessage(null, new FacesMessage("New password and confirm new password do not match"));
            return null;
        }

        credentials.setUserName(editEmployee.getEmployee().getUserName());
        credentials.setPassword(newPassword);
        conversation.end();
        return "/timesheet/list";
    }

    /**
     * Gets the employee.
     *
     * @return The employee.
     */
    public EditableEmployee getEditEmployee() {
        return editEmployee;
    }

    /**
     * Sets the employee.
     * 
     * @param employee The employee.
     */
    public void setEditEmployee(EditableEmployee employee) {
        editEmployee = employee;
    }

    /**
     * Gets the user's old password.
     *
     * @return The user's old password as a String.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the user's old password.
     *
     * @param oldPw The user's old password.
     */
    public void setOldPassword(String oldPw) {
        oldPassword = oldPw;
    }

    /**
     * Gets the user's new password.
     *
     * @return The user's new password as a String.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the user's new password.
     * 
     * @param newPw The user's new password.
     */
    public void setNewPassword(String newPw) {
        newPassword = newPw;
    }
    
    /**
     * Gets the user's confirmed new password.
     *
     * @return The user's confirmed new password as a String.
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Sets the user's confirmed new password.
     * 
     * @param confirmNewPw The user's confirmed new password.
     */
    public void setConfirmNewPassword(String confirmNewPw) {
        confirmNewPassword = confirmNewPw;
    }
}