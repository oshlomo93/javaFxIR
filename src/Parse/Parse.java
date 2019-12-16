package Parse;
import Indexer.Indexer;

import java.io.*;
import java.util.*;

public class Parse  {

    private Indexer indexer;
    private HashMap<String,Term> allTerms;
    private LinkedList<Document> allDocuments;
    private LinkedList<Document> allDocs;
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
    public ReadFile reader;
    private Hashtable<String, Term> entitiesTerms;
    private String postingPath;
    private boolean isStemmer;


    public Parse(String corpusPath , String postingPath, boolean isStemmer ){
        if(corpusPath != null && postingPath !=null ) {
            String[] pathOfIr = getPath(corpusPath);
            if (pathOfIr != null && pathOfIr.length == 2) {
                String corpusPath1 = pathOfIr[0];
                String stopWordsPath = pathOfIr[1];
                this.postingPath = postingPath ;
                reader = new ReadFile(corpusPath1);
                stopWords = new StopWords(stopWordsPath);
                allTerms = new HashMap<String, Term>();
                allDocs = new LinkedList();
                allDocuments = new LinkedList();
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
                entitiesTerms = new Hashtable<String, Term>();
                this.isStemmer = isStemmer;
            }
        }
    }

    public Parse(String postingPath) throws IOException {
        if (postingPath != null) {
            this.postingPath = postingPath;
            indexer = new Indexer();
            Map<String, String> sortedDict = uploadDictionary();
            indexer.setDictionary(sortedDict);
            HashMap<String, List<String[]>> pointers = uploadPointers();
            indexer.setPointers(pointers);
            allDocuments = uploadDocsDetails();
        }
    }

    private String[] getPath(String path) {
        File file = new File(path);
        String[] directories = file.list();
        String[] ans = new String[2];
        assert directories != null;
        ans[0] = path + "\\" + directories[0];
        ans[1] =  path + "\\stopwords.txt";
        return ans;
    }

    public void setRead(LinkedList<String[]> allFiles) {
        read = allFiles;
    }

    public void exit() {
        System.gc();
        File file = new File(postingPath);
        for (File f: file.listFiles()) {
            f.delete();
        }
    }
    public void parseAllDocs() throws IOException {
        long start = System.currentTimeMillis();
        reader.readFolder();
        setRead(reader.allDocs);
        String[] currentDoc;
        int size = 3000;
        int counter = 0;
        while (read.size() != 0) {
            currentDoc = read.get(0);
            startParseDocument(currentDoc[0], currentDoc[1]);
            counter++;
            read.remove();
            if (counter%size == 0) {
                update();
            }
        }
        if (counter%size != 0) {
            update();
        }
        indexer.deleteFromDict(entity.listOfEntity);
        System.out.println("Done parse all the docs");
        long end = System.currentTimeMillis();
        System.out.println("done in " + (end-start)/1000 + " seconds");
        System.out.println("Done");
        writeAllDocuments();
        indexer.writeDictToDisk(postingPath);
        //indexer.printDict();
    }


    private void update() throws IOException {
        indexer.updateAll(termsAndDocs, allDocs, postingPath);
        cleanDocs();
        allDocuments.addAll(allDocs);
        allDocs.clear();
        termsAndDocs.clear();
        allTerms.clear();
    }

    private void cleanDocs() {
        for (Document doc : allDocs) {
            doc.clean();
        }
    }

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
        return pointers;
    }

    public LinkedList<Document> uploadDocsDetails() throws IOException {
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
        return docs;
    }

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
        return dict;
    }

    private void showDictionary() throws IOException {
        //todo
    }

    private void writeAllDocuments() throws IOException {
        File documentsDetails = new File(postingPath + "\\documentsDetails.txt");
        FileWriter writer = new FileWriter(documentsDetails, true);
        for (Document doc: allDocuments) {
            String docDetails = doc.toString();
            writer.write(docDetails);
            writer.write("\n");
        }
        writer.close();
    }

    private void startParseDocument(String documentName , String documentText) {
        if (documentName != null && documentName.length() > 0 && documentText != null && documentText.length() > 0) {
            Document document = new Document(documentName);
            ArrayList<String> allSentences = splitDoc(documentText);
            assert allSentences != null;
            int j = 0;
            for ( String s: allSentences) {
                String [] allWords = s.split(" ");
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
                    }
                }
            }
        }
        return allSentences;

    }

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
                    //todo - we need to stem
                    term = entity.makeTerm(word);
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
                    //todo - we need to stem
                    term = upLowLetter.makeTerm(word);
                    break;
            }
        }
        return term;
    }

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

    private boolean isEndOfLine(String word){
        ArrayList<Character> charToDelete = new ArrayList<>();
        char [] deletChar= {',','.','"',':',';','!','?', '@','#', '&', ')', '}' ,'-' , '|'};
        for (char c : deletChar) {
            charToDelete.add(c);
        }
        if(word.length()>0){
            return charToDelete.contains(word.charAt(word.length() - 1));
        }
        return false;
    }

    private boolean isNewLine(String word){
        ArrayList<Character> charToDelete = new ArrayList<>();
        char [] deleteChar= {',','.','"',':',';','!','?', '@','#', '&', '(' ,'-' , '{','|'};
        for (char c : deleteChar) {
            charToDelete.add(c);
        }
        if(word.length()>0){
            return charToDelete.contains(word.charAt(0));
        }
        return false;
    }

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

    private boolean asBackSlashN(String word){
        if (word.length() > 2) {
            if (word.substring(0, 2).equals("\n")) {
                return true;
            }
            return word.substring(word.length() - 2).equals("\n");
        }
        return false;
    }

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

    //public int getMaxWordInDic(){
    //    int val = 0;
    //    for(Term t : allTerms.values()){
    //        if(t.numOfUniqueDoc()>val){
    //            val = t.numOfUniqueDoc();
    //        }
    //    }
    //    return val;
    //}

    //public void sortTerms() {
    //     Collections.sort(termsAndDocs, (o1, o2) -> {
    //        if (o1[0].equals(o2[0]))
    //            return 0;
    //        else if (o1[0].charAt(0) != o2[0].charAt(0))
    //            return (o1[0].charAt(0) > o2[0].charAt(0) ? 1 : -1);
    //        else {
    //            int len1 = o1[0].length();
    //            int len2 = o2[0].length();
    //            int len = Math.min(len1, len2);
    //            for(int i=0; i<len; i++) {
    //                if (o1[0].charAt(i) != o2[0].charAt(i)) {
    //                        return (o1[0].charAt(i) > o2[0].charAt(i) ? 1 : -1);
    //                }
    //            }
    //            if (len == len1)
    //                return 1;
    //            else
    //                return -1;
    //        }
    //    });
    //}

    //Replace the double hyphen with a comma
    private String[] replaceDoubleHyphenToComma(String text){
        if(text!= null  && text.length()>0) {
            String[] deleteDoubleHyphen = text.split("--");
            return deleteDoubleHyphen;
        }
        return null;
    }

}