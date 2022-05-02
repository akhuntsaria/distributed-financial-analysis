package com.github.akhuntsaria.distributedfinancialanalysis;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

// Subclass with a default constructor is required by reducer
public class TextArrayWritable extends ArrayWritable {

    public TextArrayWritable() {
        super(Text.class);
    }
}
