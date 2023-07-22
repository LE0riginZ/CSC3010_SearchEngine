package com.project.lucene.service;

import com.project.lucene.model.ResultItem;

public interface SearchService {
	public ResultItem retrieveSearchResults(String searchTerm, Integer pageNum, Integer pageSize);
}
