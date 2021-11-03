package ca.bcit.infosys.controller;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.manager.EmployeeManager;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * A class to control the login process.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("loginController")
@ConversationScoped
public class LoginController implements Serializable {
    
    private static final long serialVersionUID = 2L;

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
     * The employee's login ID.
     */
    private String userName;
    /**
     * The employee's password.
     */
    private String password;

    /**
     * Logs a user in. Lets user know whether login was successful or not.
     * @return A notification, as a String, to let the user know whether login was successful or not.
     */
    public String login() {
        if (conversation.isTransient()) {
            conversation.begin();
        }

        final Employee employee = employeeManager.getEmployeeByUserName(userName);
        final FacesContext context = FacesContext.getCurrentInstance();

        if (employee == null) {
            context.addMessage(null, new FacesMessage("Error: Login ID not found."));
            userName = null;
            password = null;
            return null;
        } else {
            final Credentials credentials = new Credentials(userName, password);
            credentials.setEmployeeNumber(employee.getEmployeeNumber());
            if (!employeeManager.verifyUser(credentials)) {
                context.addMessage(null, new FacesMessage("Error: Invalid Login ID & password combination."));
                return null;
            }
            context.getExternalContext().getSessionMap().put("employeeNumber", employee.getUserName());
            conversation.end();
            return "login";
        }
    }

    /**
     * Gets the employee's login ID.
     * @return The employee's login ID as a String.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the employee's login ID.
     * @param id The employee's login ID.
     */
    public void setUserName(String id) {
        userName = id;
    }

    /**
     * Gets the employee's password.
     * @return The employee's password as a String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     * @param pw The employee's password.
     */
    public void setPassword(String pw) {
        password = pw;
    }

}

