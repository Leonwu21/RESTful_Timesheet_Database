package ca.bcit.infosys.employee;

import ca.bcit.infosys.authentication.Permission;

/**
 * A class for an Employee.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class Employee {
    
    /** 
     * The employee's employee number.
     */
    private int employeeNumber;
    /** 
     * The employee's name. 
     */
    private String employeeName;
    
    /** 
     * The employee's login ID.
     */
    private String userName;

    /**
     * True if employee is an admin, else false.
     */
    private boolean isAdmin;

    /**
     * The employee's permission for RESTful services
     */
    private Permission permission;
    
    /**
     * The no-argument constructor. Used to create new employees from within the
     * application.
     */
    public Employee() {
    }

    /**
     * The argument-containing constructor. Used to create the initial employees
     * who have access as well as the administrator.
     *
     * @param name the name of the employee.
     * @param number the empNumber of the user.
     * @param id the loginID of the user.
     */
    public Employee(final int number, final String name, final String id,
            final boolean admin) {
        employeeName = name;
        employeeNumber = number;
        userName = id;
        isAdmin = admin;
    }
    
    /**
     * Returns the employee's name, employee number, and login ID as a String.
     * @return The employee's name, employee number, and login ID as a String.
     */
    @Override
    public String toString() {
        return employeeName + '\t' + employeeNumber + '\t' + userName;
    }

    /**
     * Gets the employee's name.
     * @return The employee's name as a String.
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the employee's name.
     * @param employeeName The employee's name.
     */
    public void setEmployeeName(final String name) {
        employeeName = name;
    }

    /**
     * Gets the employee's employee number.
     * @return The employee's employee number as an int.
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Sets the employee's employee number.
     * @param number The employee's employee number.
     */
    public void setEmployeeNumber(final int number) {
        employeeNumber = number;
    }

    /**
     * Gets the employee's login ID.
     * @return The employee's login ID.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the employee's login ID.
     * @param id The employee's login ID.
     */
    public void setUserName(final String id) {
        userName = id;
    }

    /**
     * Gets the employee's isAdmin boolean
     * @return True if employee is an admin, else false.
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets the employee's isAdmin boolean
     * @param admin representing if employee is an admin.
     */
    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Gets the employee's Permission enum
     * @return permission of employee
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Sets the employee's Permission enum
     * @param permission of employee
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}