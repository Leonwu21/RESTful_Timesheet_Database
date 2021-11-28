package ca.bcit.infosys.authentication;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import ca.bcit.infosys.manager.EmployeeManager;
import ca.bcit.infosys.employee.Employee;

@RequestScoped
public class AuthenticatedEmployeeProducer {

    @Inject
    private EmployeeManager employeeManager;

    @Produces
    @RequestScoped
    @AuthenticatedEmployee
    private Employee authEmployee;

    /**
     * Initializes authenticated employee in database
     * @param username of employee
     */
    public void handleAuthenticationEvent(@Observes @AuthenticatedEmployee String username) {
        try {
            authEmployee = employeeManager.getEmployeeByUserName(username);
            authEmployee.setPermission(Permission.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
