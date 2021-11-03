package ca.bcit.infosys.database;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;

/**
 * The class for the database of employees.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 *
 */
@Named("employeeDatabase")
@ApplicationScoped
public class EmployeeDatabase {
	
    /**
     * A list of all the employees.
     */
    private final List<Employee> employeeList;
    
    /**
     * Constructor for EmployeeDatabase.
     */
    public EmployeeDatabase() {
        employeeList = new ArrayList<>();
        Employee employee = new Employee("John Doe", 1111, "admin");
        employeeList.add(employee);
    }

    /**
     * Gets the list of employees.
     *
     * @return The list of employees.
     */
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

}