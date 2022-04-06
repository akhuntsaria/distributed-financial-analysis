package com.github.akhuntsaria.distributedfinancialanalysis;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final int BIG_DECIMAL_SCALE = 5;

    private static final BigDecimal RISK_FREE_RETURN = BigDecimal.valueOf(8)
            .divide(BigDecimal.valueOf(365), BIG_DECIMAL_SCALE, RoundingMode.HALF_UP);

    public static void main(String[] args) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");

        var stockSymbols = stockSymbols();
        var fromCalendar = fromCalendar();
        var toCalendar = toCalendar();

        if (stockSymbols.isEmpty() || fromCalendar.isEmpty() || toCalendar.isEmpty()) {
            return;
        }

        System.out.println();

        var maxEntry = stockSymbols.stream()
                .map(symbol -> Map.entry(symbol, sharpeRatio(symbol, fromCalendar.get(), toCalendar.get())))
                .filter(e -> e.getValue().isPresent())
                .peek(e -> System.out.printf("%s %s\n", e.getKey(), e.getValue().get()))
                .max(Comparator.comparing(e -> e.getValue().get()));

        if (maxEntry.isPresent() && maxEntry.get().getValue().isPresent()) {
            System.out.printf("\nMax: %s %s\n", maxEntry.get().getKey(), maxEntry.get().getValue().get());
        }
    }

    private static Calendar calendar(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat.parse(date);
        return simpleDateFormat.getCalendar();
    }

    private static Optional<Calendar> fromCalendar() {
        System.out.print("Enter from date (2015/01/01): ");
        String date = new Scanner(System.in).nextLine();

        if (date.isEmpty()) {
            date = "2015/01/01";
        }

        try {
            return Optional.of(calendar(date));
        } catch (ParseException e) {
            System.err.println("Invalid date format");
            return Optional.empty();
        }
    }

    private static BigDecimal mean(List<BigDecimal> numbers) {
        return numbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(numbers.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    private static Optional<BigDecimal> sharpeRatio(String stockSymbol, Calendar fromCalendar, Calendar toCalendar) {
        List<BigDecimal> returnHistory;
        try {
            Optional<Stock> stock = Optional.ofNullable(
                    YahooFinance.get(stockSymbol, fromCalendar, toCalendar, Interval.DAILY));
            if (stock.isEmpty()) {
                throw new RuntimeException("Stock not found");
            }

            returnHistory = stock.get()
                    .getHistory().stream()
                    .map(h -> h.getClose().subtract(h.getOpen())
                        .divide(h.getOpen(), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(100)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.printf("Could not load data for %s\n", stockSymbol);

            return Optional.empty();
        }

        var mean = mean(returnHistory);

        return Optional.of(mean.subtract(RISK_FREE_RETURN)
                .divide(standardDeviation(returnHistory, mean), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN));
    }

    private static BigDecimal standardDeviation(List<BigDecimal> numbers, BigDecimal mean) {
        return numbers.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b.subtract(mean).pow(2)))
                .divide(BigDecimal.valueOf(numbers.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN)
                .sqrt(new MathContext(BIG_DECIMAL_SCALE));
    }

    private static List<String> stockSymbols() {
        System.out.print("Enter comma-separated stock symbols (AAPL,MSFT): ");
        String stockSymbols = new Scanner(System.in).nextLine();

        if (stockSymbols.isEmpty()) {
            stockSymbols = "AAPL,MSFT";
        }

        return Arrays.stream(stockSymbols.split(",")).filter(Objects::nonNull).toList();
    }

    private static Optional<Calendar> toCalendar() {
        System.out.print("Enter to date (2016/01/01): ");
        String date = new Scanner(System.in).nextLine();

        if (date.isEmpty()) {
            date = "2016/01/01";
        }

        try {
            return Optional.of(calendar(date));
        } catch (ParseException e) {
            System.err.println("Invalid date format");
            return Optional.empty();
        }
    }
}
