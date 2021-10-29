package ca.bcit.infosys.database;

import java.io.Serializable;
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
public class CredentialsDatabase implements Serializable {
	
    private static final long serialVersionUID = 6L;
    
    /**
     * A list of all the credentials.
     */
    //TODO: Implement a setter.
    private final List<Credentials> credentialList;

    /**
     * Constructor for CredentialsDatabase.
     */
    //TODO: Edit default credentials to something else.
    public CredentialsDatabase() {
        credentialList = new ArrayList<Credentials>();
        credentialList.add(new Credentials("bdlink", "password"));
        credentialList.get(0).setEmpNumber(1234);
    }

    /**
     * Gets the list of credentials.
     * 
     * @return The list of credentials.
     */
    public List<Credentials> getAllCredentials() {
        return credentialList;
    }

}