package com.github.akhuntsaria.distributedfinancialanalysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JobInputEntry {

    private static final String SEPARATOR = ",";

    private final String stockSymbol;

    private Calendar from;

    private Calendar to;

    /**
     * Example of a correct entry: AAPL,2015/01/01,2016/01/01
     */
    public JobInputEntry(String csv) {
        if (csv == null) {
            throw new IllegalArgumentException("Input entry can't be null");
        }

        String[] parts = csv.split(SEPARATOR);
        if (parts.length < 3) {
            throwIllegalArgumentException(csv);
        }

        stockSymbol = parts[0];

        try {
            from = parseDate(parts[1]);
            to = parseDate(parts[2]);
        } catch (ParseException e) {
            throwIllegalArgumentException(csv);
        }
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Calendar getFrom() {
        return from;
    }

    public Calendar getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "JobInputEntry{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", from=" + from.toInstant() +
                ", to=" + to.toInstant() +
                '}';
    }

    private static Calendar parseDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat.parse(date);
        return simpleDateFormat.getCalendar();
    }

    private void throwIllegalArgumentException(String csv) {
        throw new IllegalArgumentException(String.format("Can't parse input entry \"%s\". " +
                "Example of a correct entry: AAPL,2015/01/01,2016/01/01", csv));
    }
}
