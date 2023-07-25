package com.project.lucene.service.impl;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import com.project.lucene.model.ResultItem;
import com.project.lucene.service.SearchService;
import com.project.lucene.util.Indexing;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {
	
	private Indexing indexing;

	@Override
	public ResultItem retrieveSearchResults(String searchTerm, Integer pageNum, Integer pageSize) {
		
		// Validate pageNo and set it to 1 if it's less than 1
		if (pageNum <= 0 || pageSize <= 0) {
	    	pageNum = 1;
	    	pageSize = 10;
	    }
		
		// Remove unwanted characters from the searchTerm
	    String sanitizedSearchTerm = removeUnwantedCharacters(searchTerm);

	    // Encode the sanitized searchTerm to prevent XSS vulnerabilities
	    String encodedSearchTerm = Encode.forHtml(sanitizedSearchTerm);
		
        return indexing.newQuerySearchIndex(encodedSearchTerm, pageNum, pageSize);
	}
	
	// Helper method to remove unwanted characters from the searchTerm
	private String removeUnwantedCharacters(String searchTerm) {
	    // Remove characters '/', '<', and '>'
	    return searchTerm
	            .replace("/", "")
	            .replace("<", "")
	            .replace(">", "");
	}
}
