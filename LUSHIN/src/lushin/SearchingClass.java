package lushin;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchingClass {

    private static final String INDEX_DIRECTORY = "/tmp/index";
    
    public void searchFunc() throws IOException, ParseException {
    	// Create an analyzer.
        Analyzer analyzer = new StandardAnalyzer();

        // Create an index reader.
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIRECTORY)));

        // Create an index searcher.
        IndexSearcher searcher = new IndexSearcher(reader);

        // Create a query.
        Query query = new QueryParser("content", analyzer).parse("This is");

        // Search the index.
        TopDocs results = searcher.search(query, 10);

        // Print the results.
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("content"));
        }

        // Close the index reader.
        reader.close();
    }
}
