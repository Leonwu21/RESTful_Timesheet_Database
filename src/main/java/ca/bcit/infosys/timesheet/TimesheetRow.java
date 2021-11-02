package ca.bcit.infosys.timesheet;

import java.math.BigDecimal;

/**
 * A class representing a single row of a Timesheet.
 *
 * @author Bruce Link
 * @version 1.1
 */

public class TimesheetRow implements java.io.Serializable {

    /** Timesheet row index for Saturday. */
    public static final int SAT = 0;
    /** Timesheet row index for Sunday. */
    public static final int SUN = 1;
    /** Timesheet row index for Monday. */
    public static final int MON = 2;
    /** Timesheet row index for Tuesday. */
    public static final int TUE = 3;
    /** Timesheet row index for Wednesday. */
    public static final int WED = 4;
    /** Timesheet row index for Thursday. */
    public static final int THU = 5;
    /** Timesheet row index for Friday. */
    public static final int FRI = 6;

    /** Version number. */
    private static final long serialVersionUID = 2L;

    /** The projectID. */
    private int projectID;
    /** The WorkPackage. Must be a unique for a given projectID. */
    private String workPackage;

    /**
     * An array holding all the hours charged for each day of the week. Day 0 is
     * Saturday, ... day 6 is Friday
     */
    private BigDecimal[] hoursForWeek = new BigDecimal[Timesheet.DAYS_IN_WEEK];
    /** Any notes added to the end of a row. */
    private String notes;

    /**
     * Creates a TimesheetDetails object and sets the editable state to true.
     */
    public TimesheetRow() {
    }

    /**
     * Creates a TimesheetDetails object with all fields set. Used to create
     * sample data.
     * @param id project id
     * @param wp work package number (alphanumeric)
     * @param hours number of hours charged for each day of week.
     *      null represents ZERO
     * @param comments any notes with respect to this work package charges
     */
    public TimesheetRow(final int id, final String wp,
            final BigDecimal[] hours, final String comments) {
        setProjectID(id);
        setWorkPackage(wp);
        setHoursForWeek(hours);
        setNotes(comments);
    }

    /**
     * projectID getter.
     * @return the projectID
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * projectID setter.
     * @param id the projectID to set
     */
    public void setProjectID(final int id) {
        this.projectID = id;
    }

    /**
     * workPackage getter.
     * @return the workPackage
     */
    public String getWorkPackage() {
        return workPackage;
    }

    /**
     * workPackage setter.
     * @param wp the workPackage to set
     */
    public void setWorkPackage(final String wp) {
        this.workPackage = wp;
    }

    /**
     * hoursForWeek getter.
     * @return the hours charged for each day
     */
    public BigDecimal[] getHoursForWeek() {
        return hoursForWeek;
    }

    /**
     * hoursForWeek setter.
     * @param hours the hours charged for each day
     */
    public void setHoursForWeek(final BigDecimal[] hours) {
        checkHoursForWeek(hours);
        this.hoursForWeek = hours;
    }

    /**
     * gets hour for a give day of the week.
     * @param day The day of week to return charges for
     * @return charges in hours of specific day in week
     */
    public BigDecimal getHour(final int day) {
        return hoursForWeek[day];
    }

    /**
     * sets hour for a given day of the week.
    * @param day The day of week to set the hour
    * @param hour The number of hours worked for that day
    */
   public void setHour(final int day, final BigDecimal hour) {
       checkHour(hour);
       hoursForWeek[day] = hour;
   }
   /**
    * sets hour for a given day.
   * @param day The day of week to set the hour
   * @param hour The number of hours worked for that day
   */
  public void setHour(final int day, final double hour) {
      BigDecimal bdHour = null;
      if (hour != 0.0) {
          bdHour = new BigDecimal(hour).setScale(1, BigDecimal.ROUND_HALF_UP);
      }
      checkHour(bdHour);
      hoursForWeek[day] = bdHour;
  }

    /**
     * Checks if hour value is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     *@param hour the value to check
     */
    private void checkHour(final BigDecimal hour) {
        if (hour != null) {
            if (hour.compareTo(Timesheet.HOURS_IN_DAY) > 0.0
                    || hour.compareTo(BigDecimal.ZERO) < 0.0) {
                throw new IllegalArgumentException(
                       "out of range: should be between 0 and 24");
            }
            if (hour.scale() > 1) {
                throw new IllegalArgumentException(
                        "too many decimal digits: should be at most 1");
            }
        }
    }

    /**
     * Checks if any hour value in any day of the week is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     * @param hours array of hours charged for each day in a week
     */
    private void checkHoursForWeek(final BigDecimal[] hours) {
        if (hours.length != Timesheet.DAYS_IN_WEEK) {
            throw new IllegalArgumentException(
                    "wrong week length: should be 7");
        }
        for (BigDecimal next : hours) {
            checkHour(next);
        }
    }

    /**
     * getter for notes section.
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * setter for notes section.
     * @param comments the notes to set
     */
    public void setNotes(final String comments) {
        this.notes = comments;
    }

    /**
     * adds total hours for this timesheet row.
     * @return the weekly hours
     */
    public BigDecimal getSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal next : hoursForWeek) {
            if (next != null) {
                sum = sum.add(next);
            }
        }
        return sum;
    }

}
