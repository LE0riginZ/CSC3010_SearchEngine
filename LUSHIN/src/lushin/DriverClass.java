package lushin;

public class DriverClass {

    public static void main(String[] args) throws Exception {

    	IndexingClass ic = new IndexingClass();
    	SearchingClass sc = new SearchingClass();
    	
        // Index the documents.
    	ic.runIndex();
    	
        // Search the index.
        sc.searchFunc();
    }
}

