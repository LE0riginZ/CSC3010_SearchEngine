package com.project.lucene.util;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.project.lucene.model.DocumentItem;
import com.project.lucene.model.ResultItem;

@Component
public class Indexing {
	
	private Directory memoryIndex;
	private StandardAnalyzer analyzer;
	
	public Indexing(@Qualifier("indexDirectory") Directory memoryIndex, StandardAnalyzer analyzer) {
        super();
        this.memoryIndex = memoryIndex;
        this.analyzer = analyzer;
    }
		
	/**
     * 
     * @param title
     * @param body
     */
    public void indexDocument(String title, String body, String url) {

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        try {
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
            Document document = new Document();

            document.add(new TextField("title", title, Field.Store.YES));
            document.add(new TextField("body", body, Field.Store.YES));
            document.add(new TextField("url", url, Field.Store.YES));
            document.add(new SortedDocValuesField("title", new BytesRef(title)));

            writter.addDocument(document);
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Document> searchIndex(String inField, String queryString) {
        try {
            Query query = new QueryParser(inField, analyzer).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            

            
            TopDocs topDocs = searcher.search(query, 10);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultItem searchIndex(String inField, String queryString, int currentPageNum, int pageSize) {
        try {
            Query query = new QueryParser(inField, analyzer).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            // Calculate the starting index of the documents for the given page
            int start = (currentPageNum - 1) * pageSize;

            // Perform the search and retrieve documents from the specified page
            TopDocs topDocs = searcher.search(query, start + pageSize);
            ScoreDoc[] hits = topDocs.scoreDocs;

            List<Document> documents = new ArrayList<>();
            for (int i = start; i < Math.min(hits.length, start + pageSize); i++) {
                Document doc = searcher.doc(hits[i].doc);
                documents.add(doc);
            }

            // Convert TopDocs.totalHits to int
            int totalNumOfResults = Math.toIntExact(topDocs.totalHits.value);
            
            // Calculate the number of pages
            int totalNumOfPages = (int) Math.ceil((double) totalNumOfResults / pageSize);
            
            return new ResultItem(pageSize, currentPageNum, totalNumOfPages, totalNumOfResults, parseDocumentList(documents));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Converts lucene's Document to our DocumentItem
    public DocumentItem parseDocument(Document doc) {
        // Extract field values from the Document object
        String title = doc.get("title");
        String content = doc.get("body");
        String url = doc.get("url");

        // Create a new DocumentItem object with the extracted values
        DocumentItem documentItem = new DocumentItem();
        documentItem.setTitle(title);
        documentItem.setContent(content);
        documentItem.setUrl(url);

        return documentItem;
    }
    
    public List<DocumentItem> parseDocumentList(List<Document> documents) {
        List<DocumentItem> documentItems = new ArrayList<>();

        // Loop through the list of Lucene Documents and parse each into DocumentItem
        for (Document doc : documents) {
            DocumentItem documentItem = parseDocument(doc);
            documentItems.add(documentItem);
        }
        return documentItems;
    }
    
    // Method to use multiple fields in one query
    public ResultItem querySearchIndex(String queryString, int currentPageNum, int pageSize) {
        try {
        	
        	String[] searchFields = new String[]{"title", "body"};
        	MultiFieldQueryParser queryParser = new MultiFieldQueryParser(searchFields, analyzer);
            Query query = queryParser.parse(queryString);
            
            System.out.println(query);
            
            // Calculate the starting index of the documents for the given page
            int start = (currentPageNum - 1) * pageSize;

            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
//            TopDocs topDocs = searcher.search(query, 10);
            
//            Apply title boosting here
//            float titleBoost = 2.0f; // Adjust the boosting factor based on your requirement
//            query = applyTitleBoost(query, titleBoost);
            
            float k1 = 1.5f;
            float b = 0.75f;
            
            searcher.setSimilarity(new BM25Similarity(k1, b));
            
//           Perform the search and retrieve documents from the specified page
            TopDocs topDocs = searcher.search(query, start + pageSize);
            ScoreDoc[] hits = topDocs.scoreDocs;
//            List<Document> documents = new ArrayList<>();
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                documents.add(searcher.doc(scoreDoc.doc));
//            }
            
            List<Document> documents = new ArrayList<>();
            for (int i = start; i < Math.min(hits.length, start + pageSize); i++) {
                Document doc = searcher.doc(hits[i].doc);
                documents.add(doc);
            }

            // Convert TopDocs.totalHits to int
            int totalNumOfResults = Math.toIntExact(topDocs.totalHits.value);
            
            // Calculate the number of pages
            int totalNumOfPages = (int) Math.ceil((double) totalNumOfResults / pageSize);
            
            return new ResultItem(pageSize, currentPageNum, totalNumOfPages, totalNumOfResults, parseDocumentList(documents));


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;

    }   
    
//    private Query applyTitleBoost(Query query, float titleBoost) {
//        // Clone the original query to avoid modifying it directly
//        Query clonedQuery = query.clone();
//
//        // Create a BooleanQuery with the original query as "SHOULD" clause and the title query as "MUST" clause
//        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
//        booleanQuery.add(clonedQuery, BooleanClause.Occur.SHOULD); // Original query as SHOULD
//        booleanQuery.add(new BoostQuery(clonedQuery, titleBoost), BooleanClause.Occur.MUST); // Title query as MUST with boost
//
//        return booleanQuery.build();

    
    

    public void deleteDocument(Term term) {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
            writter.deleteDocuments(term);
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Document> searchIndex(Query query) {
        try {
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Document> searchIndex(Query query, Sort sort) {
        try {
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10, sort);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    
	
}
