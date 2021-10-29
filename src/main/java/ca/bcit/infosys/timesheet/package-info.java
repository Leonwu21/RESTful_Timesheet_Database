/**
 *  Package containing classes concerning an timesheet.
 *    Timesheet can be extended to allow CDI or JPA functionality.
 *    
 *    Timesheet has TimesheetRow's to represent the detailed entries for a given
 *    project/workpackage.
 *    
 *    To allow CDI and JPA to function, methods violate Checkstyle constraint
 *    "Design for Extendion" which should be turned off

 *    TimesheetCollection provides an interface that can be implemented with
 *    or without a database.
 *    
 */
package ca.bcit.infosys.timesheet;
