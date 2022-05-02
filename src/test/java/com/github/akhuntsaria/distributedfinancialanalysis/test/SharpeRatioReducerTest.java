package com.github.akhuntsaria.distributedfinancialanalysis.test;

import com.github.akhuntsaria.distributedfinancialanalysis.SharpeRatioReducer;
import com.github.akhuntsaria.distributedfinancialanalysis.TextArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SharpeRatioReducerTest {

    @Test
    void reduce() throws IOException, InterruptedException {
        //noinspection rawtypes
        Reducer.Context contextMock = mock(Reducer.Context.class);
        ArgumentCaptor<Text> keyOutArgumentCaptor = ArgumentCaptor.forClass(Text.class);
        ArgumentCaptor<Text> valueOutArgumentCaptor = ArgumentCaptor.forClass(Text.class);

        TextArrayWritable textArrayWritable1 = new TextArrayWritable();
        textArrayWritable1.set(new Text[]{new Text("AAPL"), new Text("0.1")});
        TextArrayWritable textArrayWritable2 = new TextArrayWritable();
        textArrayWritable2.set(new Text[]{new Text("MSFT"), new Text("0.2")});

        //noinspection unchecked
        (new SharpeRatioReducer()).reduce(
                new Text(), Arrays.asList(textArrayWritable1, textArrayWritable2), contextMock);

        //noinspection unchecked
        verify(contextMock, times(1))
                .write(keyOutArgumentCaptor.capture(), valueOutArgumentCaptor.capture());
        assertEquals("MSFT", keyOutArgumentCaptor.getValue().toString());
        assertEquals("0.2", valueOutArgumentCaptor.getValue().toString());
    }

    @Test
    void reduce_incorrectInputValue() {
        TextArrayWritable textArrayWritable1 = new TextArrayWritable();
        textArrayWritable1.set(new Text[]{new Text("AAPL")});

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                (new SharpeRatioReducer()).reduce(new Text(), List.of(textArrayWritable1), null));
        assertEquals("Stock symbol and ratio are required in reducer", exception.getMessage());
    }
}
