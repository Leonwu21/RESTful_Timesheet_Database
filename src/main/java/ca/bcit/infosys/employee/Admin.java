package ca.bcit.infosys.employee;

/**
 * A class for an admin.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class Admin extends Employee {

    /**
     * Constructor for an admin.
     * @param emp The employee that is an admin.
     */
    public Admin(Employee emp) {
        super(emp.getEmployeeName(), emp.getEmployeeNumber(), emp.getUserName());
    }
}
