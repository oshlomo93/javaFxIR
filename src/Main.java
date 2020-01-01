import Parse.Parse;
import Searcher.Searcher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Parse parser = new Parse("C:\\Users\\omer\\Desktop\\Testing\\testCorpus" , "C:\\Users\\omer\\Desktop\\Testing\\posting", true);
        parser.parseAllDocs();
        Searcher searcher = new Searcher("politician", parser);
        searcher.start();
        System.out.println("Done");
    }
}
