package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;

public class SharpeRatioReducer extends Reducer<Text, TextArrayWritable, Text, Text> {

    private static final Text KEY_OUT = new Text();

    private static final Text VALUE_OUT = new Text();

    public void reduce(Text key, Iterable<TextArrayWritable> values, Context context)
            throws IOException, InterruptedException {

        String maxRatioStockSymbol = null;
        BigDecimal maxRatio = null;

        for (TextArrayWritable value : values) {
            Writable[] valueArray = value.get();
            if (valueArray.length != 2) {
                throw new IllegalArgumentException("Stock symbol and ratio are required in reducer");
            }

            BigDecimal ratio = new BigDecimal(valueArray[1].toString());

            if (maxRatio == null || ratio.compareTo(maxRatio) > 0) {
                maxRatioStockSymbol = valueArray[0].toString();
                maxRatio = ratio;
            }
        }

        if (maxRatio != null) {
            KEY_OUT.set(maxRatioStockSymbol);
            VALUE_OUT.set(maxRatio.toString());
            context.write(KEY_OUT, VALUE_OUT);
        }
    }
}
