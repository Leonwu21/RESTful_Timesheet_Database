package ca.bcit.infosys.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.database.AdminDatabase;
import ca.bcit.infosys.database.CredentialsDatabase;
import ca.bcit.infosys.database.EmployeeDatabase;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Class to manage employees.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
@Named("employeeManager")
@ConversationScoped
public class EmployeeManager implements EmployeeList, Serializable {
    
    private static final long serialVersionUID = 15L;

    /**
     * Injected EmployeeDatabase.
     */
    @Inject
    private EmployeeDatabase employeeDatabase;
    
    /**
     * Injected AdminDatabase.
     */
    @Inject
    private AdminDatabase adminDatabase;

    /**
     * Injected CredentialsManager.
     */
    @Inject
    private CredentialsManager credentialsManager;
    
    /**
     * Injected CredentialsDatabase.
     */
    @Inject
    private CredentialsDatabase credentialsDatabase;
    /**
     * Gets the list of employees.
     * @return The list of Employees as an ArrayList.
     */
    @Override
    public List<Employee> getEmployees() {
        return employeeDatabase.getEmployeeList();
    }

    /**
     * Gets the employee with the specified login ID.
     * @return The employee with the specified login ID.
     */
    @Override
    public Employee getEmployeeByUserName(String userName) {
        List<Employee> employeeList = employeeDatabase.getEmployeeList();
        
        for (Employee employee : employeeList) {
            if (employee.getUserName().equals(userName)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Gets map of valid passwords for userNames.
     * @return The map containing the valid (userName, password) combinations.
     */
    //TODO: This does nothing. May need to implement it?
    @Override
    public Map<String, String> getLoginCombos() {
        return null;
    }

    /**
     * Gets the current employee. 
     * @return The current employee.
     */
    @Override
    //TODO: Refactor method chaining.
    public Employee getCurrentEmployee() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String username = (String) context.getExternalContext().getSessionMap().get("emp_no");
        return getEmployeeByUserName(username);
    }

    /**
     * Gets the admin. Assumes there is a single administrator.
     * @return The admin.
     */
    @Override
    public Employee getAdministrator() {
        return adminDatabase.getAdminList().get(0);
    }

    /**
     * Checks if the admin is logged in.
     * @return True if the admin is logged in. Otherwise, false.
     */
    public Boolean isAdminLogin() {
        final Employee currEmployee = getCurrentEmployee();
        final Employee admin = getAdministrator();
        if (admin == null || currEmployee == null) {
            return false;
        }
        return currEmployee.getEmpNumber() == admin.getEmpNumber();
    }
    
    /**
     * Verifies that the login ID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return True if the login ID and password is a valid combination. Otherwise, false.
     */
    //TODO: Refactor method chaining.
    //TODO: Refactor to use credentialsManager (See TimesheetController.java prepareList() for an example)
    @Override
    public boolean verifyUser(Credentials credentials) {
        for (Credentials c : credentialsDatabase.getCredentialsList()) {
            return (c.equals(credentials));
        };
        return false;
    }

    /**
     * Logs the user out of the system.
     *
     * @param employee the user to logout.
     * @return a String representing the login page.
     */
    //TODO: Why do we need to feed in employee parameter?
    //TODO: Refactor method chaining.
    @Override
    public String logout(Employee employee) {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout";
    }

    /**
     * Deletes the employee of interest.
     * @param employee The employee to be deleted.
     */
    //TODO: Refactor method chaining.
    @Override
    public void deleteEmployee(Employee employee) {
        if (getAdministrator().getUserName().equals(employee.getUserName())) {
            return;
        }
        employeeDatabase.getEmployeeList().remove(employee);
    }

    /**
     * Adds the employee of interest.
     * @param employee The employee to be added.
     */
    //TODO: Refactor method chaining.
    @Override
    public void addEmployee(Employee employee) {
        for (final Employee emp : employeeDatabase.getEmployeeList()) {
            if (emp.getUserName().equals(employee.getUserName())) {
                throw new IllegalArgumentException("A user with the same username already exists");
            }

            if (emp.getEmpNumber() == employee.getEmpNumber()) {
                throw new IllegalArgumentException("A user with the same employee number already exists");
            }
        }
        employeeDatabase.getEmployeeList().add(employee);
    }
}