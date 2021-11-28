package ca.bcit.infosys.restful;

import java.net.URI;

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

import ca.bcit.infosys.authentication.Secured;
import ca.bcit.infosys.authentication.Permission;
import ca.bcit.infosys.authentication.AuthenticatedEmployee;

import ca.bcit.infosys.manager.CredentialsManager;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

@Path("/credentials")
public class CredentialResource {

    @Inject
    CredentialsManager credentialsManager;
    
    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    /**
     * Gets the credentials of an employee with a specified username.
     * @param username of the employee.
     * @return The credentials of the employee with the username
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @Path("/{username}")
    @GET
    @Produces("application/json")
    public Credentials find(@PathParam("username") String username) {
        Response res = checkCred(authEmployee, username);
        if (res != null) throw new WebApplicationException(res.getStatus());
        Credentials cred = credentialsManager.getCredentialsByUsername(username);
        if (cred == null) {
            throw new WebApplicationException(
                    "Cannot find credentials with username provided",
                    Response.Status.NOT_FOUND);
        }
        return cred;
    }

    /**
     * Updates a Credential value in the database
     * @param c credential to be updated
     * @param id of employee
     * @return response
     */
    @Secured({ Permission.ADMIN, Permission.USER })
    @Path("{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Credentials c, @PathParam("id") Integer id) {
        final Response errorRes = checkCred(authEmployee, id);
        if (errorRes != null) return errorRes;
        try {
            credentialsManager.merge(c, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }
    
    /**
     * Adds a set of credentials to the CredentialsDatabase AKA "Persist".
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    @Secured({ Permission.ADMIN })
    @POST
    @Consumes("application/json")
    public Response addCredentials(Credentials credentials) {
        try {
            credentialsManager.addCredentials(credentials);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/credentials/" +
                credentials.getEmployeeNumber())).build();
    }

    /**
     * Overloaded helper method checks if credentials belong to current employee
     * @param authEmployee to be checked
     * @param id of employee
     * @return response 
     */
    private static Response checkCred(Employee authEmployee, int id) {
        if (!authEmployee.getIsAdmin() &&
                (authEmployee.getEmployeeNumber() != id)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    "Cannot access another employee's credentials!").build();
        }
        return null;
    }

    /**
     * Overloaded helper method checks if credentials belong to current employee
     * @param authEmployee to be checked
     * @param username of employee
     * @return response 
     */
    private static Response checkCred(Employee authEmployee, String username) {
        if (!authEmployee.getIsAdmin()
                && !(authEmployee.getUserName().equals(username))) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    "Cannot access another employee's credentials!").build();
        }
        return null;
    }

}
