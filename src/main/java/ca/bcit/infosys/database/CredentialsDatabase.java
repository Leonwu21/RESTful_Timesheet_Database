package ca.bcit.infosys.database;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;

/**
 * The class for the database of credentials.
 *
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("credentialsDatabase")
@ApplicationScoped
public class CredentialsDatabase {
    
    /**
     * A list of all the credentials.
     */
    private final List<Credentials> credentialList;

    /**
     * Constructor for CredentialsDatabase.
     */
    public CredentialsDatabase() {
        credentialList = new ArrayList<Credentials>();
        Credentials credentials = new Credentials("admin", "password");
        credentialList.add(credentials);
        credentialList.get(0).setEmployeeNumber(1111);
    }

    /**
     * Gets the list of credentials.
     * 
     * @return The list of credentials.
     */
    public List<Credentials> getCredentialsList() {
        return credentialList;
    }

}