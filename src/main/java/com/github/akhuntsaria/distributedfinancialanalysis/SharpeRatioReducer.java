package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;

public class SharpeRatioReducer extends Reducer<Text, Text, Text, Text> {

    //TODO: optimize, handle errors
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        String maxRationStockSymbol = null;
        BigDecimal maxRatio = null;

        for (Text value : values) {
            String[] valueParts = value.toString().split(",");
            BigDecimal ratio = new BigDecimal(valueParts[1]);

            if (maxRatio == null || ratio.compareTo(maxRatio) > 0) {
                maxRationStockSymbol = valueParts[0];
                maxRatio = ratio;
            }
        }

        if (maxRatio != null) {
            context.write(new Text(maxRationStockSymbol), new Text(maxRatio.toString()));
        }
    }
}
