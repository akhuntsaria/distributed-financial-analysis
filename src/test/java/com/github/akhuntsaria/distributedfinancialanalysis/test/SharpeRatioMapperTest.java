package com.github.akhuntsaria.distributedfinancialanalysis.test;

import com.github.akhuntsaria.distributedfinancialanalysis.JobInputEntry;
import com.github.akhuntsaria.distributedfinancialanalysis.SharpeRatioMapper;
import com.github.akhuntsaria.distributedfinancialanalysis.SharpeRatioUtil;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SharpeRatioMapperTest {

    @Test
    void map() throws IOException, InterruptedException {

        try (MockedStatic<SharpeRatioUtil> ignored = mockStatic(SharpeRatioUtil.class)) {
            when(SharpeRatioUtil.calculate(any(JobInputEntry.class))).thenReturn(BigDecimal.ONE);
            //noinspection rawtypes
            Mapper.Context contextMock = mock(Mapper.Context.class);
            ArgumentCaptor<Text> textArgumentCaptor = ArgumentCaptor.forClass(Text.class);
            ArgumentCaptor<ArrayWritable> arrayWritableArgumentCaptor = ArgumentCaptor.forClass(ArrayWritable.class);

            //noinspection unchecked
            (new SharpeRatioMapper()).map(new Object(), new Text("AAPL,2015/01/01,2016/01/01"), contextMock);

            //noinspection unchecked
            verify(contextMock, times(1))
                    .write(textArgumentCaptor.capture(), arrayWritableArgumentCaptor.capture());
            assertEquals("key", textArgumentCaptor.getValue().toString());
            assertArrayEquals(new String[]{"AAPL","1"}, arrayWritableArgumentCaptor.getValue().toStrings());
        }
    }
}
