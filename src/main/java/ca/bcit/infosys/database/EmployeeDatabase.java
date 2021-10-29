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
    //TODO: Implement a setter.
    private final List<Employee> employeeList;
    
    /**
     * Constructor for EmployeeDatabase.
     */
    //TODO: Edit default employee to something else.
    public EmployeeDatabase() {
        employeeList = new ArrayList<>();
        employeeList.add(new Employee("Bruce Link", 1234, "bdlink"));
    }

    /**
     * Gets the list of employees.
     *
     * @return The list of employees.
     */
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

}
