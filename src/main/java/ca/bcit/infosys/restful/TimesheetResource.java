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

import ca.bcit.infosys.manager.TimesheetManager;
import ca.bcit.infosys.timesheet.Timesheet;

@Path("/timesheets")
public class TimesheetResource {

    /**
     * Provides access to the timesheet table
     */
    @Inject
    TimesheetManager tsManager;

    /**
     * Gets a list of all timesheets
     * @return list containing timesheets
     */
    @GET
    @Path("list")
    @Produces("application/json")
    public Timesheet[] getTimesheets() {
        List<Timesheet> list = tsManager.getTimesheets();
        Timesheet[] arr = new Timesheet[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    /**
     * Gets a timesheet with the specified ID
     * @param id of the timesheet
     * @return timesheet
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Timesheet getTimesheetById(@PathParam("id") Integer id) {
        Timesheet timesheet = tsManager.find(id);
        if (timesheet == null) {
           throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheet;
    }

    @POST
    @Consumes("application/json")
    /**
     * Creates a new timesheet
     * @param timesheet to be created
     * @return Response object
     */
    public Response addTimesheet(Timesheet timesheet) {
        tsManager.addTimesheet(timesheet);
        return Response.created(URI.create("/timesheets/" +
                timesheet.getTimesheetId())).build();
    }

    /**
     * Updates an existing timesheet
     * @param timesheet to be updated
     * @param timesheetId to identify timesheet
     * @return Response object
     */
    @PATCH
    @Path("{id}")
    @Consumes("application/json")
    public Response updateTimesheet(Timesheet timesheet, @PathParam("id") Integer timesheetId) {
        tsManager.updateTimesheet(timesheet, timesheetId);
        return Response.noContent().build();
    }
}
