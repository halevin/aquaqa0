/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/

package alma.obops.aqua.qa0.utils;

import alma.obops.utils.DateUtilsLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A collection of utilitites and constants for this package.
 * 
 * @author amchavan, Feb 19, 2007
 */

// This was taken from ICD/OBOPS/ObopsUtils/src/alma/obops/utils/DatetimeUtils.java
// One day we'll mavenize the lot, for the time being I'm just copying this file over
// amchavan, 11-Jul-2014

public class DatetimecoordinateUtils {

    /** Styles to format an ISO date/time string */
    public enum Style { 
        /** See {@link #ISOTIMEDATESTRING_SHORTER} */
        SHORTER, 
        
        /** See {@link #ISOTIMEDATESTRING_SHORT} */
        SHORT, 

        /** See {@link #ISOTIMEDATESTRING_MEDIUM} */
        MEDIUM, 
        
        /** See {@link #ISOTIMEDATESTRING_LONG} */
        LONG,
        
        /** See {@link #DATESTRING} */
        DATE,
        
        /** See {@link #TIMESTRING} */
        TIME,

        /** See {@link #TIMESTRING_LONG} */
        TIME_LONG,

        /** See {@link #TIMESTRING_SHORT} */
        TIME_SHORT,

        /** See {@link #TIMESTRING_SHORTER} */
        TIME_SHORTER
    }

    /** All shift log entries are in Universal Time (UT) */
    public static final TimeZone UT = TimeZone.getTimeZone( "Etc/GMT" );

    /**
     * The full ISO format for date/time strings, including milliseconds and
     * timezone.
     */
    public static final String ISOTIMEDATESTRING_LONG = 
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    
    /**
     * ISO format for date/time strings, including milliseconds but no timezone.
     */
    public static final String ISOTIMEDATESTRING_MEDIUM = 
        "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**
     * Shorter ISO format for date/time strings: no milliseconds nor time zone.
     */
    public static final String ISOTIMEDATESTRING_SHORT = 
        "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Human-readable format for timestamps, e.g. 
     * <code>06-May-2014&nbsp;16:27:48</code>
     */
    public static final String SIMPLE_DATETIME = 
        "dd-MMM-yyyy HH:mm:ss";

    /**
     * Shorter ISO format for date/time strings: no seconds
     */
    public static final String ISOTIMEDATESTRING_SHORTER = 
        "yyyy-MM-dd'T'HH:mm";

    /**
     * ISO-like format for date strings (no time)
     */
    public static final String DATESTRING = "yyyy-MM-dd";

    /**
     * ISO-like format for time strings (no date)
     */
    public static final String TIMESTRING = "HH:mm:ss";

    /**
     * ISO-like format for time strings, with milliseconds
     */
    public static final String TIMESTRING_LONG = "HH:mm:ss.SSS";

    /**
     * ISO-like format for time strings (no seconds either)
     */
    public static final String TIMESTRING_SHORT = "HH:mm";

    /**
     * ISO-like format for time strings (only hours)
     */
    public static final String TIMESTRING_SHORTER = "HH";

    /**
     * Parses a partial date-time string.
     * <P>
     * 
     * The input string is parsed using a subset of the ISO representation, and
     * it is allowed to include less then the entire ISO string; missing
     * information is supplied by the system: year and month are assumed to be
     * the same as "today", with hours, minutes and seconds being zero. <br>
     * For more info, see <code>completeISODateString( String, String )</code>
     * 
     * @param dt_in
     *                Incomplete string to parse.
     * 
     * @throws ParseException
     * 
     * @see #completeIsoDateString(String, String)
     */
    public static String completeIsoDateString( String dt_in )
        throws ParseException {

        String now = getCurrentIsoDateTime();
        int idx = now.indexOf( 'T' );
        now = now.substring( 0, idx + 1 ) + "00:00:00";
        return completeIsoDateString( dt_in, now );
    }

    /**
     * Completes a partial date-time string.
     * <P>
     * The input string is parsed using a subset of the ISO representation, and
     * it is allowed to include less then the entire ISO string; missing
     * information is supplied by parameter <code>defDT</code>. The minimum
     * piece of information to be provided is the day number. The following
     * table gives a few examples of how partial ISO date/time strings are
     * completed, assuming <code>defDT</code> is
     * <code>1997-08-01T00:00:00</code>. <BR>
     * Notice that:
     * <UL>
     * <LI> blanks surrounding the partial strings are ignored, and are shown
     * here only for clarity;
     * <LI> one-digit numbers can, but need not, be prefixed by a zero ("2" is
     * the same as "02");
     * <LI> the "T" char is only needed when a time different from midnight
     * (00:00:00) needs to be specified;
     * <LI> partial year specification (like "98") is not allowed;
     * <LI> textual month specification (like "Dec") is not allowed;
     * <LI> only years between 1970 and 2037 are supported.
     * </UL>
     * 
     * <PRE>
     *             07              1997-08-07T00:00:00
     *          10-07              1997-10-07T00:00:00
     *     1998-2-1                1998-02-01T00:00:00
     *     1998-01-01              1998-01-01T00:00:00
     *             15T05           1997-08-15T05:00:00
     *             15T05:30        1997-08-15T05:30:00
     *             20T5:5:30       1997-08-20T05:05:30
     *          10-16T21:30        1997-10-16T21:30:00
     *     1999-12-31T23:59:59.99  1999-12-31T23:59:59.99
     *     98-2-1                  ERROR: partial year specification
     *     1969-12-31              ERROR: only years 1970-2037 are supported
     *     1999-12-31T23:59:61     ERROR: invalid number of seconds
     *     0001-55-55T66:66:66     ERROR: invalid time specification
     *     2001-Oct-09             ERROR: textual month specification is not allowed
     * </PRE>
     * 
     * @param inDT
     *                Incomplete string to parse.
     * @param defDT
     *                A full ISO date+time string formatted according to
     *                {@link #ISOTIMEDATESTRING_MEDIUM}; this is where default
     *                information is taken from.<BR>
     *                For instance: <code>2001-10-09T10:52:30.00</code><BR>
     *                This parameter is not checked for integrity.
     * 
     * @return A complete ISO date+time string formatted according to
     *         {@link #ISOTIMEDATESTRING_MEDIUM}.
     * 
     * @throws ParseException
     * @throws NumberFormatException
     * 
     */
    public static String completeIsoDateString( String inDT, String defDT )
        throws ParseException, NumberFormatException {
    	return completeIsoDateString(inDT, defDT, Style.MEDIUM);
    }

    /**
     * Completes a partial date-time string.
     * <P>
     * The input string is parsed using a subset of the ISO representation, and
     * it is allowed to include less then the entire ISO string; missing
     * information is supplied by parameter <code>defDT</code>. The minimum
     * piece of information to be provided is the day number. The following
     * table gives a few examples of how partial ISO date/time strings are
     * completed, assuming <code>defDT</code> is
     * <code>1997-08-01T00:00:00</code>. <BR>
     * Notice that:
     * <UL>
     * <LI> blanks surrounding the partial strings are ignored, and are shown
     * here only for clarity;
     * <LI> one-digit numbers can, but need not, be prefixed by a zero ("2" is
     * the same as "02");
     * <LI> the "T" char is only needed when a time different from midnight
     * (00:00:00) needs to be specified;
     * <LI> partial year specification (like "98") is not allowed;
     * <LI> textual month specification (like "Dec") is not allowed;
     * <LI> only years between 1970 and 2037 are supported.
     * </UL>
     * 
     * <PRE>
     *             07              1997-08-07T00:00:00
     *          10-07              1997-10-07T00:00:00
     *     1998-2-1                1998-02-01T00:00:00
     *     1998-01-01              1998-01-01T00:00:00
     *             15T05           1997-08-15T05:00:00
     *             15T05:30        1997-08-15T05:30:00
     *             20T5:5:30       1997-08-20T05:05:30
     *          10-16T21:30        1997-10-16T21:30:00
     *     1999-12-31T23:59:59.99  1999-12-31T23:59:59.99
     *     98-2-1                  ERROR: partial year specification
     *     1969-12-31              ERROR: only years 1970-2037 are supported
     *     1999-12-31T23:59:61     ERROR: invalid number of seconds
     *     0001-55-55T66:66:66     ERROR: invalid time specification
     *     2001-Oct-09             ERROR: textual month specification is not allowed
     * </PRE>
     * 
     * @param inDT
     *                Incomplete string to parse.
     * @param defDT
     *                A full ISO date+time string formatted according to
     *                {@link #ISOTIMEDATESTRING_MEDIUM}; this is where default
     *                information is taken from.<BR>
     *                For instance: <code>2001-10-09T10:52:30.00</code><BR>
     *                This parameter is not checked for integrity.
     * 
     * @return A complete ISO date+time string formatted according to
     *         {@link #ISOTIMEDATESTRING_MEDIUM}.
     * 
     * @throws ParseException
     * @throws NumberFormatException
     * 
     */
    public static String completeIsoDateString( String inDT, String defDT, Style style )
        throws ParseException, NumberFormatException {

        int[] inElements = parseIsoDateTime( inDT );
        int[] defElements = parseIsoDateTime( defDT );
        StringBuffer complete = new StringBuffer();
        String ret;

        // Provide default elements
        // ----------------------------------------
        for( int i = 0; i < inElements.length; i++ )
            if( inElements[i] == 0 )
                inElements[i] = defElements[i];

        // Build final string
        // ----------------------------------------
        complete.append( String.format( "%04d", inElements[0] ) ).append( '-' )
                .append( String.format( "%02d", inElements[1] ) ).append( '-' )
                .append( String.format( "%02d", inElements[2] ) ).append( 'T' )
                .append( String.format( "%02d", inElements[3] ) ).append( ':' )
                .append( String.format( "%02d", inElements[4] ) ).append( ':' )
                .append( String.format( "%02d", inElements[5] ) ).append( '.' )
                .append( String.format( "%03d", inElements[6] ) );

        ret = complete.toString();

        // Now verify if it makes any sense at all
        // ----------------------------------------
        SimpleDateFormat iso = getIsoDateFormat();
        iso.setLenient( false );
        Date d = iso.parse( ret );
        if( d == null )
            throw new ParseException( ret, 0 );

        // All is OK, return (Style.MEDIUM by default)
        if( style == null )
        	return ret;
        return formatAsIso(d, style);
    }

    /**
     * Format a date according to the ISO conventions and timezone UT (see
     * {@link #UT }.
     * 
     * @param date    The date to format.
     * 
     * @return An ISO date/time string, formatted following
     *         {@link #ISOTIMEDATESTRING_SHORT}.
     */
    public static String formatAsIso( Date date ) {
        return formatAsIso( date, Style.SHORT );
    }

    /**
     * Format a date according to the ISO conventions and timezone UT (see
     * {@link #UT }.
     * 
     * @param date        The date to format.
     *                
     * @param style
     *                Defines what formatter will be returned. Depending on its
     *                value, a formatter for {@link #ISOTIMEDATESTRING_SHORT},
     *                {@link #ISOTIMEDATESTRING_MEDIUM}, or
     *                {@link #ISOTIMEDATESTRING_LONG} will be chosen.
     *                
     * @return An ISO date/time string.
     */
    public static String formatAsIso( Date date, Style style ) {
        SimpleDateFormat sdf = getIsoDateFormat( style );
        return sdf.format( date );
    }

    /**
     * @return A Calendar instance preset to UT (GMT) and "now".
     */
    public static Calendar getCalendar() {
        return getCalendar( UT );

    }

    /**
     * @param timeZone
     *                The time zone the returned Calendar should operate in.
     * 
     * @return A Calendar instance preset to the input time zone and the current
     *         time and date.
     */
    public static Calendar getCalendar( TimeZone timeZone ) {
        return getCalendar( timeZone, (Date) null );
    }

    /**
     * @param timeZone
     *            The time zone the returned Calendar should operate in.
     * @param date
     *            The initial date of the Calendar; if <code>null</code> it
     *            defaults to the current time and date.
     * 
     * @return A Calendar instance preset to the input time zone and the given
     *         date.
     */
    public static Calendar getCalendar( TimeZone timeZone, Date date ) {
        if( date == null ) {
            date = DateUtilsLite.getNowUT();
        }
        Calendar calendar = Calendar.getInstance( timeZone );
        calendar.setTime( date );
        return calendar;
    }

    /**
     * @param timeZone
     *                The time zone the returned Calendar should operate in.
     * @param date
     *                The initial date of the Calendar, written in
     *                {@link #ISOTIMEDATESTRING_SHORT} format.
     * 
     * @return A Calendar instance preset to the input time zone and the given
     *         date.
     * 
     * @throws ParseException
     *                 If the input string doesn't match the
     *                 {@link #ISOTIMEDATESTRING_SHORT} pattern.
     */
    public static Calendar getCalendar( TimeZone timeZone, String date )
        throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat( ISOTIMEDATESTRING_SHORT);
        format.setTimeZone( UT );
        Date ddate = format.parse( date );
        return getCalendar( timeZone, ddate );
    }

    /**
     * @return The current date/time, formatted as an ISO date-time string. For
     *         instance <code>2001-10-09T10:52:30</code>.
     */
    public static String getCurrentIsoDateTime() {
        SimpleDateFormat fmt = getIsoDateFormat();
        Calendar cal = getCalendar();
        Date now = cal.getTime();
        return fmt.format( now );
    }

    /**
     * Parse the input string.
     * 
     * @param isodate
     *                An ISO date/time string formatted according to
     *                {@link #ISOTIMEDATESTRING_SHORT}
     * 
     * @return The Date object obtained from parsing the input date.
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static Date getDate( String isodate )
        throws NumberFormatException, ParseException {
        return getDate( isodate, getIsoDateFormat() );
    }

    /**
     * Parse the input string; this is a wrapper around 
     * {@link SimpleDateFormat#parse(String)}.
     * 
     * @param isodate
     *                An ISO date/time string
     * 
     * @param format
     *                The format according to which we will parse the time/date
     *                string
     * 
     * @return The Date object obtained from parsing the input date
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static Date getDate( String isodate, SimpleDateFormat format )
        throws ParseException, NumberFormatException {
        return format.parse( isodate );
    }

    /**
     * @return A SimpleDateFormat for the short form of the ISO datetime string
     *         {@link #ISOTIMEDATESTRING_SHORT}, timezone {@link #UT}.
     */
    public static SimpleDateFormat getIsoDateFormat() {
        return getIsoDateFormat( Style.SHORT );
    }

    /**
     * @param style
     *                Defines what formatter will be returned. Depending on its
     *                value, a formatter for {@link #ISOTIMEDATESTRING_SHORT},
     *                {@link #ISOTIMEDATESTRING_MEDIUM}, or
     *                {@link #ISOTIMEDATESTRING_LONG} will be chosen.
     * 
     * @return A SimpleDateFormat for the given form of the ISO date/time
     *         string, timezone {@link #UT}.
     */
    public static SimpleDateFormat getIsoDateFormat( Style style ) {
        return getIsoDateFormat( style, UT );
    }
    
    /**
     * @param style
     *                Defines what formatter will be returned. Depending on its
     *                value, a formatter for {@link #ISOTIMEDATESTRING_SHORT},
     *                {@link #ISOTIMEDATESTRING_MEDIUM}, or
     *                {@link #ISOTIMEDATESTRING_LONG} will be chosen.
     * 
     * @param timezone
     *                The timezone to use for formatting. If <code>null</code>
     *                defaults to {@link #UT}.
     * 
     * @return A SimpleDateFormat for the long or short form of the ISO datetime
     *         string.
     */
    public static SimpleDateFormat getIsoDateFormat( Style style,
                                                     TimeZone timezone ) {
        if( timezone == null ) {
            timezone = UT;
        }

        String fmt;
        switch( style ) {
        case LONG:
            fmt = ISOTIMEDATESTRING_LONG;
            break;
            
        case MEDIUM:
            fmt = ISOTIMEDATESTRING_MEDIUM;
            break;
            
        case SHORT:
            fmt = ISOTIMEDATESTRING_SHORT;
            break;
            
        case SHORTER:
            fmt = ISOTIMEDATESTRING_SHORTER;
            break;

        case DATE:
            fmt = DATESTRING;
            break;

        case TIME:
            fmt = TIMESTRING;
            break;

        case TIME_LONG:
        	fmt = TIMESTRING_LONG;
        	break;

        case TIME_SHORT:
            fmt = TIMESTRING_SHORT;
            break;

        case TIME_SHORTER:
            fmt = TIMESTRING_SHORTER;
            break;
            
        default:
            throw new IllegalArgumentException( "Style="+style );
        }
        
        SimpleDateFormat ret = new SimpleDateFormat( fmt );
        ret.setTimeZone( timezone );
        return ret;
    }
    
    /**
     * Breaks up an ISO date+time string into an array of 7 elements: year,
     * month, day, hour, minutes, seconds, milliseconds.
     * 
     * @author amchavan, 09-Oct-2001: creation
     * 
     * @throws NumberFormatException
     * @throws ParseException
     */
    static int[] parseIsoDateTime( String isodatetime )
        throws NumberFormatException, ParseException {

        int[] components = new int[7];
        String isodate = "";
        String isotime = "";

        isodatetime = isodatetime.trim(); // just in case

        // Check if we have a "T"
        int idx = isodatetime.indexOf( 'T' );
        if( idx == -1 ) {
            // we don't know whether this is a time-only or a date-only
            // string, let's find out...
            idx = isodatetime.indexOf( ':' );
            if( idx == -1 ) { // do we have a time?
                isodate = isodatetime; // NO, assume it's a date string
            }
            else {
                isotime = isodatetime; // YES, it's a time string
            }
        }
        else {
            isodate = isodatetime.substring( 0, idx );
            isotime = isodatetime.substring( idx + 1 );
        }

        // temp arrays
        String[] tdate = new String[3];
        String[] ttime = new String[3];

        // Parse the date part
        // ----------------------------------------

        String[] tmp = isodate.split( "-" );
        switch( tmp.length ) {
        case 0:
            // no-op
            break;

        case 1:
            tdate[2] = tmp[0]; // day
            break;

        case 2:
            tdate[1] = tmp[0]; // month
            tdate[2] = tmp[1]; // day
            break;

        case 3:
            tdate[0] = tmp[0]; // year
            tdate[1] = tmp[1]; // month
            tdate[2] = tmp[2]; // day
            break;

        default:
            throw new ParseException( "Invalid date: <" + isodate + ">", 0 );
        }

        // Parse the time part
        // ----------------------------------------

        tmp = isotime.split( ":" );
        switch( tmp.length ) {
        case 0:
            // no-op
            break;

        case 1:
            ttime[0] = tmp[0]; // seconds
            break;

        case 2:
            ttime[0] = tmp[0]; // minutes
            ttime[1] = tmp[1]; // seconds
            break;

        case 3:
            ttime[0] = tmp[0]; // hours
            ttime[1] = tmp[1]; // minutes
            ttime[2] = tmp[2]; // seconds
            break;

        default:
            throw new ParseException( "Invalid time: <" + isotime + ">", 0 );
        }

        // Convert all to integers
        // --------------------------------------
        String comp; // useful component

        for( int i = 0; i < 3; i++ ) {
            comp = tdate[i];
            if( comp == null || comp.length() == 0 ) {
                components[i] = 0;
            }
            else {
                components[i] = Integer.parseInt( comp );
            }
        }

        for( int i = 0; i < 2; i++ ) {
            comp = ttime[i];
            if( comp == null || comp.length() == 0 ) {
                components[i + 3] = 0;
            }
            else {
                components[i + 3] = Integer.parseInt( comp );
            }
        }

        // Special treatment for seconds (can be like '32.456')
        // -----------------------------------------------------
        comp = ttime[2];
        if( comp == null || comp.length() == 0 ) {
            components[5] = 0;
            components[6] = 0;
        }
        else {
            double seconds = Double.parseDouble( ttime[2] );
            components[5] = (int) Math.floor( seconds );
            components[6] = (int) Math.round( (seconds - components[5]) * 1000);
        }

        return components;
    }
    
    /**
     * Force the minute, second and milliseconds parts of the input date to
     * zero, rounding the hour part.
     * 
     * @param input   Date to modify; can be <code>null</code>.
     * 
     * @return <code>null</code> if the input date is <code>null</code>. 
     *         Otherwise the original date, modified so that its minute, second
     *         and milliseconds parts are zero, and its hour part is
     *         correspondingly rounded.<br/>
     * 
     *         For instance, <code>08:32:29.678</code> will be rounded to
     *         <code>09:00:00.000</code> and <code>08:28:15.400</code> will be
     *         rounded to <code>08:00:00.000</code>
     */
    public static Date roundToHour( Date input ) {

        if( input == null ) {
            return null;
        }
        
        input = roundToMinute( input );
        Calendar cal = getCalendar();
        cal.setTime( input );
        int min = cal.get( Calendar.MINUTE );
        if( min == 0 ) {
            return input;       // nothing to do, sec is already zero
        }
        if( min >= 30 ) {
            cal.add( Calendar.HOUR_OF_DAY, 1 );
        }
        cal.set( Calendar.MINUTE, 0 );
        return cal.getTime();
    }

    /**
     * Force the second and milliseconds parts of the input date to zero,
     * rounding the minute part.
     * 
     * @param input   Date to modify, can be <code>null</code>.
     * 
     * @return <code>null</code> if the input date is <code>null</code>. 
     *         Otherwise the original date; modified so that its second 
     *         and milliseconds parts are zero, and its minute part is 
     *         correspondingly rounded.<br/>
     * 
     *         For instance, <code>08:32:29.678</code> will be rounded to
     *         <code>08:33:00.000</code> and <code>08:32:15.400</code> will be
     *         rounded to <code>08:32:00.000</code>
     */
    public static Date roundToMinute( Date input ) {

        if( input == null ) {
            return null;
        }
        input = roundToSecond( input );
        
        Calendar cal = getCalendar();
        cal.setTime( input );
        int sec = cal.get( Calendar.SECOND );
        if( sec == 0 ) {
            return input;       // nothing to do, sec is already zero
        }
        if( sec >= 30 ) {
            cal.add( Calendar.MINUTE, 1 );
        }
        cal.set( Calendar.SECOND, 0 );
        return cal.getTime();
    }

    /**
     * Force the milliseconds part of the input date to zero, rounding the
     * seconds part.
     * 
     * @param input   Date to modify; can be <code>null</code>.
     * 
     * @return <code>null</code> if the input date is <code>null</code>. 
     *         Otherwise the original date, modified so that its milliseconds
     *         part is zero, and its seconds part is correspondingly rounded.
     *         <br/>
     * 
     *         For instance, <code>08:32:15.678</code> will be rounded to
     *         <code>08:32:16.000</code> and <code>08:32:15.400</code> will be
     *         rounded to <code>08:32:15.000</code>
     */
    public static Date roundToSecond( Date input ) {
        
        if( input == null ) {
            return null;
        }
        
        Calendar cal = getCalendar();
        cal.setTime( input );
        int msec = cal.get( Calendar.MILLISECOND );
        if( msec == 0 ) {
            return input;       // nothing to do, msec is already zero
        }
        if( msec >= 500 ) {
            cal.add( Calendar.SECOND, 1 );
        }
        cal.set( Calendar.MILLISECOND, 0 );
        return cal.getTime();
    }

    /**
     * Set the time part of a date.
     * 
     * @param input    Date to modify.
     * @param min      New minutes
     * @param sec      New seconds
     * @param msec     New milliseconds
     * 
     * @return The original date, modified so that its minutes, seconds and
     *         milliseconds corresponds to the input time components.<br/>
     *         
     *         For instance, if the input date is
     *         <code>2008-04-11T08:32:15.000</code> and the time components are
     *         all zero, the output date will be
     *         <code>2008-04-11T08:00:00.000</code>
     */
    public static Date setTime( Date input, int min, int sec, int msec ) {
        
        Calendar cal = getCalendar();
        cal.setTime( input );
        return setTime( input, cal.get( Calendar.HOUR_OF_DAY), min, sec, msec );
    }

    /**
     * Set the time part of a date.
     * 
     * @param input  Date to modify.
     * @param hour   New hours (24-hour calendar)
     * @param min    New minutes
     * @param sec    New seconds
     * @param msec   New milliseconds
     * @return The original date, modified so that its time part corresponds
     *         to the input time components.<br/>For instance, if the input date
     *         is <code>2008-04-11T08:32:15.000</code> and the time components
     *         are all zero, the output date will be 
     *         <code>2008-04-11T00:00:00.000</code>
     */
    public static Date setTime( Date input, 
                                int hour, int min, int sec, int msec ) {
        
        Calendar cal = getCalendar();
        cal.setTime( input );
        cal.set( Calendar.HOUR_OF_DAY, hour );
        cal.set( Calendar.MINUTE, min );
        cal.set( Calendar.SECOND, sec );
        cal.set( Calendar.MILLISECOND, msec );

        return cal.getTime();
    }

    /**
     * @param format
     *                A date-time format string
     * 
     * @param timezone
     *                The timezone to use for formatting. If <code>null</code>
     *                defaults to {@link #UT}.
     * 
     * @return A SimpleDateFormat for the long or short form of the ISO datetime
     *         string.
     */
    public static SimpleDateFormat getDateFormat( String format,
                                                  TimeZone timezone ) {
        if( timezone == null ) {
            timezone = UT;
        }
        
        SimpleDateFormat ret = new SimpleDateFormat( format );
        ret.setTimeZone( timezone );
        return ret;
    }
    
    public static String getRA(double ra)
    {
    	if (ra == 0 || Double.isNaN(ra)) return "";
    	ra /= 15;
    	
    	int hours = (int) (ra);
    	int minutes = (int) ((ra-hours)*60);
    	double seconds = ((ra-hours)*60 - minutes)*60;
    	
    	return String.format("%2dh %2dm %5.1fs", hours, minutes, seconds);
    	
    }
    
    public static String getDec(double dec)
    {
    	if (dec == 0 || Double.isNaN(dec)) return "";
    	
    	int degr = (int) (dec);
    	int minutes = (int) ((dec-degr)*60);
    	double seconds = ((dec-degr)*60 - minutes)*60;
    	
    	return String.format("%2dd %2dm %5.1fs", degr, Math.abs(minutes), Math.abs(seconds));
    	
    }
    
    
}
