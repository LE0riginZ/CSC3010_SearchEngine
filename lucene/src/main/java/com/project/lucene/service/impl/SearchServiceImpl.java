package com.project.lucene.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.lucene.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Override
	public List<String> retrieveSearchResults(String searchTerm) {
		// Create an ArrayList to store strings
        List<String> stringList = new ArrayList<>();

        // Add strings to the list
        stringList.add("Apple");
        stringList.add("Banana");
        stringList.add("Orange");
        stringList.add("Grapes");
        stringList.add("Mango");
		return stringList;
	}
	
}
