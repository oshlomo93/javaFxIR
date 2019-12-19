package Parse;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is getting a path and read all the files within it
 */
public class ReadFile {

    private BufferedReader reader;
    private String line;
    private ArrayList<String> currentDoc;
    private String folderPath;
    public LinkedList<String[]> allDocs;
    int docCount = 0;



    public ReadFile(String from) {
        this.folderPath = from;
        allDocs = new LinkedList<>();
    }

    /**
     * Gets a folder and read all his sub-folders and sub-files
     */
    public void readFolder() {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isDirectory()) {
                    File f = new File(file.getPath());
                    File[] files;
                    files = f.listFiles();
                    if (files != null) {
                        for (File fl : files) {
                            readFile(fl);
                        }
                    }
                }
                if (file.isFile()) {
                    readFile(file);
                }
            }
        }
    }

    /**
     * Gets a file and reads all the documents inside it
     * @param f
     */
    private void readFile(File f) {
        try {
            if (reader != null) {
                reader.close();
            }
            FileReader fileReader = new FileReader(f);
            reader = new BufferedReader(fileReader);
            while((line = reader.readLine()) != null) {
                if(line.equals("<DOC>")) {
                    readDoc();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a document
     * @throws IOException
     */
    private void readDoc() throws IOException {
        currentDoc = new ArrayList<>();
        currentDoc.add(line);
        line = reader.readLine();
        currentDoc.add(line);
        while((line = reader.readLine()) != null && ! line.equals("<DOC>")) {
            currentDoc.add(line);
            if (line.equals("</DOC>")) {
                //currentDoc.add(line);
                addCurrentDoc();
                break;
            }
        }
    }

    /**
     * Adds a documents
     */
    private void addCurrentDoc() {
        String docName = getDocName(currentDoc.get(1));
        StringBuilder docText = new StringBuilder();
        int start = 10000;
        for (int i=0; i<currentDoc.size(); i++) {
            if (currentDoc.get(i).equals("<TEXT>")) {
                start = i;
            } else if (i > start) {
                docText.append(currentDoc.get(i));
            } else if (currentDoc.get(i).equals("</TEXT>")) {
                break;
            }
        }
        String[] node = new String[2];
        node[0] = docName;
        node[1] = docText.toString();
        allDocs.add(node);
    }

    /**
     * Getter for the document name
     * @param line
     * @return
     */
    private String getDocName(String line) {
        docCount += 1;
        String name = "" + docCount;
        return name;
    }
}
