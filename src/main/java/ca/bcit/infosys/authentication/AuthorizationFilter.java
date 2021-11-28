//package ca.bcit.infosys.authentication;
//
//import java.io.IOException;
//import java.lang.reflect.AnnotatedElement;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.annotation.Priority;
//import javax.inject.Inject;
//import javax.security.auth.message.AuthException;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.ResourceInfo;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//
//import ca.bcit.infosys.employee.Employee;
//import ca.bcit.infosys.manager.EmployeeManager;
//
//@Secured
//@Provider
//@Priority(Priorities.AUTHORIZATION)
//public class AuthorizationFilter implements ContainerRequestFilter {
//
//    @Context
//    private ResourceInfo resourceInfo;
//
//    @Inject
//    @AuthenticatedEmployee
//    private Employee authEmployee;
//    
//    @Inject
//    private EmployeeManager employeeManager;
//
//    /**
//     * Filters the incoming request, checks permissions and authorizes user if allowed
//     * @param req incoming request
//     */
//    @Override
//    public void filter(ContainerRequestContext req) throws IOException {
//        List<Permission> classPermissions = extractPermissions(resourceInfo.
//                getResourceClass());
//        List<Permission> methodPermissions = extractPermissions(resourceInfo.
//                getResourceMethod());
//        try {
//            if (methodPermissions.isEmpty()) checkPermissions(classPermissions);
//            else checkPermissions(methodPermissions);
//        } catch (Exception e) {
//            req.abortWith(Response.status(Response.Status.FORBIDDEN).build());
//        }
//    }
//
//    /**
//     * Extract the permissions from the annotated element
//     * @param ae annotated element
//     * @return list of permission objects
//     */
//    private List<Permission> extractPermissions(AnnotatedElement ae) {
//        if (ae == null) {
//            return new ArrayList<Permission>();
//        } else {
//            Secured secured = ae.getAnnotation(Secured.class);
//            if (secured == null) {
//                return new ArrayList<Permission>();
//            } else {
//                Permission[] allowed = secured.value();
//                return Arrays.asList(allowed);
//            }
//        }
//    }
//
//    /**
//     * Checks the permissions for a user and throws exception if not allowed
//     * @param allowed list of permissions
//     * @throws Exception if not allowed
//     */
//    private void checkPermissions(List<Permission> allowed) throws Exception {
//        if (!authEmployee.getIsAdmin()) {
//            final Employee admin = employeeManager.findAdmin();
//            if (admin != null && admin.getUserName() != null
//                    && admin.getUserName().equals(authEmployee.getUserName())) {
//                Permission p = Permission.ADMIN;
//                authEmployee.setPermission(p);
//            }
//        }
//        if (allowed.size() == 1 && allowed.get(0) == Permission.ADMIN) {
//            if (authEmployee.getPermission() != Permission.ADMIN) {
//                throw new AuthException("Do not have permission to access this resource!");
//            }
//        }
//    }
//}
