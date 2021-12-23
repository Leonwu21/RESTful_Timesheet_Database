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

import ca.bcit.infosys.authentication.AuthenticatedEmployee;
import ca.bcit.infosys.authentication.Permission;
import ca.bcit.infosys.authentication.Secured;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.manager.EmployeeManager;

@Path("/employees")
public class EmployeeResource {

    @Inject
    private EmployeeManager employeeManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;
    
    
    /**
     * Gets the list of employees.
     * @return The list of Employees as an Array.
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @GET
    @Produces("application/json")
    public Employee[] getEmployeeList() {
        Employee[] employees;
        try {
            List<Employee> employeeList = employeeManager.getEmployeeList();
            employees = new Employee[employeeList.size()];
            for (int i = 0; i < employeeList.size(); i++) employees[i] = employeeList.get(i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (employees.length == 0) throw new WebApplicationException(
                "Employee list is empty", Response.Status.NOT_FOUND);
        return employees;
    }
    
    /**
     * Gets the employee with the specified employee number.
     * @param id of employee
     * @return The employee with the specified employee number.
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Employee find(@PathParam("id") Integer id) {
        Employee employee = employeeManager.getEmployeeByNumber(id);
        if (employee == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employee;
    }

    /**
     * Updates an existing employee record in the employees table AKA "Merge"
     *
     * @param id of employee to be edited
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @Path("{id}")
    @PATCH
    @Consumes("application/json")
    public Response editEmployee(@PathParam("id") Integer id) {
        if (!authEmployee.getIsAdmin() &&
                authEmployee.getEmployeeNumber() != id) {
            throw new WebApplicationException("Cannot edit another employee's "
                    + "credentials!", Response.Status.UNAUTHORIZED);
        }
        try {
            Employee employee = employeeManager.getEmployeeByNumber(id);
            employeeManager.editEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Adds the employee of interest into database AKA "Persist"
     * @param employee The employee to be added.
     */
    @Secured({ Permission.ADMIN })
    @POST
    @Consumes("application/json")
    public Response addEmployee(Employee employee) {
        if (!authEmployee.getIsAdmin()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            employeeManager.addEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/employees/" + 
                employee.getEmployeeNumber())).build();
    }

    /**
     * Deletes the employee of interest from database AKA "Remove".
     * @param id of the employee to be deleted.
     */
    @Secured({ Permission.ADMIN })
    @Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") Integer id) {
        if (!authEmployee.getIsAdmin()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            Employee employee = employeeManager.getEmployeeByNumber(id);
            employeeManager.deleteEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.ok().build();
    }

}
