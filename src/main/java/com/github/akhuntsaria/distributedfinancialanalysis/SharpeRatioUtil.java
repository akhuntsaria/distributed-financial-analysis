package com.github.akhuntsaria.distributedfinancialanalysis;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SharpeRatioUtil {

    private static final int BIG_DECIMAL_SCALE = 5;

    private static final BigDecimal RISK_FREE_RETURN = BigDecimal.valueOf(8)
            .divide(BigDecimal.valueOf(365), BIG_DECIMAL_SCALE, RoundingMode.HALF_UP);

    public static BigDecimal calculate(JobInputEntry jobInputEntry) {
        List<BigDecimal> returnHistory = returnHistory(jobInputEntry);

        var mean = mean(returnHistory);

        return mean.subtract(RISK_FREE_RETURN)
                .divide(standardDeviation(returnHistory, mean), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal mean(List<BigDecimal> numbers) {
        return numbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(numbers.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    private static List<BigDecimal> returnHistory(JobInputEntry jobInputEntry) {
        try {
            Optional<Stock> stock = Optional.ofNullable(
                    YahooFinance.get(
                            jobInputEntry.getStockSymbol(),
                            jobInputEntry.getFrom(),
                            jobInputEntry.getTo(),
                            Interval.DAILY));

            if (stock.isEmpty()) {
                throw new RuntimeException("Stock not found");
            }

            return stock.get()
                    .getHistory().stream()
                    .map(h -> h.getClose().subtract(h.getOpen())
                            .divide(h.getOpen(), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN)
                            .multiply(BigDecimal.valueOf(100)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static BigDecimal standardDeviation(List<BigDecimal> numbers, BigDecimal mean) {
        return numbers.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b.subtract(mean).pow(2)))
                .divide(BigDecimal.valueOf(numbers.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN)
                .sqrt(new MathContext(BIG_DECIMAL_SCALE));
    }
}
