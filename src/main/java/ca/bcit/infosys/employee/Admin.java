package ca.bcit.infosys.employee;

import ca.bcit.infosys.employee.Employee;

/**
 * A class for an admin.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
public class Admin extends Employee {
    //TODO: Why do we need this? Does it need to be this number?
    private static final long serialVersionUID = 3893651203522967194L;

    /**
     * Constructor for an admin.
     * @param emp The employee that is an admin.
     */
    public Admin(Employee emp) {
        super(emp.getEmpName(), emp.getEmpNumber(), emp.getUserName());
    }
}
