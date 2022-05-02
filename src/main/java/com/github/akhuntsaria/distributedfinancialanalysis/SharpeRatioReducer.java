package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;

public class SharpeRatioReducer extends Reducer<Text, Text, Text, Text> {

    private static final String VALUE_IN_SEPARATOR = ",";

    private static final Text KEY_OUT = new Text();

    private static final Text VALUE_OUT = new Text();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String maxRatioStockSymbol = null;
        BigDecimal maxRatio = null;

        for (Text value : values) {
            String[] valueParts = value.toString().split(VALUE_IN_SEPARATOR);
            if (valueParts.length != 2) {
                throw new IllegalArgumentException(String.format("Invalid reducer value \"%s\". " +
                        "Example of a correct value: MSFT,0.8", value));
            }

            BigDecimal ratio = new BigDecimal(valueParts[1]);

            if (maxRatio == null || ratio.compareTo(maxRatio) > 0) {
                maxRatioStockSymbol = valueParts[0];
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
