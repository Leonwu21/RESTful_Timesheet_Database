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
public class ProfileController implements Serializable {

	private static final long serialVersionUID = 3L;
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
     * The current user's credentials.
     */
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
    private String confirmNewPassword;

    /**
     * Prepares the user profile page.
     * @return The path to the user profile page.
     */
    public String goToEmployeeProfilePage() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = employeeManager.getCurrentEmployee();
        if (employee == null) {
            return null;
        }
        int employeeNumber = employee.getEmployeeNumber();
        credentials = credentialsManager.getCredentialsByEmpNumber(employeeNumber);
        editableEmployee = new EditableEmployee(employee, true);
        return "employeeProfilePage";
    }

    /**
     * Saves the current user's profile and performs validation checking.
     * @return The path to the list of timesheet page.
     */
    public String saveProfile() {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (oldPassword == null || newPassword == null || confirmNewPassword == null) {
            context.addMessage(null, new FacesMessage("Error: Missing required fields."));
            return null;
        }

        if (!credentials.getPassword().equals(oldPassword)) {
            context.addMessage(null, new FacesMessage("Error: Incorrect old password entered."));
            return null;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            context.addMessage(null, new FacesMessage("Error: New password fields entered are not matching."));
            return null;
        }

        Employee editEmployee = editableEmployee.getEmployee();
        
        credentialsManager.editPassword(editEmployee, newPassword);
        conversation.end();
        return "timesheetListPage";
    }

    /**
     * Gets the employee.
     * @return The employee.
     */
    public EditableEmployee getEditableEmployee() {
        return editableEmployee;
    }

    /**
     * Sets the employee.
     * @param employee The employee.
     */
    public void setEditableEmployee(EditableEmployee employee) {
        editableEmployee = employee;
    }
    
    /**
     * Gets the user's credentials.
     * @return The user's credentials (login ID and password).
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the user's credentials.
     * @param creds The user's new credentials (new login ID and password).
     */
    public void setCredentials(Credentials creds) {
        credentials = creds;
    }

    /**
     * Gets the user's old password.
     * @return The user's old password as a String.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the user's old password.
     * @param oldPw The user's old password.
     */
    public void setOldPassword(String oldPw) {
        oldPassword = oldPw;
    }

    /**
     * Gets the user's new password.
     * @return The user's new password as a String.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the user's new password.
     * @param newPw The user's new password.
     */
    public void setNewPassword(String newPw) {
        newPassword = newPw;
    }
    
    /**
     * Gets the user's confirmed new password.
     * @return The user's confirmed new password as a String.
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Sets the user's confirmed new password.
     * @param confirmNewPw The user's confirmed new password.
     */
    public void setConfirmNewPassword(String confirmNewPw) {
        confirmNewPassword = confirmNewPw;
    }
}