package Parse;
import Indexer.Indexer;
import Stemmer.Stemmer;

import java.io.*;
import java.util.*;

/**
 * This class receives long text and generates terms it recognizes by the rules,
 * thus generating all the indexer and all files that store the information in the ram
 */
public class Parse  {

    public Indexer indexer;
    private HashMap<String,Term> allTerms;
    public LinkedList<Document> allDocs;
    private int counter = 0;
    private StopWords stopWords ;
    private NumNoUnits numNoUnits;
    private Date date;
    private Entity entity;
    private Expression expression;
    private Percent percent;
    private Price price;
    private UpLowLetter upLowLetter;
    private Countries countries;
    private UnknownType unknownType;
    private LinkedList<String[]> read;
    private List<String[]> termsAndDocs;
    private ReadFile reader;
    private String postingPath;
    private boolean isStemmer;
    private Stemmer stemmer;
    private ArrayList<Character> isNewLineCharToDelete;
    private ArrayList<Character> isEndOfLineCharToDelete;


    public Parse(String corpusPath , String postingPath, boolean isStemmer ) throws IOException {
        if(corpusPath != null && postingPath !=null ) {
            String[] pathOfIr = getPath(corpusPath);
            if (pathOfIr != null && pathOfIr.length == 2) {
                String corpusPath1 = pathOfIr[0];
                String stopWordsPath = pathOfIr[1];
                this.postingPath = getPostingPath(postingPath, isStemmer) ;
                reader = new ReadFile(corpusPath1);
                stopWords = new StopWords(stopWordsPath);
                if(stopWords ==null){
                    return;
                }
                allTerms = new HashMap<String, Term>();
                allDocs = new LinkedList();
                numNoUnits = new NumNoUnits();
                date = new Date();
                entity = new Entity();
                expression = new Expression();
                percent = new Percent();
                price = new Price();
                upLowLetter = new UpLowLetter();
                countries = new Countries();
                unknownType = new UnknownType();
                termsAndDocs = new ArrayList<>();
                indexer = new Indexer();
                this.isStemmer = isStemmer;
                stemmer = new Stemmer();
                isNewLineCharToDelete = new ArrayList<>();
                char [] deleteChar= {',','.','"',':',';','!','?', '@','#', '&', '(' ,'-' , '{','|', '*' , '+'};
                for (char c : deleteChar) {
                    isNewLineCharToDelete.add(c);
                }
                deleteChar = null;
                isEndOfLineCharToDelete = new ArrayList<>();
                char [] deletChar= {',','.','"',':',';','!','?', '@','#', '&', ')', '}' ,'-' , '|'};
                for (char c : deletChar) {
                    isEndOfLineCharToDelete.add(c);
                }
                deletChar = null;

            }
        }
    }



    public Parse(String postingPath, boolean selected) throws IOException {
        isStemmer =selected;
        if (postingPath != null) {
            if(isStemmer) {
                this.postingPath = postingPath+"\\WithStemming";
            }
            else {

                this.postingPath = postingPath+"\\WithoutStemming";

            }
            indexer = new Indexer();
            Map<String, String> sortedDict = uploadDictionary();
            indexer.setDictionary(sortedDict);
            HashMap<String, List<String[]>> pointers = uploadPointers();
            indexer.setPointers(pointers);
            allDocs = uploadDocsDetails(isStemmer);
            isStemmer = selected;
            counter = allDocs.size();
            System.out.println("The upload finished successfully");


        }
    }

    public String getPostingPath() {
        return postingPath;
    }

    /**
     * Gets the correct posting path from the user's input
     * @param postingPath
     * @param isStemmer
     * @return
     */
    private String getPostingPath(String postingPath, boolean isStemmer) {
        if (isStemmer) {
            postingPath += "\\WithStemming";
            File newFolder = new File(postingPath);
            newFolder.mkdirs();
        }
        else {
            postingPath += "\\WithoutStemming";
            File newFolder = new File(postingPath);
            newFolder.mkdirs();
        }
        return postingPath;
    }

    /**
     * Gets the corpus and stop words path from the user's input
     * @param path
     * @return
     */
    private String[] getPath(String path) {
        File file = new File(path);
        String[] directories = file.list();
        String[] ans = new String[2];
        assert directories != null;
        ans[0] = path + "\\" + directories[0];
        ans[1] =  path + "\\stop_words 05.txt";
        return ans;
    }

    /**
     * Setter for the files that need to be parsed
     * @param allFiles
     */
    public void setRead(LinkedList<String[]> allFiles) {
        read = allFiles;
    }

    /**
     * Delete all the files from the posting path
     * @param selected
     */
    public void exit(boolean selected) {
        System.gc();
        String strExit;
        File file = new File(postingPath);
        for (File f: file.listFiles()) {
            f.delete();
        }
        file.delete();

    }

    /**
     * Parse all the documents and send them to the indexer
     * @throws IOException
     */
    public void parseAllDocs() throws IOException {
        long start = System.currentTimeMillis();
        reader.readFolder();
        setRead(reader.allDocs);
        counter = read.size();
        String[] currentDoc;
        int size = 3000;
        int counterall = 0;
        while (read.size() != 0) {
            currentDoc = read.get(0);
            startParseDocument(currentDoc[0], currentDoc[1]);
            counterall++;
            read.remove();
            if (counterall%size == 0) {
                update();
            }
        }
        if (counterall%size != 0) {
            update();
        }
        indexer.deleteFromDict(entity.listOfEntity);
        System.out.println("Done parse all the docs");
        long end = System.currentTimeMillis();
        System.out.println("done in " + (end-start)/1000 + " seconds");
        System.out.println("Done");
        indexer.writeDictToDisk(postingPath);
        System.out.println(indexer.getSortedDict().size());
    }

    /**
     * Sends the indexer new data to update
     * @throws IOException
     */
    private void update() throws IOException {
        indexer.updateAll(termsAndDocs, allDocs, postingPath, isStemmer);
        termsAndDocs.clear();
        allTerms.clear();
        writeAllDocuments();
        counter = counter+ allDocs.size();
        allDocs.clear();
    }

    /**
     * Getter for the indexer Dictionary
     * @return
     */
    public Map<String, String> getSortedDict() {
        return indexer.getSortedDict();
    }

    /**
     * Upload the pointers file
     * @return
     * @throws IOException
     */
    public HashMap<String, List<String[]>> uploadPointers() throws IOException {
        HashMap<String, List<String[]>> pointers = new HashMap<>();
        File file = new File(postingPath + "\\Pointers.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] words;
        while ((line = br.readLine()) != null) {
            words = line.split("; ");
            LinkedList<String[]> values = new LinkedList<>();
            for (int i=1; i < words.length; i += 2) {
                String[] pointer = new String[2];
                pointer[0] = words[i];
                pointer[1] = words[i+1];
                values.add(pointer);
            }
            pointers.put(words[0], values);
        }
        System.out.println("The pointers uploaded successfully");
        return pointers;
    }

    /**
     * Upload the file with all the documents details
     * @param isStemmer
     * @return
     * @throws IOException
     */
    public LinkedList<Document> uploadDocsDetails(boolean isStemmer) throws IOException {
        //todo isStemmer
        LinkedList<Document> docs = new LinkedList();
        File file = new File(postingPath + "\\documentsDetails.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] words;
        while ((line = br.readLine()) != null) {
            words = line.split(";");
            Document doc = new Document(words[0], words[1], words[2], words[3]);
            docs.add(doc);
        }
        System.out.println("The documents details uploaded successfully");

        return docs;
    }

    /**
     * Upload the Dictionary file
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadDictionary() throws IOException {
        Map<String, String> dict = new TreeMap<>();
        File file = new File(postingPath + "\\Dictionary.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] words;
        while ((line = br.readLine()) != null) {
            words = line.split(", ");
            dict.put(words[0], words[1]);
        }
        System.out.println("The dictionary uploaded successfully");
        return dict;
    }

    /**
     * Writes all the documents details to the Disk
     * @throws IOException
     */
    private void writeAllDocuments() throws IOException {
        File documentsDetails = new File(postingPath + "\\documentsDetails.txt");
        FileWriter writer = new FileWriter(documentsDetails, true);
        for (Document doc: allDocs) {
            String docDetails = doc.toString();
            writer.write(docDetails);
            writer.write("\n");
        }
        writer.close();
    }

    /**
     * Gets a documents and parsing all the terms
     * @param documentName
     * @param documentText
     */
    public void startParseDocument(String documentName , String documentText) {
        if (documentName != null && documentName.length() > 0 && documentText != null && documentText.length() > 0) {
            Document document = new Document(documentName);
            ArrayList<String> allSentences = splitDoc(documentText);
            assert allSentences != null;
            int j = 0;
            for ( String s: allSentences) {
                String [] allWords = s.split(" ");
                document.setNumOfWords(allWords.length);
                int i = 0;
                while (i < allWords.length) {
                    if (i+3 < allWords.length) {
                        String type = findWhoIAmFour(allWords[i], allWords[i+1], allWords[i+2], allWords[i+3]);
                        if (type != null) {
                            String termName = allWords[i] + " " + allWords[i+1] + " " + allWords[i+2] + " " + allWords[i+3];
                            createTerm(document, type, termName , j);
                            i += 4;
                            j =j +4;
                            continue;
                        }
                    }

                    if(i+2 < allWords.length) {
                        String type = findWhoIAmThree(allWords[i], allWords[i+1], allWords[i+2]);
                        if (type != null) {
                            String termName = allWords[i] + " " + allWords[i+1] + " " + allWords[i+2];
                            createTerm(document, type, termName , j);
                            i += 3;
                            j =j +3;
                            continue;
                        }
                    }

                    if (i+1 < allWords.length) {
                        String type = findWhoIAmTwo(allWords[i], allWords[i+1]);
                        if (type != null) {
                            String termName = allWords[i] + " " + allWords[i+1];
                            createTerm(document, type, termName , j);
                            i += 2;
                            j =j +2;

                            continue;
                        }
                    }
                    String type = findWhoIAmOne(allWords[i]);
                    if (type != null) {
                        String termName = allWords[i];
                        Term term = makeTerm(type, termName);
                        if(term != null) {
                            createTerm(document, type, termName , j);
                            i += 1;
                            j =j +1;

                            continue;
                        }
                    }
                    i++;
                    j++;
                }
            }
            allDocs.add(document);
        }
    }

    /**
     * Gets the text of a documents and split it to sentences
     * @param text
     * @return
     */
    private ArrayList<String> splitDoc(String text){
        ArrayList<String> allSentences = null;
        if(text != null && text.length()>0){
            allSentences = new ArrayList<>();
            String [] noHStrings= replaceDoubleHyphenToComma(text);
            for(String part : noHStrings) {
                String[] allWords = part.split(" ");
                String sentence = "";
                for (String word : allWords) {
                    if (word.length() > 0) {
                        String cleanWord = cleanBackSlashN(word);
                        if (isNewLine(cleanWord) && isEndOfLine(cleanWord)) {
                            if (sentence.length() > 0) {
                                allSentences.add(sentence);
                                sentence = cleanString(cleanWord);
                                if (sentence.length() > 0) {
                                    allSentences.add(sentence);
                                    sentence = "";
                                }
                            }
                        } else if (isNewLine(cleanWord)) {
                            if (sentence.length() > 0) {
                                allSentences.add(sentence);
                            }
                            cleanWord = cleanString(cleanWord);
                            if (cleanWord.length() > 0) {
                                sentence = cleanWord;
                            }
                        } else if (isEndOfLine(cleanWord)) {
                            cleanWord = cleanString(cleanWord);
                            if (sentence.length() > 0 && cleanWord.length() > 0) {
                                sentence = sentence + " " + cleanWord;
                                allSentences.add(sentence);
                                sentence = "";
                                continue;
                            }
                            if (sentence.length() > 0) {
                                allSentences.add(sentence);
                                sentence = "";
                                continue;
                            }
                            if (cleanWord.length() > 0) {
                                allSentences.add(cleanWord);
                            }
                        } else {
                            if (sentence.length() > 0) {
                                sentence = sentence + " " + cleanWord;
                            } else {
                                sentence = cleanWord;
                            }
                        }
                        if (word.equals(allWords[allWords.length-1])){
                            allSentences.add(sentence);
                        }
                    }
                }
            }
        }
        return allSentences;

    }

    /**
     * Update the data after a creation of new term
     * @param document
     * @param type
     * @param termName
     * @param position
     */
    private void createTerm(Document document, String type, String termName , int position) {
        Term term = makeTerm(type, termName);
        if (term != null ) {
            if (allTerms.containsKey(termName)) {
                term = allTerms.get(termName);
            }
            document.addTerm(term , position);
            term.add(document.Id);
            String[] newTerm = new String[2];
            newTerm[0] = term.getTerm();
            newTerm[1] = document.getId();
            allTerms.put(termName, term);
            termsAndDocs.add(newTerm);
        }
    }

    /**
     * Creates new term
     * @param role
     * @param word
     * @return
     */
    private Term makeTerm(String role , String word ){
        Term term = null;
        if (role == null && word != null) {
            term = unknownType.makeTerm(word);
        }
        if(word != null && word.length() > 0 && role != null && role.length() >0 ){
            if (stopWords.AmIStopWord(word))
                return null;
            switch (role) {
                case "NumNoUnits":
                    term = numNoUnits.makeTerm(word);
                    break;
                case "Date":
                    term = date.makeTerm(word);
                    break;
                case "Countries":
                    term = countries.makeTerm(word);
                    break;
                case "Entity":
                    if (isStemmer) {
                        String stemmedWord = stem(word);
                        term = entity.makeTerm(stemmedWord);
                    }
                    else {
                        term = entity.makeTerm(word);
                    }
                    break;
                case "Expression":
                    term = expression.makeTerm(word);
                    break;
                case "Percent":
                    term = percent.makeTerm(word);
                    break;
                case "Price":
                    term = price.makeTerm(word);
                    break;
                case "UpLowLetter":
                    if (isStemmer) {
                        String stemmedWord = stem(word);
                        term = upLowLetter.makeTerm(stemmedWord);
                    }
                    else {
                        term = upLowLetter.makeTerm(word);
                    }
                    break;
            }
        }
        return term;
    }

    /**
     * Use the stemmer to stem a word
     * @param word
     * @return
     */
    private String stem(String word) {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        String stemmedWord = stemmer.toString();
        return  stemmedWord;
    }

    /**
     * Search what is the right term type for a word
     * @param word
     * @return
     */
    private String findWhoIAmOne(String word){
        String iAm = null;
        if(numNoUnits.amIThis(word)){
            iAm = "NumNoUnits";
        }
        else if(date.amIThis(word)){
            iAm = "Date";
        }
        else if(entity.amIThis(word)){
            iAm = "Entity";
        }
        else if(countries.amIThis(word)){
            iAm = "Countries";
        }
        else if(expression.amIThis(word)){
            iAm = "Expression";
        }
        else if(percent.amIThis(word)){
            iAm = "Percent";
        }
        else if(price.amIThis(word)){
            iAm = "Price";
        }
        else if(upLowLetter.amIThis(word)){
            iAm = "UpLowLetter";
        }
        return iAm;
    }

    /**
     * Search what is the right term type of two words
     * @param wordOne
     * @param wordTwo
     * @return
     */
    private String findWhoIAmTwo(String wordOne , String wordTwo ){
        String iAm = null;
        if(numNoUnits.amIThis(wordOne , wordTwo)){
            iAm = "NumNoUnits";
        }
        else if(date.amIThis(wordOne , wordTwo)){
            iAm = "Date";
        }
        else if(countries.amIThis(wordOne, wordTwo )){
            iAm = "Countries";
        }
        else if(entity.amIThis(wordOne , wordTwo)){
            iAm = "Entity";
        }
        else if(expression.amIThis(wordOne , wordTwo)){
            iAm = "Expression";
        }
        else if(percent.amIThis(wordOne , wordTwo)){
            iAm = "Percent";
        }
        else if(price.amIThis(wordOne , wordTwo)){
            iAm = "Price";
        }
        else if(upLowLetter.amIThis(wordOne , wordTwo)){
            iAm = "UpLowLetter";
        }
        return iAm;
    }

    /**
     * Search what is the right term type of three words
     * @param word1
     * @param word2
     * @param word3
     * @return
     */
    private String findWhoIAmThree(String word1, String word2, String word3) {
        String iAm = null;
        if(numNoUnits.amIThis(word1 , word2, word3)){
            iAm = "NumNoUnits";
        }
        else if(date.amIThis(word1 , word2, word3)){
            iAm = "Date";
        }
        else if(countries.amIThis(word1, word2 , word3)){
            iAm = "Countries";
        }
        else if(entity.amIThis(word1 , word2, word3)){
            iAm = "Entity";
        }
        else if(expression.amIThis(word1 , word2, word3)){
            iAm = "Expression";
        }
        else if(percent.amIThis(word1 , word2, word3)){
            iAm = "Percent";
        }
        else if(price.amIThis(word1 , word2, word3)){
            iAm = "Price";
        }
        else if(upLowLetter.amIThis(word1 , word2, word3)){
            iAm = "UpLowLetter";
        }
        return iAm;
    }

    /**
     * Search what is the right term type of four words
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return
     */
    private  String findWhoIAmFour(String word1, String word2, String word3, String word4){
        String iAm = null;
        if(numNoUnits.amIThis(word1 , word2, word3, word4)){
            iAm = "NumNoUnits";
        }
        else if(date.amIThis(word1 , word2, word3, word4)){
            iAm = "Date";
        }
        else if(entity.amIThis(word1 , word2, word3, word4)){
            iAm = "Entity";
        }
        else if(expression.amIThis(word1 , word2, word3, word4)){
            iAm = "Expression";
        }
        else if(percent.amIThis(word1 , word2, word3, word4)){
            iAm = "Percent";
        }
        else if(price.amIThis(word1 , word2, word3, word4)){
            iAm = "Price";
        }
        else if(upLowLetter.amIThis(word1 , word2, word3, word4)){
            iAm = "UpLowLetter";
        }
        return iAm;
    }


    /** Checks whether a new row can be split
     * @param word
     * @return boolean
     */
    private boolean isEndOfLine(String word){
        if(word.length()>0){
            return isEndOfLineCharToDelete.contains(word.charAt(word.length() - 1));
        }
        return false;
    }


    /** Checks whether a new row can be split
     * @param word
     * @return boolean
     */
    private boolean isNewLine(String word){
        if(word.length()>0){
            return isNewLineCharToDelete.contains(word.charAt(0));
        }
        return false;
    }

    /**
     * This function gets a string and removes all non-meaningful characters
     * @param word
     * @return String
     */
    private String cleanString(String word){
        if(word.length() ==1 ){
            if(isEndOfLine(word) || isNewLine(word)){
                return "";
            }
        }
        if(word.length() >1){
            while(word.length() > 0 && (isEndOfLine(word) || isNewLine(word))){
                if(isNewLine(word)){
                    word = word.substring(1);
                }
                else{
                    word = word.substring(0 , word.length()-1);
                }
            }
        }
        return word;
    }

    /** This function clears the document from blank lines
     * @param word
     * @return boolean
     */
    private boolean asBackSlashN(String word){
        if (word.length() > 2) {
            if (word.substring(0, 2).equals("\n")) {
                return true;
            }
            return word.substring(word.length() - 2).equals("\n");
        }
        return false;
    }

    /** This function clears the document from blank lines
     * @param word
     * @return String
     */
    private String cleanBackSlashN(String word){
        String toReturn = word;
        while (toReturn.length() >1 && asBackSlashN(toReturn)){
            if(toReturn.substring(0,2).equals("\n")){
                toReturn = toReturn.substring(2);
            }
            else if(toReturn.substring(toReturn.length()-2).equals("\n")){
                toReturn = toReturn.substring(0,toReturn.length()-2);
            }
        }
        return toReturn;
    }

    /**
     * Replace the double hyphen with a comma
     * @param text
     * @return String[]
     */
    private String[] replaceDoubleHyphenToComma(String text){
        if(text!= null  && text.length()>0) {
            String[] deleteDoubleHyphen = text.split("--");
            return deleteDoubleHyphen;
        }
        return null;
    }

    /**
     * Accept the number of documents
     * @return int
     */
    public int getNumofDoc() {
        return counter;
    }
}