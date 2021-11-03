package ca.bcit.infosys.employee;

/**
 * A class for a set of Credentials.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class Credentials {
    
    /** 
     * The login ID.
     */
    private String userName;
    
    /** 
     * The password.
     */
    private String password;
    
    /**
     * The employee number.
     */
    private int employeeNumber;
    
    /**
     * Default constructor for Credentials.
     */
    public Credentials() {   
    }
    
    /**
     * Constructor for Credentials.
     * @param id The login ID.
     * @param pw The password.
     */
    public Credentials(String id, String pw) {
        userName = id;
        password = pw;
    }
    
    /**
     * Returns the login ID and password as a String.
     * @return The login ID and password as a String.
     */
    @Override
    public String toString() {
        return userName + '\t' + password;
    }
    
    /**
     * Checks if two Credential objects are the same.
     * @return True if two Credential objects are the same. Otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Credentials other = (Credentials) obj;
        if (employeeNumber != other.employeeNumber)
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }
    
    /**
     * Gets the login ID.
     * @return The login ID as a String.
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Sets the login ID.
     * @param id The login ID.
     */
    public void setUserName(final String id) {
        userName = id;
    }
    
    /**
     * Gets the password.
     * @return The password as a String.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * @param pw The password.
     */
    public void setPassword(final String pw) {
        password = pw;
    }
    
    /**
     * Gets the employee's number.
     * @return The employee's number.
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }
    
    /**
     * Sets the employee's number.
     * @param empNumber The employee's number.
     */
    public void setEmployeeNumber(int empNumber) {
        employeeNumber = empNumber;
    }
}