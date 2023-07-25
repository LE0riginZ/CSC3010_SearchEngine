package com.project.lucene;

import org.apache.lucene.benchmark.byTask.Benchmark;

public class MyLuceneBenchmark {
    public static void main(String[] args) throws Exception {
        Benchmark.main(new String[] { "-config", "classpath:benchmark.xml" });
    }
}
