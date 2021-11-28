package ca.bcit.infosys.restful;

import java.net.URI;
import java.util.ArrayList;
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
import ca.bcit.infosys.manager.TimesheetRowManager;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
import ca.bcit.infosys.authentication.Permission;

@Path("/rows")
public class TimesheetRowResource {

    @Inject
    TimesheetRowManager tsRowManager;

    @Inject
    TimesheetManager tsManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    /**
     * Gets list of timesheet rows by timesheetId
     *
     * @param id of timesheet
     * @return ArrayList of TimesheetRow objects
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @GET
    @Path("{id}")
    @Produces("application/json")
    public ArrayList<TimesheetRow> getTimesheetRows(@PathParam("id") Integer id) {
        Response res = checkAuth(id);
        if (res != null) throw new WebApplicationException((String) res.getEntity(),
                res.getStatus());
        ArrayList<TimesheetRow> rowsList;
        try {
            rowsList = tsRowManager.getTimesheetRows(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (rowsList == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return rowsList;
    }

    /**
     * Edits TimesheetRow object in database (update)
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param rows specifies rows to be added
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @PATCH
    @Path("{id}")
    @Consumes("application/json")
    public Response editRow(@PathParam("id") Integer id, 
            List<TimesheetRow> rows) {
        Response res = checkAuth(id);
        if (res != null) throw new WebApplicationException((String) res.getEntity(),
                res.getStatus());
        try {
            Timesheet timesheet = tsManager.find(id);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Could "
                        + "not find timesheet via provided ID").build();
            }
            if (timesheet.getEmployee().getEmployeeNumber() !=
                    authEmployee.getEmployeeNumber()) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        "Cannot edit another employee's timesheet").build();
            }
            tsRowManager.editRow(id, rows);
            
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }
    
    /**
     * Adds TimesheetRow object to a timesheet object in the database
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param rows specifies rows to be added
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @POST
    @Path("{id}")
    @Consumes("application/json")
    public Response addRow(@PathParam("id") Integer id,
            List<TimesheetRow> rows) {
        Response res = checkAuth(id);
        if (res != null) throw new WebApplicationException((String) res.getEntity(),
                res.getStatus());
        try {
            Timesheet timesheet = tsManager.find(id);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Could "
                        + "not find timesheet via provided ID").build();
            }
            if (timesheet.getDetails().size() == Timesheet.DAYS_IN_WEEK) {
                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .entity("Cannot add more than 7 rows to a timesheet").build();
            }
            if (timesheet.getEmployee().getEmployeeNumber() !=
                    authEmployee.getEmployeeNumber()) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        "Cannot edit another employee's timesheet").build();
            }
            tsRowManager.addRow(id, rows);
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/rows/" + id)).build();
    }

    /**
     * Checks authentication of user
     * @param id of timesheet
     * @return response
     */
    private Response checkAuth(int id) {
        Timesheet ts = tsManager.find(id);
        if (ts == null) throw new WebApplicationException(
                "Could not find timesheet", Response.Status.NOT_FOUND);
        if (ts.getEmployee().getEmployeeNumber() !=
                authEmployee.getEmployeeNumber())
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    "Cannot access another user's timesheet").build();
        return null;
    }
}
