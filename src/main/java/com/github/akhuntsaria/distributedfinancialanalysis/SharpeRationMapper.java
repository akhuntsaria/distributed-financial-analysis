package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SharpeRationMapper extends Mapper<Object, Text, Text, ArrayWritable> {

    private static final Logger logger = LoggerFactory.getLogger(SharpeRationMapper.class);

    // Same output key for all entries because we need to find a maximum value later
    private static final Text KEY_OUT = new Text("key");

    private static final Text STOCK_SYMBOL_OUT = new Text();

    private static final Text SHARPE_RATIO_OUT = new Text();

    private static final TextArrayWritable VALUE_OUT = new TextArrayWritable();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        var jobInputEntry = new JobInputEntry(value.toString());
        var sharpeRatio = SharpeRatioUtil.calculate(jobInputEntry);

        logger.info("Mapped job entry {} to {}", jobInputEntry, sharpeRatio);

        STOCK_SYMBOL_OUT.set(jobInputEntry.getStockSymbol());
        SHARPE_RATIO_OUT.set(sharpeRatio.toString());
        VALUE_OUT.set(new Text[]{STOCK_SYMBOL_OUT, SHARPE_RATIO_OUT});

        context.write(KEY_OUT, VALUE_OUT);
    }
}