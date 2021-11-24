package ca.bcit.infosys.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class TimesheetApplication extends Application {
    private final Set<Object> singletons = new HashSet<Object>();
    
    public TimesheetApplication() {
        
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
