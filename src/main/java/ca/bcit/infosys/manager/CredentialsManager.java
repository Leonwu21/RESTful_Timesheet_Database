package ca.bcit.infosys.manager;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import ca.bcit.infosys.authentication.TokenBuilder;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

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
    private TokenBuilder tokenBuilder;
    
    @Inject
    EmployeeManager employeeManager;
    
    /**
     * Datasource for the project
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_system")
    private DataSource dataSource;
    
    /**
     * Constructor initializes TokenBuilder
     */
    public CredentialsManager() {
        try {
            tokenBuilder = new TokenBuilder();
        } catch (final NoSuchAlgorithmException e) {
            tokenBuilder = null;
            e.printStackTrace();
        }
    }
    /**
     * Gets the credentials of an employee with a specified employee number.
     * @param empNumber The employee number.
     * @return The credentials of the employee with the specified employee number.
     */
    public Credentials getCredentialsByEmpNumber(int empNumber) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Credentials WHERE employeeNumber = ?");
                    stmt.setInt(1, empNumber);
                    ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Credentials(result.getInt("employeeNumber"),
                                result.getString("userName"),
                                result.getString("password"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getCredentialsByEmpNumber " + empNumber);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the credentials of an employee with a specified username.
     * @param username of the employee.
     * @return The credentials of the employee with the username
     */
    public Credentials getCredentialsByUsername(String username) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Credentials WHERE userName = ?");
                    stmt.setString(1, username);
                    ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Credentials(result.getInt("employeeNumber"),
                                result.getString("userName"),
                                result.getString("password"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Adds a set of credentials to the CredentialsDatabase AKA "Persist".
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    public void addCredentials(Credentials credentials) {
        // order of fields in INSERT statement
        final int employeeNumber = 1;
        final int userName = 2;
        final int password = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Credentials "
                            + "VALUES (?, ?, ?)");
                    stmt.setInt(employeeNumber, credentials.getEmployeeNumber());
                    stmt.setString(userName, credentials.getUserName());
                    stmt.setString(password, credentials.getPassword());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in addCredentials " + credentials);
            ex.printStackTrace();
        }
    }
    
    /**
     * Adds a set of credentials to the CredentialsDatabase AKA "Persist".
     * @param credentials The set of credentials to be added to the CredentialsDatabase.
     */
    public void addCredentialsToken(Credentials credentials) {
        // Must first add new employee with placeholder name due to foreign key constraints
        Employee e = new Employee(credentials.getEmployeeNumber(),
                "Placeholder", credentials.getUserName(), false);
        employeeManager.addEmployee(e);
        final int employeeNumber = 1;
        final int userName = 2;
        final int password = 3;
        final int tokenNum = 4;
        Connection connection = null;
        PreparedStatement stmt = null;
        byte[] token = tokenBuilder.encrypt(credentials.getUserName() +
                credentials.getPassword());
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Credentials "
                            + "VALUES (?, ?, ?, ?)");
                    stmt.setInt(employeeNumber, credentials.getEmployeeNumber());
                    stmt.setString(userName, credentials.getUserName());
                    stmt.setString(password, credentials.getPassword());
                    stmt.setBytes(tokenNum, token);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in addCredentials " + credentials);
            ex.printStackTrace();
        }
    }
    
    /**
     * Gets the list of Credentials.
     * @return The list of Credentials as an ArrayList.
     */
    public List<Credentials> getCredentialsList() {
        ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Credentials ORDER BY employeeNumber;");
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        credentialList.add(new Credentials(
                                result.getInt("employeeNumber"),
                                result.getString("userName"),
                                result.getString("password")));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getCredentialList");
            ex.printStackTrace();
            return null;
        }
        return credentialList;
    }
    
    /**
     * Deletes the credentials of interest from database AKA "Remove".
     * @param employee The employee's credentials to be deleted.
     */
    public void deleteCredentials(Employee employee) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("DELETE FROM "
                            + "Credentials WHERE employeeNumber = ?");
                    stmt.setInt(1, employee.getEmployeeNumber());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in deleteCredentials " + employee);
            ex.printStackTrace();
        }
    }
    
    /**
     * Updates an existing credential password in the Credentials table
     *
     * @param employee's password to be edited
     */
    public void editPassword(Employee employee, String pw) {
        final int password = 1;
        final int employeeNumber = 2;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Credentials "
                            + "SET password = ? WHERE employeeNumber = ?");
                    stmt.setString(password, pw);
                    stmt.setInt(employeeNumber, employee.getEmployeeNumber());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in editPassword " + employee);
            ex.printStackTrace();
        }
    }
    
    /**
     * Updates an existing credential userName in the Credentials table
     *
     * @param employee's userName to be edited
     */
    public void editUserName(Employee employee, String user) {
        final int username = 1;
        final int employeeNumber = 2;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Credentials "
                            + "SET userName = ? WHERE employeeNumber = ?");
                    stmt.setString(username, user);
                    stmt.setInt(employeeNumber, employee.getEmployeeNumber());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in editUserName " + employee);
            ex.printStackTrace();
        }
    }
    
    /**
     * Gets credentials by token string
     *
     * @param token string
     * @return Credentials of the employee
     * @throws SQLException
     */
    public Credentials getCredentialByToken(String token)
            throws SQLException, DecoderException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "Credentials WHERE token = ?");
                    stmt.setBytes(1, Hex.decodeHex(token));
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        final String password = Hex.encodeHexString(
                                result.getBytes("token"));
                        final Credentials c = new Credentials(
                                result.getString("userName"), password);
                        c.setEmployeeNumber(result.getInt("employeeNumber"));
                        return c;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return null;
    }
    
    /**
     * Updates an existing Credentials record in database
     *
     * @param credentials to be updated
     * @throws SQLException
     */
    public void merge(Credentials credentials, int id) throws SQLException {
        final int user = 1;
        final int pw = 2;
        final int num = 3;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Credentials "
                            + "SET userName=?, password=? " + "WHERE employeeNumber = ?");
                    stmt.setInt(num, id);
                    stmt.setString(user, credentials.getUserName());
                    stmt.setString(pw, credentials.getPassword());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}