package com.project.lucene.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class LuceneConfig {

    // Define the directory path for the Lucene index
    private static final String RELATIVE_LUCENE_INDEX_DIRECTORY = "../index_directory";
    private static final String RELATIVE_LUCENE_DATA_DIRECTORY = "../data_directory";

//    @Bean
//    public Directory luceneDirectory() throws IOException {
//        // Create a FSDirectory using the specified path
//        Path indexPath = Paths.get(LUCENE_INDEX_DIRECTORY);
//        return FSDirectory.open(indexPath);
//    }
    
    @Bean
    @Qualifier("indexDirectory")
    public Directory indexDirectory() throws IOException {
        // Calculate the absolute path for the Lucene index directory
        String currentWorkingDirectory = System.getProperty("user.dir");
        Path indexPath = Paths.get(currentWorkingDirectory, RELATIVE_LUCENE_INDEX_DIRECTORY);
        return FSDirectory.open(indexPath);
    }
    
    @Bean
    @Qualifier("dataDirectory")
    public Directory dataDirectory() throws IOException {
        // Calculate the absolute path for the Lucene data directory
        String currentWorkingDirectory = System.getProperty("user.dir");
        Path indexPath = Paths.get(currentWorkingDirectory, RELATIVE_LUCENE_DATA_DIRECTORY);
        return FSDirectory.open(indexPath);
    }
    
    @Bean
    public StandardAnalyzer standardAnalyzer() {
        return new StandardAnalyzer();
    }
}