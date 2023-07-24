package com.project.lucene;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.lucene.model.DocumentItem;
import com.project.lucene.model.ResultItem;
import com.project.lucene.util.Indexing;
import com.project.lucene.util.LuceneFileSearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
		
		// Specify the directory path containing the files
        String directoryPath = "../data_directory";
		
//        String filePath = "../data_directory/data.json";
		
		indexJson(directoryPath);
		
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
	
	public static String removeUnicodeSymbols(String input) {
        // Regular expression pattern to match all Unicode symbols excluding +, ., and ?
        // The caret (^) inside the square brackets means "not"
        // So [^\\p{L}\\p{N}+\\Q.\\E?\\s] matches any character that is not a letter, digit, +, ., ?, or whitespace
        Pattern pattern = Pattern.compile("[^\\p{L}\\p{N},\\-+.']");
        Matcher matcher = pattern.matcher(input);

        // Use the matcher to replace all non-matching characters with an empty string
        String cleanedString = matcher.replaceAll(" ");

        return cleanedString;
    }
	
	// Function to read in JSON and index documents from the JSON
	public void indexJson(String directoryPath) throws IOException {
		// Create a File object for the directory
        File directory = new File(directoryPath);
        
        File[] files = directory.listFiles();
        
        String outputFilePath = "../index_log.txt";
        
        int count = 0;
        
     // Create a FileWriter object in append mode
        FileWriter writer = new FileWriter(outputFilePath, true);
        
     // Loop through each file in the directory
        for (File file : files) {
            if (file.isFile()) {
                // Process the file name
                String fileName = file.getName();
                String filePath = directoryPath + "/" + fileName;
                System.out.println("Processing file: " + filePath);
                
             
                
             // Get the current date and time
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                
             // Append the file name and current time to the output text file
                writer.write(fileName + " - " + currentTime.format(formatter) + "\n");
                
             // Read in JSON file as a list of documents
        		List<DocumentItem> documentsList = readJsonFileAndParse(filePath);
        		System.out.println("Indexing started");
        		
        		
        		
//        		Simple index
        		for(DocumentItem item: documentsList) {        			
        			indexing.indexDocument(item.title.toString(), item.content ,item.url);
        			count++;
        			writer.write(Integer.toString(count) + ". Indexed: " + item.title.toString() + "\n");
        			System.out.println(Integer.toString(count) + ". Indexed: " + item.title.toString());
        		}
                
            }
        }
		
		
		
//		Remove duplicates		
//		for(DocumentItem item: documentsList) {
//			
//			System.out.println("From JSON: " + item.title);
//			System.out.println("From JSON (removed symbols): " + removeUnicodeSymbols(item.title));
//			
//			try {
//			List<Document> docList = indexing.searchIndex("title", removeUnicodeSymbols(item.title));
//			}catch(Exception e) {
//				System.out.println("From JSON: " + item.title);
//			}
//			
//			
//			if(docList.isEmpty()) {
//				System.out.println("docList is null");
//				indexing.indexDocument(removeUnicodeSymbols(item.title), item.content ,item.url);
//				count++;
//				System.out.println(Integer.toString(count) + ". Indexed: " + removeUnicodeSymbols(item.title));
//				
//				
//			}else {
//				System.out.println("docList is NOT null");
//				for (Document doc: docList) {
//					System.out.println("From query: " + doc.get("title"));
//					if(doc.get("title").equals(removeUnicodeSymbols(item.title))) {
//						System.out.println("Document has already been indexed");
//						break;
//						
//						
//					}else {
//						indexing.indexDocument(removeUnicodeSymbols(item.title), item.content ,item.title);
//						count++;
//						System.out.println(Integer.toString(count) + ". Indexed: " + removeUnicodeSymbols(item.title));
//						break;
//						
//					}
//				}
//			}
//		}
        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        writer.write("Index finished: " + currentTime.format(formatter) + "\n");
        writer.write("Indexed " + count + " documents" + "\n");
     // Close the FileWriter to save and release resources
        writer.close();
        
		System.out.println("Indexed " + count + " documents");
	}


	
}
