package com.github.akhuntsaria.hadoopfinancialanalysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class Main {

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
                .divide(returnHistoryStandardDeviation, 5, RoundingMode.HALF_EVEN);
        System.out.println(sharpeRatio);
    }

    //TODO: cleanup, stream API
    private static BigDecimal mean(List<BigDecimal> m) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal bigDecimal : m) {
            sum = sum.add(bigDecimal);
        }
        return sum.divide(BigDecimal.valueOf(m.size()), RoundingMode.HALF_EVEN);
    }

    //TODO: cleanup, stream API
    public static BigDecimal standardDeviation(List<BigDecimal> numbers) {
        var standardDeviation = BigDecimal.ZERO;
        var mean = mean(numbers);

        for (var number: numbers) {
            standardDeviation = standardDeviation.add(number.subtract(mean).pow(2));
        }

        return standardDeviation.divide(BigDecimal.valueOf(numbers.size()).sqrt(new MathContext(5)), RoundingMode.HALF_EVEN);
    }
}
