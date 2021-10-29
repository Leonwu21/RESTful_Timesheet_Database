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
    
    //TODO: Why do we need this? Does it need to be this number?
    private static final long serialVersionUID = 6687823809360236313L;

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
     *
     * @return A notification, as a String, to let the user know whether login was successful or not.
     */
    //TODO: Refactor if-else statement.
    public String login() {
        if (conversation.isTransient()) {
            conversation.begin();
        }

        final Employee employee = employeeManager.getEmployeeByUserName(userName);
        final FacesContext context = FacesContext.getCurrentInstance();

        if (employee == null) {
            context.addMessage(null, new FacesMessage("Unknown login, please try again"));
            userName = null;
            password = null;
            return null;
        } else {
            final Credentials credentials = new Credentials(userName, password);
            credentials.setEmpNumber(employee.getEmpNumber());
            if (!employeeManager.verifyUser(credentials)) {
                context.addMessage(null, new FacesMessage("Could not authenticate user, please try again"));
                return null;
            }
            context.getExternalContext().getSessionMap().put("emp_no", employee.getUserName());
            conversation.end();
            return "success";
        }
    }

    //TODO: EmployeeList has a logout method as well. Currently implemented through there. If OK, then delete this method.
//    /**
//     * Logs the user out.
//     *
//     * @return A notification, "logout", as a String.
//     */
//    public String logout() {
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        return "logout";
//    }

    /**
     * Gets the employee's login ID.
     *
     * @return The employee's login ID as a String.
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Sets the employee's login ID.
     *
     * @param id The employee's login ID.
     */
    public void setUsername(String id) {
        userName = id;
    }

    /**
     * Gets the employee's password.
     *
     * @return The employee's password as a String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     *
     * @param pw The employee's password.
     */
    public void setPassword(String pw) {
        password = pw;
    }

}

