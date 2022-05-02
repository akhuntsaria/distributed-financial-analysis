package com.github.akhuntsaria.distributedfinancialanalysis.test;

import com.github.akhuntsaria.distributedfinancialanalysis.JobInputEntry;
import com.github.akhuntsaria.distributedfinancialanalysis.SharpeRatioUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class SharpeRatioUtilTest {

    @Test
    void calculate() {
        try (MockedStatic<SharpeRatioUtil> ignored = mockStatic(SharpeRatioUtil.class)) {
            when(SharpeRatioUtil.calculate(any())).thenCallRealMethod();
            when(SharpeRatioUtil.mean(any())).thenCallRealMethod();
            when(SharpeRatioUtil.returnHistory(any())).thenReturn(List.of(BigDecimal.ONE, BigDecimal.TEN));
            when(SharpeRatioUtil.standardDeviation(any(), any())).thenCallRealMethod();

            BigDecimal sharpeRatio = SharpeRatioUtil.calculate(new JobInputEntry("AAPL,2015/01/01,2016/01/01"));

            assertEquals(new BigDecimal("1.21735"), sharpeRatio);
        }
    }

    @Test
    void mean() {
        BigDecimal mean = SharpeRatioUtil.mean(List.of(BigDecimal.ONE, BigDecimal.TEN));
        assertEquals(new BigDecimal("5.50000"), mean);
    }

    @Test
    void standardDeviation() {
        List<BigDecimal> numbers = Stream.of(9, 2, 5, 4, 12, 7, 8, 11).map(BigDecimal::new).toList();
        BigDecimal standardDeviation = SharpeRatioUtil.standardDeviation(numbers, SharpeRatioUtil.mean(numbers));
        assertEquals(new BigDecimal("3.2307"), standardDeviation);
    }
}
