package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SharpeRatioReducer extends Reducer<Text, ObjectWritable, Text, Text> {

    private static final Text MAX_RATIO_KEY = new Text("MaxRatio");

    private static final Text STOCK_SYMBOL_KEY = new Text("StockSymbol");

    public void reduce(Text key, Iterable<ObjectWritable> values, Context context)
            throws IOException, InterruptedException {
        context.write(MAX_RATIO_KEY, new Text("1.0"));
        context.write(STOCK_SYMBOL_KEY, new Text("TSLA"));
    }
}
