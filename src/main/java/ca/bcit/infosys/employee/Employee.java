package ca.bcit.infosys.employee;

import java.io.Serializable;

/**
 * A class for an Employee.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class Employee implements Serializable {
    //TODO: What is this for? Do we need this specific number?
    private static final long serialVersionUID = 11L;
    
    /** 
     * The employee's name. 
     */
    private String empName;
    
    /** 
     * The employee's employee number.
     */
    private int empNumber;
    
    /** 
     * The employee's login ID.
     */
    private String userName;


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
    public Employee(final String name, final int number, final String id) {
        empName = name;
        empNumber = number;
        userName = id;
    }

    /**
     * Gets the employee's name.
     * @return The employee's name as a String.
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * Sets the employee's name.
     * @param empName The employee's name.
     */
    public void setEmpName(final String name) {
        empName = name;
    }

    /**
     * Gets the employee's employee number.
     * @return The employee's employee number as an int.
     */
    public int getEmpNumber() {
        return empNumber;
    }

    /**
     * Sets the employee's employee number.
     * @param number The employee's employee number.
     */
    public void setEmpNumber(final int number) {
        empNumber = number;
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
     * Returns the employee's name, employee number, and login ID as a String.
     * @return The employee's name, employee number, and login ID as a String.
     */
    @Override
    public String toString() {
        return empName + '\t' + empNumber + '\t' + userName;
    }


}
