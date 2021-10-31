package ca.bcit.infosys.timesheet;

import java.util.Arrays;

/**
 * A class representing a single row of a Timesheet.
 *
 * @author Bruce Link
 * @version 2.0
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

    /** decimal base in float. */
    public static final float BASE10 = 10.0F;
    
    /** Day number for Friday. */
    public static final int FIRST_DAY = SAT;

    /** Day number for Friday. */
    public static final int LAST_DAY = FRI;

    /** Version number. */
    private static final long serialVersionUID = 18L;

    /** mask for packing, unpacking hours. */
    private static final long[] MASK = {0xFFL, 
                                        0xFF00L, 
                                        0xFF0000L, 
                                        0xFF000000L,
                                        0xFF00000000L, 
                                        0xFF0000000000L, 
                                        0xFF000000000000L};
    
    /** mask for packing, unpacking hours. */
    private static final long[] UMASK = {0xFFFFFFFFFFFFFF00L, 
                                         0xFFFFFFFFFFFF00FFL, 
                                         0xFFFFFFFFFF00FFFFL, 
                                         0xFFFFFFFF00FFFFFFL,
                                         0xFFFFFF00FFFFFFFFL, 
                                         0xFFFF00FFFFFFFFFFL, 
                                         0xFF00FFFFFFFFFFFFL};
    
    /** 2**8. */
    private static final long BYTE_BASE = 256;
    
    /** max number of deci-hours per day. */
    private static final int DECI_MAX = 240;
    
    /** number of bits in a byte. */
    private static final int BITS_PER_BYTE = 8;

    /** The projectId. */
    private int projectId;
    
    /** The WorkPackageId. Must be a unique for a given projectId. */
    private String workPackageId;

    /** hours for the week, packed into a long.
     * Each of the low-order 7 bytes is the decihours for one day.
     * Order is 00-FR-TH-WE-TU-MO-SU-SA
     * Counting days d = 0 .. 6 (starting at Saturday) the position of 
     * a day's byte is shifted d * 8 bits to the left.
     */
    private long packedHours;

    /** Any notes added to the timesheet row. */
    private String notes;

    /** create empty timesheetRow to be modified later.*/
    public TimesheetRow() {
    }

    /**
     * Initialize timesheet row with instance data, no hours charged.
     * @param projectId project number
     * @param workPackageId work package id
     */
    public TimesheetRow(int projectId, String workPackageId) {
        this.projectId = projectId;
        this.workPackageId = workPackageId;
    }
    
    /**
     * Initialize timesheet row with instance data, hours charged (in order of
     * Saturday, .. Friday.
     * @param projectId project number
     * @param workPackageId work package id
     * @param notes is a field for comments for this row
     * @param hours the charges for each day of the week.  There must be 7, or 
     *        else an array with 7 hours passed.
     */
    public TimesheetRow(int projectId, String workPackageId, String notes, 
            float...hours) {
        if (hours.length != Timesheet.DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Wrong number of hours");
        }
        setHours(hours);
        this.projectId = projectId;
        this.workPackageId = workPackageId;
        this.notes = notes;
    }
    
    /**
     * convert hour to decihour.  hour rounded to one fractional decimal place.
     * @param hour as float
     * @return equivalent number of decihours as int
     */
    public static int toDecihour(float hour) {
        return Math.round(hour * BASE10);
    }
    
    /**
     * convert decihour to hour.  
     * @param decihour as int
     * @return equivalent number of hours as float
     */
    public static float toHour(int decihour) {
        return decihour / BASE10;
    }
    

    /**
     * projectId getter.
     * @return the projectId
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * projectId setter.
     * @param id the projectId to set, must be >= 0
     */
    public void setProjectId(final int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ProjectId must be >= 0");
        }
        this.projectId = id;
    }

    /**
     * workPackageId getter.
     * @return the workPackageId
     */
    public String getWorkPackageId() {
        return workPackageId;
    }

    /**
     * workPackageId setter.
     * @param wp the workPackageId to set
     */
    public void setWorkPackageId(final String wp) {
        this.workPackageId = wp;
    }

    /**
     * packedHours getter.
     * @return the hours charged for each day, packed into a long.
     */
    public long getPackedHours() {
        return packedHours;
    }

    /**
     * packedHours setter.
     * @param packedHours the hours charged for each day, packed into a long
     */
    public void setPackedHours(final long packedHours) {
        checkHoursForWeek(packedHours);
        this.packedHours = packedHours;
    }

    /**
     * Extract the hours for a given day.
     * @param d the day number (0 = Saturday .. 6 = Friday)
     * @return hours for that day
     */
    public float getHour(int d) {
        return toHour(getDecihour(d));
    }
    
    /**
     * Set the hours for a given day. Rounded to one decimal.
     * @param d the day number (0 = Saturday .. 6 = Friday)
     * @param charge hours charged for that day
     * @throws IllegalArgumentException if charge < 0 or > 24
     */
    public void setHour(int d, float charge) {
        if (charge < 0.0 || charge > Timesheet.HOURS_IN_DAY) {
            throw new IllegalArgumentException("Charge is out of range");
        }
        setDecihour(d, toDecihour(charge));
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
     * @param notes the notes to set
     */
    public void setNotes(final String notes) {
        this.notes = notes;
    }

    /**
     * adds total hours for this timesheet row.
     * @return the weekly hours
     */
    public Float getSum() {
        return toHour(getDeciSum());
    }
    
    /**
     * Adds total hours for this timesheet row.
     * @return total hours in units of decihours
     */
    public int getDeciSum() {
        int[] charges = getDecihours();
        int sum = 0;
        for (int charge: charges) {
            sum += charge;
        }
        return sum;
    }
    
    /**
     * Extract the integer hours * 10 for a given day.
     * @param d the day number (0 = Saturday .. 6 = Friday)
     * @return hours for that day
     */
    public int getDecihour(int d) {
        if (d < FIRST_DAY || d > LAST_DAY) {
            throw new IllegalArgumentException("day number out of range");
        }
        return (int) ((packedHours & MASK[d]) >> d * BITS_PER_BYTE);
    }
    
    /**
     * Set the integer hours * 10 for a given day.
     * @param d the day number (0 = Saturday .. 6 = Friday)
     * @param charge hours charged for that day
     * @throws IllegalArgumentException if invalid day or charge
     */
    public void setDecihour(int d, int charge) {
        if (d < FIRST_DAY || d > LAST_DAY) {
            throw new IllegalArgumentException("day number out of range, " 
                        + "must be in 0 .. 6");
        }
        if (charge < 0 || charge > DECI_MAX) {
            throw new IllegalArgumentException("charge out of range, " 
                        + "must be 0 .. 240");
        }
        packedHours = packedHours & UMASK[d]
                | (long) charge << (d * BITS_PER_BYTE);
    }
    
    /**
     * Get hours array of charges, index is day number.
     * @return hours as array of charges
     */
    public float[] getHours() {
        float[] result = new float[LAST_DAY + 1];
        long check = packedHours;
        for (int i = FIRST_DAY; i <= LAST_DAY; i++) {
            result[i] = check % BYTE_BASE / BASE10;
            check /= BYTE_BASE;
        }
        return result;
    }
    
    /**
     *  Convert hours array to packed hours and store in hours field.
     *  Index of array is day of week number, starting with Saturday
     * @param charges array of hours to pack (single fractional digit)
     * @throws IllegalArgumentException if charges < 0 or > 24
     */
    public void setHours(float[] charges) {
        for (float charge : charges) {
            if (charge < 0.0 || charge > Timesheet.HOURS_IN_DAY) {
                throw new IllegalArgumentException("charge is out of " 
                            + "maximum hours in day range");           
            }
        }
        long result = 0;
        for (int i = LAST_DAY; i >= FIRST_DAY; i--) {
            result = result * BYTE_BASE + toDecihour(charges[i]);
        }
        packedHours = result;
    }
    
    /**
     * Get hours array of charges, index is day number.
     * @return hours as array of charges
     */
    public int[] getDecihours() {
        int[] result = new int[LAST_DAY + 1];
        long check = packedHours;
        for (int i = FIRST_DAY; i <= LAST_DAY; i++) {
            result[i] = (int) (check % BYTE_BASE);
            check /= BYTE_BASE;
        }
        return result;
    }
    
    /**
     *  Convert hours array to packed hours and store in hours field.
     *  Index of array is day of week number, starting with Saturday
     * @param charges array of hours to pack (single fractional digit)
     * @throws IllegalArgumentException if charges < 0 or > 24
     */
    public void setDecihours(int[] charges) {
        for (float charge : charges) {
            if (charge < 0 || charge > Timesheet.DECIHOURS_IN_DAY) {
                throw new IllegalArgumentException("charge is out of " 
                            + "maximum hours in day range");           
            }
        }
        long result = 0;
        for (int i = LAST_DAY; i >= FIRST_DAY; i--) {
            result = result * BYTE_BASE + charges[i];
        }
        packedHours = result;
    }
    
    /* throw IllegalArgumentException if an hour is out of range */
    private void checkHoursForWeek(final long packedDecihours) {
        if (packedDecihours < 0) {
            throw new IllegalArgumentException(
                    "improperly formed packedHours < 0");
        }    
        long check = packedDecihours;
        for (int i = FIRST_DAY; i <= LAST_DAY; i++) {
            if (check % BYTE_BASE > Timesheet.DECIHOURS_IN_DAY) {
                throw new IllegalArgumentException(
                        "improperly formed packedHours");
            }
            check /= BYTE_BASE;
        }
        //top byte must be zero
        if (check > 0) {
            throw new IllegalArgumentException(
                    "improperly formed packedHours");
        }

    }
    
    @Override
    public String toString() {
        return projectId + " " + workPackageId + " "
                + Arrays.toString(getHours());
    }
    

}
