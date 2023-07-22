package com.project.lucene.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultItem {
	
	private int pageSize; // Number of result items per page
	private int currentPageNum; // Current page
	private int totalNumOfPages; // Total number of pages
    private int totalNumOfResults; // Total number of documents returned
	private List<DocumentItem> documents;

    public List<DocumentItem> getDocuments() {
        return documents;
    }

    public int getTotalNumOfResults() {
        return totalNumOfResults;
    }
}
