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
    @Override
    public Map<String, String> getLoginCombos() {
        return null;
    }

    //TODO: Refactor emp_no to employeeNumber?
    /**
     * Gets the current employee. 
     * @return The current employee.
     */
    @Override
    public Employee getCurrentEmployee() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String id = (String) context.getExternalContext().getSessionMap().get("emp_no");
        return getEmployeeByUserName(id);
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
        final Employee currentEmployee = getCurrentEmployee();
        final Employee admin = getAdministrator();
        if (admin == null || currentEmployee == null) {
            return false;
        }
        return currentEmployee.getEmpNumber() == admin.getEmpNumber();
    }
    
    /**
     * Verifies that the login ID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return True if the login ID and password is a valid combination. Otherwise, false.
     */
    @Override
    public boolean verifyUser(Credentials credentials) {
        List<Credentials> credentialsList = credentialsDatabase.getCredentialsList();
        for (Credentials c : credentialsList) {
            return (c.equals(credentials));
        };
        return false;
    }

    /**
     * Logs the user out of the system.
     *
     * @param employee the user to logout.
     * @return a String for navigation to the login page
     */
    @Override
    public String logout(Employee employee) {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout";
    }
    
    /**
     * Adds the employee of interest.
     * @param employee The employee to be added.
     */
    @Override
    public void addEmployee(Employee employee) {
        List<Employee> employeeList = employeeDatabase.getEmployeeList();
        for (final Employee emp : employeeList) {
            
            String empId = emp.getUserName();
            String employeeId = employee.getUserName();
            if (empId.equals(employeeId)) {
                throw new IllegalArgumentException("Error: An existing user has that username already.");
            }
            
            int empNumber = emp.getEmpNumber();
            int employeeNumber = employee.getEmpNumber();
            if (empNumber == employeeNumber) {
                throw new IllegalArgumentException("Error: An existing user has that employee number already.");
            }
        }
        employeeList.add(employee);
    }

    /**
     * Deletes the employee of interest.
     * @param employee The employee to be deleted.
     */
    @Override
    public void deleteEmployee(Employee employee) {
        String administratorId = getAdministrator().getUserName();
        String employeeId = employee.getUserName();
        if (administratorId.equals(employeeId)) {
            return;
        }
        List<Employee> employeeList = employeeDatabase.getEmployeeList();
        employeeList.remove(employee);
    }
}