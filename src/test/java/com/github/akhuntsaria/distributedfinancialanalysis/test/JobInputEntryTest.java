package com.github.akhuntsaria.distributedfinancialanalysis.test;

import com.github.akhuntsaria.distributedfinancialanalysis.JobInputEntry;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JobInputEntryTest {

    @Test
    void constructor() throws ParseException {
        JobInputEntry jobInputEntry = new JobInputEntry("AAPL,2015/01/01,2016/01/01");
        assertEquals("AAPL", jobInputEntry.getStockSymbol());

        Calendar expectedFromDate = getCalendarInstance("2015/01/01");
        assertEquals(expectedFromDate, jobInputEntry.getFrom());

        Calendar expectedToDate = getCalendarInstance("2016/01/01");
        assertEquals(expectedToDate, jobInputEntry.getTo());
    }

    @Test
    void constructor_incorrectFromDate() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new JobInputEntry("AAPL,201501/01,2016/01/01"));
        assertEquals("Can't parse input entry \"AAPL,201501/01,2016/01/01\". Example of a correct entry: " +
                "AAPL,2015/01/01,2016/01/01", exception.getMessage());
    }

    @Test
    void constructor_incorrectToDate() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new JobInputEntry("AAPL,2015/01/01,2016.01.01"));
        assertEquals("Can't parse input entry \"AAPL,2015/01/01,2016.01.01\". Example of a correct entry: " +
                "AAPL,2015/01/01,2016/01/01", exception.getMessage());
    }

    @Test
    void constructor_insufficientCsvParts() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new JobInputEntry("AAPL"));
        assertEquals("Can't parse input entry \"AAPL\". Example of a correct entry: " +
                "AAPL,2015/01/01,2016/01/01", exception.getMessage());
    }

    @Test
    void constructor_nullArgument() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new JobInputEntry(null));
        assertEquals("Input entry can't be null", exception.getMessage());
    }

    @Test
    void testToString() {
        JobInputEntry jobInputEntry = new JobInputEntry("AAPL,2015/01/01,2016/01/01");
        assertEquals("JobInputEntry{stockSymbol='AAPL', from=2014-12-31T23:00:00Z, to=2015-12-31T23:00:00Z}",
                jobInputEntry.toString());
    }

    private Calendar getCalendarInstance(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat.parse(date);
        return simpleDateFormat.getCalendar();
    }
}
