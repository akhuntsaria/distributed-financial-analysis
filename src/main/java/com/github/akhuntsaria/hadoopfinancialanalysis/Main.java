package com.github.akhuntsaria.hadoopfinancialanalysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class Main {

    private static final int BIG_DECIMAL_SCALE = 5;

    public static void main(String[] args) {
        var returnHistory = List.of(
                BigDecimal.valueOf(10.2),
                BigDecimal.valueOf(8),
                BigDecimal.valueOf(15.1),
                BigDecimal.valueOf(12.5),
                BigDecimal.valueOf(10.6));
        var expectedReturn = mean(returnHistory);
        var riskFreeReturn = BigDecimal.valueOf(10);
        var returnHistoryStandardDeviation = standardDeviation(returnHistory);
        var sharpeRatio = (expectedReturn.subtract(riskFreeReturn))
                .divide(returnHistoryStandardDeviation, BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
        System.out.println(expectedReturn + " " + returnHistoryStandardDeviation + " " + sharpeRatio);
    }

    private static BigDecimal mean(List<BigDecimal> m) {
        return m.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(m.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal standardDeviation(List<BigDecimal> numbers) {
        var mean = mean(numbers);
        return numbers.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b.subtract(mean).pow(2)))
                .divide(BigDecimal.valueOf(numbers.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN)
                .sqrt(new MathContext(BIG_DECIMAL_SCALE));
    }
}
