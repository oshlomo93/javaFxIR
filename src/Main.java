import GUI.View;
import Parse.Parse;
import Searcher.Searcher;
import javafx.event.ActionEvent;
import Searcher.ReadQueries;
import Searcher.IdentifyEntityInDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {

      Parse parser = new Parse("C:\\Users\\omer\\Desktop\\Testing\\testCorpus" , "C:\\Users\\omer\\Desktop\\Testing\\posting", true);
      parser.parseAllDocs();
      Searcher searcher = new Searcher("Falkland petroleum exploration", parser, false);
      searcher.start();
      //HashMap<String, ArrayList<String>> allDoc =searcher.getResults();
      //System.out.println(allDoc.size());

       //IdentifyEntityInDocument id = new IdentifyEntityInDocument(parser.getPostingPath());
       //id.getAllEntities( "FBIS3-13");
       //id.getTopEntities();
       System.out.println("Done");

//        ReadQueries reader = new ReadQueries("C:\\Users\\omer\\Desktop\\queries.txt");
//        reader.readQueries();
//        System.out.println("done");


    }
}
