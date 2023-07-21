package com.project.lucene.service;

import java.util.List;

public interface SearchService {
	public List<String> retrieveSearchResults(String searchTerm);
}
