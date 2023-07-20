package lushin;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexingClass {

    private static final String INDEX_DIRECTORY = "/tmp/index";
    
    public void runIndex() throws IOException {
    	// Create an analyzer.
        Analyzer analyzer = new StandardAnalyzer();

        // Create a directory for the index.
        Directory directory =  FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Create an index writer.
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);

        // Add documents to the index.
        Document doc = new Document();
        doc.add(new Field("content", "This is the text to be indexed.", TextField.TYPE_STORED));
        iwriter.addDocument(doc);

        // Close the index writer.
        iwriter.close();
    }
}