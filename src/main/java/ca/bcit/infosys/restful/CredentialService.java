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

import ca.bcit.infosys.manager.CredentialsManager;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

@Path("/credentials")
public class CredentialService {

    @Inject
    CredentialsManager credentialsManager;

    /**
     * Gets the credentials of an employee with a specified username.
     * @param username of the employee.
     * @return The credentials of the employee with the username
     */
    @Path("/{username}")
    @GET
    @Produces("application/json")
    public Credentials find(@PathParam("username") String userName) {
        Credentials cred;
        cred = credentialsManager.getCredentialsByUsername(userName);
        if (cred == null) {
            throw new WebApplicationException(
                    "Unable to find credentials with username provided",
                    Response.Status.NOT_FOUND);
        }
        return cred;
    }

    /**
     * Adds a set of credentials to the CredentialsDatabase AKA "Persist".
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    @POST
    @Consumes("application/json")
    public Response addCredentials(Credentials credentials) {
        credentialsManager.addCredentials(credentials);
        return Response.created(URI.create("/credentials/" +
                credentials.getEmployeeNumber())).build();
    }

//    @Path("/{id}")
//    @PATCH
//    @Consumes("application/json")
//    public Response merge(Credentials credentials, @PathParam("id") Integer empId) {
//        final Response errorRes = checkErrors(authEmployee, empId);
//        if (errorRes != null) {
//            return errorRes;
//        }
//        try {
//            credentialsManager.merge(credentials, empId);
//        } catch (final SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return Response.serverError().entity(e).build();
//        }
//        return Response.noContent().build();
//    }

//    // Checks if credentials belong to current employee
//    private static Response checkErrors(Employee authEmployee, int empId) {
//        if (authEmployee.getRole() == Role.EMPLOYEE && !(authEmployee.getEmpNumber() == empId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).entity("Cannot access another user's credentials")
//                    .build();
//        }
//        return null;
//    }
//
//    // Checks if credentials belong to current employee
//    private static Response checkErrors(Employee authEmployee, String username) {
//        if (authEmployee.getRole() == Role.EMPLOYEE && !(authEmployee.getUsername().equals(username))) {
//            return Response.status(Response.Status.UNAUTHORIZED).entity("Cannot access another user's credentials")
//                    .build();
//        }
//        return null;
//    }

}
