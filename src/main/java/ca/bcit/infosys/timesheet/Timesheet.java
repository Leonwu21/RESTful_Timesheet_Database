package ca.bcit.infosys.timesheet;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import ca.bcit.infosys.employee.Employee;

/**
 * A class representing a single Timesheet.
 *
 * @author Bruce Link
 * @version 2.0
 */
public class Timesheet implements java.io.Serializable {

    /** Number of days in a week. */
    public static final int DAYS_IN_WEEK = 7;
    
    /** Number of hours in a day as double. */
    public static final double HOURS_IN_DAY = 24.0;
    
    /** Number of decihours in a day. */
    public static final int DECIHOURS_IN_DAY = 240;
    
    /** Number of work hours in week as double. */
    public static final double FULL_WORK_WEEK_HOURS = 40.0;

    /** Number of work hours in week as double. */
    public static final int FULL_WORK_WEEK_DECIHOURS = 400;

    /** Week fields of week ending on Friday. */
    public static final WeekFields FRIDAY_END 
            = WeekFields.of(DayOfWeek.SATURDAY, 1);

    /** Serial version number. */
    private static final long serialVersionUID = 4L;

    /** The user associated with this timesheet. */
    private Employee employee;
    
    /** The date of Friday for the week of the timesheet. */
    private LocalDate endDate;
    
    /** The List of all details (i.e. rows) that the form contains. */
    private List<TimesheetRow> details;
    
    /** The total number of overtime hours on the timesheet. Decihours. 
     *  Must be >= 0 */
    private int overtime;
    
    /** The total number of flextime hours on the timesheet. Decihours.
     *  Must be >= 0  */
    private int flextime;

    /** Primary key identifier. */
    private int timesheetId;

    /**
     * Constructor for Timesheet.
     * Initialize a Timesheet with no rows, no employee and
     * to the current date.
     */
    public Timesheet() {
        details = new ArrayList<TimesheetRow>();
        endDate = LocalDate.now().
                with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

    /**
     * Creates new timesheet with no rows.  Date is adjusted to Friday.
     * @param employee owner of timesheet
     * @param endDate date in timesheet week
     */
    public Timesheet(Employee employee, LocalDate endDate, int id) {
        details = new ArrayList<TimesheetRow>();
        this.employee = employee;
        this.endDate = endDate.
                with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        timesheetId = id;
    }
    
    /**
     * Creates a Timesheet object with all fields set.
     * Date is adjusted to Friday.
     * 
     * @param user The owner of the timesheet
     * @param endDate The date of the end of the week for this
     *                 timesheet (Friday)
     * @param details The detailed hours charged for the week for this 
     *        timesheet
     */
    public Timesheet(final Employee user, final LocalDate endDate,
            final List<TimesheetRow> details) {
        employee = user;
        this.endDate = endDate.
                with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        this.details = details;
    }

    /**
     * Getter for time sheet owner.
     * @return the employee.
     */
    public Employee getEmployee() {
        return employee;
    }
    
    /**
     * Setter for time sheet owner.
     * Allows user to be null.
     * @param user the employee for the timesheet.
     */
    public void setEmployee(final Employee user) {
        employee = user;
    }

    /**
     * Getter for timesheet's end of week date.
     * @return the endWeek
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    
    /**
     * Sets the timesheet end of week. Adjusted to be next or same Friday.
     * @param end the endWeek to set. 
     */
    public void setEndDate(final LocalDate end) {
        endDate = end.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

    /**
     * Calculate the week number of the timesheet.
     * @return the calculated week number
     */
    public int getWeekNumber() {
        return endDate.get(FRIDAY_END.weekOfWeekBasedYear());
    }

    /**
     * Sets the end of week based on the week number.
     *
     * @param weekNo the week number of the timesheet week
     * @param weekYear the year of the timesheet
     */
    public void setWeekNumber(final int weekNo, final int weekYear) {
        final LocalDate weekByNumber = 
                LocalDate.of(weekYear, 1, 1).
                  with(FRIDAY_END.weekOfWeekBasedYear(), weekNo);

        final TemporalAdjuster adjuster = 
                TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY);
        endDate = weekByNumber.with(adjuster);
    }

    /**
     * Calculate the time sheet's end date as a string.
     * @return the endWeek as string yyyy-mm-dd
     */
    public String getWeekEnding() {
        return endDate.toString();
    }

    /**
     * Getter for timesheet row details.
     * @return the details
     */
    public List<TimesheetRow> getDetails() {
        return details;
    }

    /**
     * Sets the details of the timesheet.
     *
     * @param newDetails new weekly charges to set
     */
    public void setDetails(final List<TimesheetRow> newDetails) {
        details = newDetails;
    }

    /**
     * Getter for overtime field, indicates number of decihours to
     * be paid for overtime for this week.
     * @return the overtime
     */
    public int getOvertimeDecihours() {
        return overtime;
    }

    /**
     * Setter for overtime field, indicates number of decihours to
     * be paid for overtime for this week.
     * @param ot the overtime to set
     * @throws IllegalArgumentException if ot < 0
     */
    public void setOvertime(final int ot) {
        if (ot < 0) {
            throw new IllegalArgumentException("must be >= 0");
        }
        overtime = ot;
    }

    /**
     * Setter for overtime field, indicates number of hours to
     * be paid for overtime for this week.
     * Rounded to one fractional digit.
     * @param ot the overtime to set
     * @throws IllegalArgumentException if ot < 0
     */
    public void setOvertime(final float ot) {
        if (ot < 0f) {
            throw new IllegalArgumentException("must be >= 0");
        }
        overtime = Math.round(ot * TimesheetRow.BASE10);
    }

    /**
     * Getter for overtime field, indicates number of hours to
     * be paid for overtime for this week.
     * @return the overtime as float
     */
    public float getOvertimeHours() {
        return overtime / TimesheetRow.BASE10;
    }

    /**
     * Getter for flextime field, indicates number of decihours to
     * save for flextime this week.
     * @return the flextime
     */
    public int getFlextimeDecihours() {
        return flextime;
    }

    /**
     * Setter for flextime field, indicates number of decihours to
     * save for flextime this week.
     * @param flex the flextime to set
     * @throws IllegalArgumentException if flex < 0
     */
    public void setFlextime(final int flex) {
        if (flex < 0) {
            throw new IllegalArgumentException("must be >= 0");
        }
        flextime = flex;
    }

    /**
     * Getter for flextime field, indicates number of hours to
     * save for flextime this week.
     * @return the flextime
     */
    public float getFlextimeHours() {
        return flextime / TimesheetRow.BASE10;
    }

    /**
     * Setter for flextime field, indicates number of hours to
     * save for flextime this week.
     * rounded to one fractional digit.
     * @param flex the float flextime value to set
     * @throws IllegalArgumentException if flex < 0
     */
    public void setFlextime(final float flex) {
        if (flex < 0f) {
            throw new IllegalArgumentException("must be >= 0");
        }
        flextime =  Math.round(flex * TimesheetRow.BASE10);
    }

    /**
     * Gets the timesheet's total hours.
     *
     * @return The timesheet's total hours.
     */
    public BigDecimal getTotalHours() {
        BigDecimal timesheetHours = BigDecimal.ZERO;
        for (TimesheetRow row : details) {
            BigDecimal rowHours = row.getSum();
            timesheetHours = timesheetHours.add(rowHours);
        }
        return timesheetHours;
    }

    /**
     * Calculates the total decihours.
     *
     * @return total decihours for timesheet.
     */
    public int getTotalDecihours() {
        int sum = 0;
        for (TimesheetRow row : details) {
            sum = sum + row.getDeciSum();
        }
        return sum;
    }

    /**
     * Calculates the daily total hours.
     *
     * @return array of total hours for each day of week for timesheet.
     */
    public float[] getDailyHours() {
        int[] deciSums = new int[DAYS_IN_WEEK];
        float[] sums = new float[DAYS_IN_WEEK];
        for (TimesheetRow day : details) {
            int[] hours = day.getDecihours();
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                deciSums[i] += hours[i];
            }
        }
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            sums[i] = deciSums[i] / TimesheetRow.BASE10;
        }
        return sums;
    }

    /**
     * Calculates the daily total decihours.
     *
     * @return array of total hours for each day of week for timesheet.
     */
    public int[] getDailyDecihours() {
        int[] deciSums = new int[DAYS_IN_WEEK];
        for (TimesheetRow day : details) {
            int[] hours = day.getDecihours();
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                deciSums[i] += hours[i];
            }
        }
        return deciSums;
    }

    /**
     * Checks to see if timesheet total nets 40 hours.
     * @return true if FULL_WORK_WEEK == hours -flextime - overtime
     *     and at most one of flex time and overtime is non zero
     */
    public boolean isValid() {
        long total = getTotalDecihours();

        return (overtime == 0 || flextime == 0)
             && (total - overtime - flextime == FULL_WORK_WEEK_DECIHOURS);
    }

    /**
     * Deletes the specified row from the timesheet.
     *
     * @param rowToRemove
     *            the row to remove from the timesheet.
     */
    public void deleteRow(final TimesheetRow rowToRemove) {
        details.remove(rowToRemove);
    }

    /**
     * Add an empty row to to the end of the timesheet details.
     */
    public void addRow() {
        details.add(new TimesheetRow());
    }
    
    /**
     * Returns employee parameters as String representation
     */
    @Override
    public String toString() {
        String result = employee.toString() + '\t' + endDate + '\t' 
                + overtime + '\t' + flextime;
        for (TimesheetRow tsr : details) {
            result += '\n' + tsr.toString();
        }
        return result;
    }

    /**
     * Gets timesheetId primary key
     * @return timesheetId
     */
    public int getTimesheetId() {
        return timesheetId;
    }

    /**
     * Sets timesheetId primary key
     * @param timesheetId id to set
     */
    public void setTimesheetId(int timesheetId) {
        this.timesheetId = timesheetId;
    }

}
