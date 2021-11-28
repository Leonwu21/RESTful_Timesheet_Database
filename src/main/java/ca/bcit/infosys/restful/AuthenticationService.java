package ca.bcit.infosys.restful;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Hex;

import ca.bcit.infosys.manager.CredentialsManager;
import ca.bcit.infosys.authentication.TokenBuilder;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.authentication.AuthenticatedEmployee;

@Path("/authentication")
public class AuthenticationService {

    @Inject
    private CredentialsManager credManager;

    @Inject
    @AuthenticatedEmployee
    Event<String> authEvent;

    private TokenBuilder tokenBuilder;

    /**
     * Constructor initializes TokenBuilder member
     */
    public AuthenticationService() {
        try {
            tokenBuilder = new TokenBuilder();
        } catch (NoSuchAlgorithmException e) {
            tokenBuilder = null;
            e.printStackTrace();
        }
    }

    /**
     * Method to authenticate user
     * @param c of user
     * @return response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials c) {
        try {
            authenticate(c.getUserName(), c.getPassword());
            authEvent.fire(c.getUserName());
            final String token = Hex.encodeHexString(tokenBuilder.encrypt(
                    c.getUserName() + c.getPassword()));
            return Response.ok(token).build();
        } catch (final Exception e) {
            return Response.status(Status.UNAUTHORIZED).
                    entity(e.getLocalizedMessage()).build();
        }
    }

    /**
     * Helper method for authentication
     * @param username of user
     * @param password of user
     * @throws AuthenticationException on authenticate failure
     * @throws SQLException on db access failure
     */
    private void authenticate(String username, String password)
            throws AuthenticationException, SQLException {
        Credentials c = credManager.getCredentialsByUsername(username);
        if (c == null) {
            throw new AuthenticationException("Cannot find user with username: "
                    + username + "!");
        }
        if (!tokenBuilder.validate(c.getPassword(), password)) {
            throw new AuthenticationException("Password is invalid!");
        }
    }
}
