package ca.bcit.infosys.restful;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.authentication.AuthenticatedEmployee;
import ca.bcit.infosys.authentication.Secured;
import ca.bcit.infosys.manager.TimesheetManager;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.authentication.Permission;

@Path("/timesheets")
public class TimesheetResource {

    @Inject
    TimesheetManager tsManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;
    
    /**
     * Gets a list of all timesheets
     * @return list containing timesheets
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @GET
    @Path("list")
    @Produces("application/json")
    public Timesheet[] getTimesheets() {
        Timesheet[] arr;
        try {
            List<Timesheet> list = tsManager.getTimesheets();
            arr = new Timesheet[list.size()];
            for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return arr;
    }

    /**
     * Gets a timesheet with the specified ID
     * @param id of the timesheet
     * @return timesheet
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Timesheet getTimesheetById(@PathParam("id") Integer id) {
        Timesheet timesheet = null;
        try {
            timesheet = tsManager.find(id);
            if (!authEmployee.getIsAdmin()
                    && timesheet.getEmployee().getEmployeeNumber() !=
                    authEmployee.getEmployeeNumber()) {
                throw new WebApplicationException("Cannot access another " +
                    "employee's timesheets!", Response.Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheet == null) {
           throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheet;
    }

    /**
     * Updates an existing timesheet
     * @param timesheet to be updated
     * @param timesheetId to identify timesheet
     * @return Response object
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @PATCH
    @Path("{id}")
    @Consumes("application/json")
    public Response updateTimesheet(Timesheet timesheet, @PathParam("id") Integer id) {
        Response res = checkAuth(timesheet);
        if (res != null) return res;
        try {
            tsManager.updateTimesheet(timesheet, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }
    
    @POST
    @Consumes("application/json")
    /**
     * Creates a new timesheet
     * @param timesheet to be created
     * @return Response object
     */
    public Response addTimesheet(Timesheet timesheet) {
        Response res = checkAuth(timesheet);
        if (res != null) return res;
        try {
            tsManager.addTimesheet(timesheet);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/timesheets/" +
                timesheet.getTimesheetId())).build();
    }

    /**
     * Checks for authorization
     * @param timesheet to be checked
     * @return response
     */
    private Response checkAuth(Timesheet timesheet) {
        if (!authEmployee.getIsAdmin()
                && timesheet.getEmployee().getEmployeeNumber() !=
                authEmployee.getEmployeeNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    "Cannot access another employee's timesheet!").build();
        }
        return null;
    }
}
