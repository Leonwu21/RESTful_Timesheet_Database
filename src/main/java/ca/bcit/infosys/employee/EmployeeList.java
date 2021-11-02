package ca.bcit.infosys.employee;

import java.util.List;
import java.util.Map;

/**
 * Interface to back-end to access Employees and verify login credentials.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
public interface EmployeeList {
	
    /**
     * Gets the list of employees.
     * @return The list of Employees as an ArrayList.
     */
    List<Employee> getEmployees();

    /**
     * Gets the employee with the specified login ID.
     * @return The employee with the specified login ID.
     */
    Employee getEmployeeByUserName(String name);

    /**
     * Gets map of valid passwords for userNames.
     * @return The map containing the valid (userName, password) combinations.
     */
    Map<String, String> getLoginCombos();

    /**
     * Gets the current employee. 
     * @return The current employee.
     */
    Employee getCurrentEmployee();

    /**
     * Gets the admin. Assumes there is a single administrator.
     * @return The admin.
     */
    Employee getAdministrator();

    /**
     * Verifies that the login ID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return True if the login ID and password is a valid combination. Otherwise, false.
     */
    boolean verifyUser(Credentials credential);

    /**
     * Logs the user out of the system.
     *
     * @param employee the user to logout.
     * @return a String representing the login page.
     */
    String logout(Employee employee);

    /**
     * Deletes the employee of interest.
     * @param employee The employee to be deleted.
     */
    void deleteEmployee(Employee employee);

    /**
     * Adds the employee of interest.
     * @param employee The employee to be added.
     */
    void addEmployee(Employee employee);
}
