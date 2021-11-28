/**
 *
 */
package ca.bcit.infosys.authentication;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import ca.bcit.infosys.manager.CredentialsManager;
import ca.bcit.infosys.employee.Credentials;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String SCHEME = "Bearer";
    private static final String REALM = "test";

    @Inject
    private CredentialsManager credManager;
    
    @Inject
    @AuthenticatedEmployee
    private Event<String> authEvent;

    /**
     * Extracts and validates authorization header and token
     * req incoming request
     */
    @Override
    public void filter(ContainerRequestContext req) throws IOException {
        // Gets header and validates
        final String header = req.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (!isTokenBasedAuthentication(header)) {
            abortUnauthorized(req);
            return;
        }

        // Gets and validates token
        final String token = header.substring(SCHEME.length()).trim();
        try {
            validateToken(token);
        } catch (final Exception e) {
            abortUnauthorized(req);
        }
    }

    /**
     * Helper method to validate header token
     * @param authorizationHeader
     * @return
     */
    private boolean isTokenBasedAuthentication(String header) {
        // Header must not be null and must be prefixed with "Bearer "
        return header != null && header.toLowerCase().
                startsWith(SCHEME.toLowerCase() + " ");
    }

    /**
     * Helper method to abort if unauthorized with 401 response.
     * @param req
     */
    private void abortUnauthorized(ContainerRequestContext req) {
        req.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, SCHEME +
                        " realm=\"" + REALM + "\"").build());
    }

    /**
     * Helper method to validate token, throws exception if invalid
     * @param token to be validated
     * @throws Exception if invalid token
     */
    private void validateToken(String token) throws Exception {
        final Credentials c = credManager.getCredentialByToken(token);
        if (c == null) {
            throw new AuthenticationException();
        }
        authEvent.fire(c.getUserName());
    }
}