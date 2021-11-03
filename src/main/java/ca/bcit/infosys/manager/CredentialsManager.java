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
    
    private static final long serialVersionUID = 14L;
    
    /**
     * Injected CredentialsDatabase.
     */
    @Inject
    private CredentialsDatabase credentialsDatabase;
    
    /**
     * Gets the credentials of an employee with a specified employee number.
     * @param empNumber The employee number.
     * @return The credentials of the employee with the specified employee number.
     */
    public Credentials getCredentialsByEmpNumber(int empNumber) {
        List<Credentials> credentialsList = credentialsDatabase.getCredentialsList();
        
        for(Credentials credentials : credentialsList) {
            if (credentials.getEmployeeNumber() == empNumber) {
                return credentials;
            }
        }
        return null;
    }
    
    /**
     * Adds a set of credentials to the CredentialsDatabase.
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    public void addCredentials(Credentials credentials) {
        List<Credentials> credentialsList = credentialsDatabase.getCredentialsList();
        credentialsList.add(credentials);
    }
}