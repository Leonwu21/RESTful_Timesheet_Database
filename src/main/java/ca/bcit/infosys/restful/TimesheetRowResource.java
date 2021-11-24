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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.infosys.manager.TimesheetManager;
import ca.bcit.infosys.manager.TimesheetRowManager;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Path("/rows")
public class TimesheetRowResource {

    @Inject
    TimesheetRowManager tsRowManager;

    @Inject
    TimesheetManager tsManager;


    /**
     * Gets list of timesheet rows by timesheetId
     *
     * @param id of timesheet
     * @return ArrayList of TimesheetRow objects
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public ArrayList<TimesheetRow> getTimesheetRows(@PathParam("id") Integer timesheetId) {
        ArrayList<TimesheetRow> rowsList;
        rowsList = tsRowManager.getTimesheetRows(timesheetId);
        if (rowsList == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return rowsList;
    }

    
    /**
     * Adds TimesheetRow object to a timesheet object in the database
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param rows specifies rows to be added
     */
    @POST
    @Consumes("application/json")
    public Response addRow(@QueryParam("timesheetId") Integer timesheetId, List<TimesheetRow> rows) {
        try {
            Timesheet timesheet = tsManager.find(timesheetId);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Could not find a matching timesheet via provided ID").build();
            } else if (timesheet.getDetails().size() == Timesheet.DAYS_IN_WEEK) {
                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .entity("Cannot add more than 7 rows to a timesheet").build();
            }
            tsRowManager.addRow(timesheetId, rows);
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/rows/" + timesheetId)).build();
    }

    /**
     * Edits TimesheetRow object in database (update)
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param rows specifies rows to be added
     */
    @PATCH
    @Consumes("application/json")
    public Response editRow(@QueryParam("timesheetId") Integer timesheetId, List<TimesheetRow> rows) {
        try {
            tsRowManager.editRow(timesheetId, rows);
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }

}
