package ca.bcit.infosys.restful;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.manager.EmployeeManager;

@Path("/employees")
public class EmployeeService {

    @Inject
    private EmployeeManager employeeManager;

    /**
     * Gets the employee with the specified employee number.
     * @return The employee with the specified employee number.
     */
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Employee find(@PathParam("id") Integer empId) {
//        if (authEmployee.getRole() == Role.EMPLOYEE && authEmployee.getEmpNumber() != empId) {
//            throw new WebApplicationException(Response.Status.FORBIDDEN);
//        }
        Employee employee = employeeManager.getEmployeeByNumber(empId);
        if (employee == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employee;
    }

    /**
     * Gets the list of employees.
     * @return The list of Employees as an Array.
     */
    //@Secured({ Role.ADMIN })
    @GET
    @Produces("application/json")
    public Employee[] getEmployeeList() {
        Employee[] employees;
        List<Employee> employeeList = employeeManager.getEmployeeList();
        employees = new Employee[employeeList.size()];
        for (int i = 0; i < employeeList.size(); i++) employees[i] = employeeList.get(i);
        return employees;
    }

    /**
     * Adds the employee of interest into database AKA "Persist"
     * @param employee The employee to be added.
     */
    //@Secured({ Role.ADMIN })
    @POST
    @Consumes("application/json")
    public Response addEmployee(Employee employee) {
        employeeManager.addEmployee(employee);
        return Response.created(URI.create("/employees/" + 
                employee.getEmployeeNumber())).build();
    }

    /**
     * Updates an existing employee record in the employees table AKA "Merge"
     *
     * @param id of employee to be edited
     */
    //@Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response editEmployee(@PathParam("id") Integer id) {
//        if (authEmployee.getRole() == Role.EMPLOYEE && authEmployee.getEmpNumber() != empId) {
//            throw new WebApplicationException("Cannot edit another employee's credentials", Response.Status.FORBIDDEN);
//        }
        Employee employee = employeeManager.getEmployeeByNumber(id);
        employeeManager.editEmployee(employee);
        return Response.noContent().build();
    }

    /**
     * Deletes the employee of interest from database AKA "Remove".
     * @param id of the employee to be deleted.
     */
    //@Secured({ Role.ADMIN })
    @Path("/{id}")
    @DELETE
    public Response remove(@PathParam("id") Integer id) {
        Employee employee = employeeManager.getEmployeeByNumber(id);
        employeeManager.deleteEmployee(employee);
        return Response.ok().build();
    }

}
