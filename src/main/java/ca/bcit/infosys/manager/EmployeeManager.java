package ca.bcit.infosys.manager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import javax.sql.DataSource;

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
     * Datasource for the timesheet system
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_system")
    private DataSource dataSource;
    
    
    /**
     * Provides access to the credentials table in the datasource
     */
    @Inject
    private CredentialsManager credentialsManager;
    
    /**
     * Gets the list of employees.
     * @return The list of Employees as an ArrayList.
     */
    @Override
    public List<Employee> getEmployeeList() {
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Employees"
                            + " ORDER BY employeeNumber;");
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        employeeList.add(new Employee(
                                result.getInt("employeeNumber"),
                                result.getString("employeeName"),
                                result.getString("userName"),
                                result.getBoolean("isAdmin")));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getEmployeeList");
            ex.printStackTrace();
            return null;
        }
        return employeeList;
    }

    /**
     * Gets the employee with the specified login ID.
     * @return The employee with the specified login ID.
     */
    @Override
    public Employee getEmployeeByUserName(String userName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Employees"
                            + " WHERE userName = ?");
                    stmt.setString(1, userName);
                    ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Employee(result.getInt("employeeNumber"),
                                result.getString("employeeName"),
                                result.getString("userName"),
                                result.getBoolean("isAdmin"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getEmployeeByUsername " + userName);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the employee with the specified employee number.
     * @return The employee with the specified employee number.
     */
    public Employee getEmployeeByNumber(int num) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Employees"
                            + " WHERE employeeNumber = ?");
                    stmt.setInt(1, num);
                    ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Employee(result.getInt("employeeNumber"),
                                result.getString("employeeName"),
                                result.getString("userName"),
                                result.getBoolean("isAdmin"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getEmployeeByNumber " + num);
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Adds the employee of interest into database AKA "Persist"
     * @param employee The employee to be added.
     */
    public void addEmployee(Employee employee) {
        // order of fields in INSERT statement
        final int employeeNumber = 1;
        final int employeeName = 2;
        final int userName = 3;
        final int isAdmin = 4;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Employees "
                            + "VALUES (?, ?, ?, ?)");
                    stmt.setInt(employeeNumber, employee.getEmployeeNumber());
                    stmt.setString(employeeName, employee.getEmployeeName());
                    stmt.setString(userName, employee.getUserName());
                    stmt.setBoolean(isAdmin, employee.getIsAdmin());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in addEmployee " + employee);
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Updates an existing employee record in the employees table AKA "Merge"
     *
     * @param employee to be edited
     */
    public void editEmployee(Employee employee) {
        final int employeeName = 1;
        final int userName = 2;
        final int employeeNumber = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Employees "
                            + "SET employeeName = ?, userName = ? "
                            + "WHERE employeeNumber = ?");
                    stmt.setString(employeeName, employee.getEmployeeName());
                    stmt.setString(userName, employee.getUserName());
                    stmt.setInt(employeeNumber, employee.getEmployeeNumber());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in editEmployee " + employee);
            ex.printStackTrace();
        }
    }
    
    /**
     * Deletes the employee of interest from database AKA "Remove".
     * @param employee The employee to be deleted.
     */
    @Override
    public void deleteEmployee(Employee employee) {
        // Checks if employee is an admin first
        String administratorId = getAdministrator().getUserName();
        String employeeId = employee.getUserName();
        if (administratorId.equals(employeeId)) {
            return;
        }
        
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("DELETE FROM Employees "
                            + "WHERE employeeNumber = ?");
                    stmt.setInt(1, employee.getEmployeeNumber());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in deleteEmployee " + employee);
            ex.printStackTrace();
        }
        credentialsManager.deleteCredentials(employee);
    }
    
    /**
     * Gets map of valid passwords for userNames.
     * @return The map containing the valid (userName, password) combinations.
     */
    @Override
    public Map<String, String> getLoginCombos() {
        return null;
    }

    /**
     * Gets the current employee. 
     * @return The current employee.
     */
    @Override
    public Employee getCurrentEmployee() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String id = (String) context.getExternalContext().
                getSessionMap().get("employeeNumber");
        return getEmployeeByUserName(id);
    }

    /**
     * Gets the admin. Assumes there is a single administrator.
     * @return The admin.
     */
    @Override
    public Employee getAdministrator() {
        List<Employee> employeeList = getEmployeeList();
        for (Employee e : employeeList) {
            if (e.getIsAdmin()) return e;
        }
        return null;
    }
    
    /**
     * Verifies that the login ID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return True if the login ID and password is a valid combination. Otherwise, false.
     */
    @Override
    public boolean verifyUser(Credentials credentials) {
        List<Credentials> credentialsList = credentialsManager.getCredentialsList();
        for (Credentials c : credentialsList) {
            if (c.equals(credentials)) return true;
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
     * Checks if the admin is logged in.
     * @return True if the admin is logged in. Otherwise, false.
     */
    public Boolean isAdminLogin() {
        final Employee currentEmployee = getCurrentEmployee();
        final Employee admin = getAdministrator();
        if (admin == null || currentEmployee == null) {
            return false;
        }
        return currentEmployee.getEmployeeNumber() == admin.getEmployeeNumber();
    }
}