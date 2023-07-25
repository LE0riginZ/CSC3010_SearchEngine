package com.project.lucene;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.lucene.model.DocumentItem;
import com.project.lucene.model.GoogleSearchItem;
import com.project.lucene.model.ResultItem;
import com.project.lucene.util.Indexing;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
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
	
	@Override
	public void run(String... args) throws Exception {
//		Benchmark.main(new String[] { "-config", "classpath:benchmark.xml" });
		// TODO Auto-generated method stub
		
		// Specify the directory path containing the files
//        String directoryPath = "../data_directory";
		
//        String filePath = "../data_directory/data.json";
		
//		indexJson(directoryPath); 
		
		System.out.println("==============================");
 
        String keyword = "glasgow";
        int searchCount = 20;
		
		ResultItem queriedList = indexing.newQuerySearchIndex(keyword, 1, searchCount);
		
		// Store Queried Link in querylinksList
		List<String> querylinksList = new ArrayList<>();
		for(DocumentItem item: queriedList.getDocuments()) {
			querylinksList.add(item.url);
		}

		// Get the directory where the CSV files are located.
		String googleTopTwentyPath = "../google_toptwenty";
        
		// Run method to calculate scores
		compareGoogleQueryScores(googleTopTwentyPath, querylinksList ,keyword, searchCount);
       
	}
	
    public static int containsAny(List<String> querylist, List<String> googlelinksList) {
        int count = 0;
        for (String element : querylist) {
            if (googlelinksList.contains(element)) {
                count++;
            }else {
            	System.out.println(element);
            }
        }
        return count;
    }
    
	// Function to read in JSON and index documents from the JSON
	public void compareGoogleQueryScores(String directoryPath, List<String> queriedlinksList, String keyword, int topNum) throws IOException {
		new File(directoryPath);
             
        String filePath = directoryPath + "/topTwenty searches_" + keyword + ".json";
        
        // Contains top ten links from Google
        List<String> googlelinksList = new ArrayList<>();
        
        // Read in JSON file as a list of documents
   		List<GoogleSearchItem> topGoogleList = readJsonFileGoogleSearch(filePath);
   		
   		int count = 0;
		for(GoogleSearchItem item: topGoogleList) {
			if (count < topNum) {
				googlelinksList.add(String.valueOf(item.url));
				count++;
			}		
//			writer.write(Integer.toString(count) + ". Indexed: " + item.title.toString() + "\n");
		}
		
		int relevantRetrieved = containsAny(googlelinksList, queriedlinksList);
		
		System.out.println("=================================");
		System.out.println("Number of similar urls: " + relevantRetrieved);
		System.out.println("Google");
		for(String url: googlelinksList) {
			System.out.println(url);
		}
		System.out.println("=================================");
		System.out.println("Query");
		for(String url: queriedlinksList) {
			System.out.println(url);
		}
			
		System.out.println("================================="); 
		
		
		
		// Calculate precision
		double precision = (double) relevantRetrieved / queriedlinksList.size();
		
		// Calculate recall
		double recall = (double) relevantRetrieved / topGoogleList.size();
		System.out.println("Google list size: " + topGoogleList.size());
		
		// Calculate F1-score
		double f1Score = 2 * (precision * recall) / (precision + recall);
		
		// Print the results
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F1-Score: " + f1Score);
		System.out.println("================================="); 
     				
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
	
	
	
	// Method to read in JSON
	public List<GoogleSearchItem> readJsonFileGoogleSearch(String filePath) throws IOException {
        // Create an instance of ObjectMapper from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and parse it into a List of ItemDTO objects
        List<GoogleSearchItem> items = objectMapper.readValue(new File(filePath),
                                                     objectMapper.getTypeFactory().constructCollectionType(List.class, GoogleSearchItem.class));

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
