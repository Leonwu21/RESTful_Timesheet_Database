package ca.bcit.infosys.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.sql.DataSource;

import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Class to manage timesheet rows.
 * 
 * @author Benedict Halim and Leon Wu
 * @version 1.0
 */

@Named("timesheetRowManager")
@ConversationScoped
public class TimesheetRowManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Datasource for timesheet system
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_system")
    private DataSource dataSource;
    
    /**
     * Gets list of timesheet rows by timesheetId
     *
     * @param timesheetId of timesheet
     */
    public ArrayList<TimesheetRow> getTimesheetRows(Integer timesheetId) {
        final ArrayList<TimesheetRow> timesheetRows = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "TimesheetRows WHERE timesheetId = ? "
                            + "ORDER BY timesheetId");
                    stmt.setInt(1, timesheetId);
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        String hours = result.getString("totalWeekHours");
                        BigDecimal[] hoursBD = convertStringToBD(hours);
                        TimesheetRow newRow = new TimesheetRow(
                                result.getInt("projectId"),
                                result.getString("workPackageId"));
                        newRow.setNotes(result.getString("notes"));
                        newRow.setTotalWeekHours(hoursBD);
                        timesheetRows.add(newRow);
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
        return timesheetRows;
    }

    
    public BigDecimal[] convertStringToBD(String hours) {
        final String[] hoursString = hours.split(",");
        final BigDecimal[] hoursBD = new BigDecimal[hoursString.length];

        for (int i = 0; i < hoursBD.length; i++) {
            hoursBD[i] = new BigDecimal(hoursString[i]);
        }
        return hoursBD;
    }
    /**
     * Adds TimesheetRow object to a timesheet object in the database
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param timesheetRows specifies rows to be added
     */
    public void addRow(Integer timesheetId, List<TimesheetRow> timesheetRows) {
        if (!checkPK(timesheetRows)) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null,
                    new FacesMessage("Error: a timesheet row with same"
                            + " ProjectID and WorkPackageID exists."));
            return;
        }
        final int tsId = 1;
        final int pId = 2;
        final int wpId = 3;
        final int hours = 4;
        final int notes = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO "
                            + "TimesheetRows VALUES(?, ?, ?, ?, ?)");
                    for (TimesheetRow timesheetRow : timesheetRows) {
                        stmt.setInt(tsId, timesheetId);
                        stmt.setInt(pId, timesheetRow.getProjectId());
                        stmt.setString(wpId, timesheetRow.getWorkPackageId());
                        stmt.setString(hours, convertBDToString(timesheetRow.
                                getTotalWeekHours()));
                        stmt.setString(notes, timesheetRow.getNotes());
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();
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
        }
    }
    
    /**
     * Edits TimesheetRow object in database (update)
     *
     * @param timesheetId specifies timesheet to add rows to
     * @param timesheetRows specifies rows to be added
     */
    public void editRow(Integer timesheetId, List<TimesheetRow> timesheetRows) {
        if (!checkPK(timesheetRows)) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null,
                    new FacesMessage("Error: a timesheet row with same"
                            + " ProjectID and WorkPackageID exists."));
            return;
        }
        final int pId = 1;
        final int wpId = 2;
        final int hours = 3;
        final int notes = 4;
        final int tsId = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE TimesheetRows "
                            + "SET projectId=?, workPackageId=?, totalWeekHours"
                            + " =?, notes =? WHERE timesheetId = ?");
                    for (final TimesheetRow timesheetRow : timesheetRows) {
                        stmt.setInt(pId, timesheetRow.getProjectId());
                        stmt.setString(wpId, timesheetRow.getWorkPackageId());
                        stmt.setString(hours, convertBDToString(
                                timesheetRow.getTotalWeekHours()));
                        stmt.setString(notes, timesheetRow.getNotes());
                        stmt.setInt(tsId, timesheetId);
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();
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
        }
    }
    
    /**
     * Converts BigDecimal array format to String
     * @param hours array of BigDecimal objects
     * @return String representing BigDecimal array elements
     */
    public String convertBDToString(BigDecimal[] hours) {
        String s = "";
        for (int i = 0; i < hours.length; i++) {
            s += hours[i].toString();
            if (i != hours.length-1) s+= ",";
        }
        return s;
    }
    
    public boolean checkPK(List<TimesheetRow> timesheetRows) {
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean res = true;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM "
                            + "TimesheetRows");
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        for (TimesheetRow timesheetRow : timesheetRows) {
                            if (timesheetRow.getProjectId() ==
                                    result.getInt("projectId") &&
                                    timesheetRow.getWorkPackageId() ==
                                    result.getString("workPackageId")) {
                                res = false;
                            }
                        }
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
        }
        return res;
    }
}
