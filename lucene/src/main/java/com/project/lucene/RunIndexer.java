package com.project.lucene;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.lucene.util.Indexing;
import com.project.lucene.util.LuceneFileSearch;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RunIndexer implements CommandLineRunner{
	
	// Your main functions put here for those
	// functions you want to run upon running the project
	// without user prompt
	
	private Indexing indexing;
	private LuceneFileSearch luceneFileSearch;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}


	
}
