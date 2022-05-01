package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SharpeRationMapper extends Mapper<Object, Text, Text, Text> {

    private static final Logger logger = LoggerFactory.getLogger(SharpeRationMapper.class);

    // Same output key for all entries because we need to find a maximum value later
    private static final Text KEY_OUT = new Text("key");

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        var jobInputEntry = new JobInputEntry(value.toString());
        var sharpeRatio = SharpeRatioUtil.calculate(jobInputEntry);

        logger.info("Mapped job entry {} to {}", jobInputEntry, sharpeRatio);

        context.write(KEY_OUT, new Text(jobInputEntry.getStockSymbol() + "," + sharpeRatio));
    }
}