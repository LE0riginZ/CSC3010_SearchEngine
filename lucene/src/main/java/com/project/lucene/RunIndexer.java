package com.project.lucene;

import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.lucene.model.DocumentItem;
import com.project.lucene.model.ResultItem;
import com.project.lucene.util.Indexing;
import com.project.lucene.util.LuceneFileSearch;

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
		String filePath = "../data_directory/data.json";
		
		indexJson(filePath);
		
		List<Document> testList = indexing.searchIndex("title", "information");
		System.out.println("===================");
		
		for(Document item: testList) {
			System.out.println(item.get("title"));
		}
		System.out.println("Multi here ===================");
		
		ResultItem testList2 = indexing.querySearchIndex("information", 1, 20);
		
		for(DocumentItem item: testList2.getDocuments()) {
			System.out.println(item.title);
		}
		
	}
	
	// Method to read in JSON
	public List<DocumentItem> readJsonFileAndParse(String filePath) throws IOException {
        // Create an instance of ObjectMapper from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and parse it into a List of ItemDTO objects
        List<DocumentItem> items = objectMapper.readValue(new File(filePath),
                                                     objectMapper.getTypeFactory().constructCollectionType(List.class, DocumentItem.class));

        return items;
    }
	
	// Function to read in JSON and index documents from the JSON
	public void indexJson(String filePath) throws IOException {
		// Read in JSON file as a list of documents
		List<DocumentItem> documentsList = readJsonFileAndParse(filePath);
		System.out.println("Indexing started");
		
		int count = 0;
		
		for(DocumentItem item: documentsList) {
			indexing.indexDocument(item.title.toString(), item.content ,item.url);
			count++;
			System.out.println(Integer.toString(count) + ". Indexed: " + item.title.toString());
		}
		
		
//		for(DocumentItem item: documentsList) {
//			System.out.println("From JSON: " + item.url);
//			List<Document> docList = indexing.searchIndex("url", item.url);
//		
//			if(docList == null) {
//				System.out.println("docList is null");
//				indexing.indexDocument(item.title, item.content ,item.url);
//				count++;
//				System.out.println(Integer.toString(count) + ". Indexed: " + item.url);
//				
//				
//			}else {
//				System.out.println("docList is NOT null");
//				for (Document doc: docList) {
//					System.out.println("From query: " + doc.get("title"));
//					if(doc.get("url").equals(item.url) ) {
//						System.out.println("Document has already been indexed");
//						break;
//						
//						
//					}else {
//						indexing.indexDocument(item.title, item.content ,item.url);
//						count++;
//						System.out.println(Integer.toString(count) + ". Indexed: " + item.url);
//						break;
//						
//					}
//				}
//			}
//		}
		
		System.out.println("Indexed " + count + " documents");
	}


	
}
