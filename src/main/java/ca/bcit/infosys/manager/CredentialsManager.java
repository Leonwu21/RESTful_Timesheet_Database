package ca.bcit.infosys.manager;

import java.io.Serializable;

import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.database.CredentialsDatabase;
import ca.bcit.infosys.employee.Credentials;

/**
 * Class to manage credentials.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */
@Named("credentialsManager")
@ConversationScoped
public class CredentialsManager implements Serializable {
    
    //TODO: Why do we need this? Does it need to be this number?
    private static final long serialVersionUID = -6478292740340769939L;
    
    /**
     * Injected CredentialsDatabase.
     */
    @Inject
    private CredentialsDatabase credentialsDatabase;
    
    /**
     * Gets the credentials of an employee that has the employee number given.
     * @param empNumber The employee's employee number.
     * @return The credentials of the employee with the employee number given.
     */
    public Credentials getCredentialsByEmpNumber(int empNumber) {
        List<Credentials> credentialsList = credentialsDatabase.getAllCredentials();
        
        for(Credentials credentials : credentialsList) {
            if (credentials.getEmpNumber() == empNumber) {
                return credentials;
            }
        }
        return null;
    }
    
    /**
     * Adds a set of credentials to the CredentialsDatabase.
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    public void add(Credentials credentials) {
        credentialsDatabase.getAllCredentials().add(credentials);
    }
    
}
