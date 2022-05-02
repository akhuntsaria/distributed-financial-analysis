package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Maximum sharpe ratio");

        job.setJarByClass(Application.class);
        job.setMapperClass(SharpeRatioMapper.class);
        job.setReducerClass(SharpeRatioReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TextArrayWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        setInputAndOutputPaths(job, args);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    private static void setInputAndOutputPaths(Job job, String[] args) throws IOException {
        Path input = new Path(args.length > 0 ? args[0] : "src/main/resources/input/");
        Path output = new Path(args.length > 1 ? args[1] : "src/main/resources/output/");

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);
    }
}
