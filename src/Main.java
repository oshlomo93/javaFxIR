import Parse.Parse;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        boolean isStemmer = false;
        Parse parser = new Parse( "C:\\Users\\omer\\Desktop\\newCorpus" ,"C:\\Users\\omer\\Desktop\\newCorpus\\PostingFiles", isStemmer);
        //parser.reader.readFolder();
        System.out.println("Done read all the files");
        parser.setRead(parser.reader.allDocs);
        parser.parseAllDocs();
        System.out.println("Done parse all the docs");
        long end = System.currentTimeMillis();
        System.out.println("done in " + (end-start)/1000 + " seconds");
        //parser.exit();
        System.out.println("Done");
        parser.uploadDictionary();
    }
}