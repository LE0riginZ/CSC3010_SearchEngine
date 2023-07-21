package com.project.lucene;

import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.lucene.model.DocumentItem;
import com.project.lucene.util.Indexing;
import com.project.lucene.util.LuceneFileSearch;
import com.project.lucene.util.YourClassName;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

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

		
//		indexing.indexDocument("This is a title", "Test");
//		indexing.indexDocument("This is a test", "Title");
		List<Document> testList = indexing.searchIndex("title", "test");
		
		System.out.println(testList);
		
	}
	
	public List<DocumentItem> readJsonFileAndParse(String filePath) throws IOException {
        // Create an instance of ObjectMapper from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and parse it into a List of ItemDTO objects
        List<DocumentItem> items = objectMapper.readValue(new File(filePath),
                                                     objectMapper.getTypeFactory().constructCollectionType(List.class, DocumentItem.class));

        return items;
    }


	
}
