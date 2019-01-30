package com.application.itunes.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * All comments above tests are adjusted to pacific time zone instead of GMT, but that shouldn't
 * affect their difference.
 */
public class DateUtilsTest {

    @Test
    public void checkDateDifferenceMethodJustNow() {
        /* 5/5/2017, 10:29:30 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("Just Now", DateUtils.getDateDifference(1494005370000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceMinutesNotPlural() {
        /* 5/5/2017, 10:29:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("1 minute ago", DateUtils.getDateDifference(1494005340000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceMinutesPlural() {
        /* 5/4/2017, 10:20:00 PM, 5/4/2017, 10:30:00 PM */
        assertEquals("10 minutes ago", DateUtils.getDateDifference(1493961600000L, 1493962200000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceHoursNotPlural() {
        /* 5/5/2017, 9:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("1 hour ago", DateUtils.getDateDifference(1494001800000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceHoursPlural() {
        /* 5/5/2017, 1:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("9 hours ago", DateUtils.getDateDifference(1493973000000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceDaysNotPlural() {
        /* 5/4/2017, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("1 day ago", DateUtils.getDateDifference(1493919000000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceDaysPlural() {
        /* 4/24/2017, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("11 days ago", DateUtils.getDateDifference(1493055000000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceMonthsNotPlural() {
        /* 4/5/2017, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("1 month ago", DateUtils.getDateDifference(1491413400000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceMonthsPlural() {
        /* 8/9/2016, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("9 months ago", DateUtils.getDateDifference(1470331800000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceYearNotPlural() {
        /* 5/5/2016, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("1 year ago", DateUtils.getDateDifference(1462469400000L, 1494005400000L));
    }

    @Test
    public void checkDateDifferenceMethodDifferenceYearPlural() {
        /* 8/1/2006, 10:30:00 AM, 5/5/2017, 10:30:00 AM */
        assertEquals("10 years ago", DateUtils.getDateDifference(1154453400000L, 1494005400000L));
    }
}