# CSC3010_SearchEngine
Group 1 Repo for Info Retrieval Search Engine project using Apache Lucene

## Lucene tied with Spring Boot Project (9.7.0)

Files for (Indexing and Searching Functionalities):
1. com.project.lucene -> RunIndexer.java
2. com.project.lucene.util -> Indexing.java and LuceneFileSearch.java

## Folders

**NLP**: Contains python script for Lemmatizer for JSON data and NLP text processing to produce txt files with terms in bag of words format

**data_directory**: Contains JSONs containing documents crawled from www.encyclopedia.com 

**index_directory**: Contains index files, indexed by Lucene

**google_toptwenty**: Contains Google_TopTwenty_Crawler.py and the JSONs with top twenty Google searches for specific keywords

**lucene**: Contains Lucene configuration source codes. Includes the sources codes for Indexing and Searching functionalities

**scrapy_crawler_project**: Contains the Python Scrapy crawler used to crawl data from www.encyclopedia.com 

**vue-frontend/lucene_search_project**: Contains the Frontend of the Search Engine

## Things required

1. Crawled data from www.encyclopedia.com to be stored in data_directory
2. System Requirements: JAVA 11 or higher 



### Instructions
1. Indexing
   - Call indexJson(directoryPath) method to index files from specified directory
  
2. Run Frontend
   - Abadi help to put instructions



