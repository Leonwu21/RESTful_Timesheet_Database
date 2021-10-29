package ca.bcit.infosys.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Admin;
import ca.bcit.infosys.employee.Employee;

/**
 * The class for the database of admins.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("adminDatabase")
@ApplicationScoped
public class AdminDatabase implements Serializable {

    private static final long serialVersionUID = 5L;
    
    /**
     * A list of all the admins.
     */
    private final List<Admin> adminList;

    /**
     * Constructor for AdminDatabase.
     */
    public AdminDatabase() {
        adminList = new ArrayList<>();
        adminList.add(new Admin(new Employee("John Doe", 1111, "admin")));
    }

    /**
     * Gets the list of admins.
     * 
     * @return The list of admins.
     */
    public List<Admin> getAdminList() {
        return adminList;
    }
}
