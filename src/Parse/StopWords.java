package Parse;

import java.io.*;
import java.util.ArrayList;

public class StopWords {

    ArrayList <String> allStopWords;

    public StopWords(String path) throws IOException {
        if(path.length()> 0 &&  path != null){
            File newFile = new File(path);
                FileReader fileReader = new FileReader(newFile);
                BufferedReader reader = new BufferedReader(fileReader);
                allStopWords = new ArrayList<>();
                String stopWord= reader.readLine();
                while( stopWord != null){
                    allStopWords.add(stopWord);
                    stopWord =reader.readLine();
                }
        }
    }

    public boolean AmIStopWord(String word){
        if(allStopWords.contains(word)){
            return true;
        }
        String lowWord= word.toLowerCase();
        if(allStopWords.contains(lowWord)){
            return true;
        }
        return false;
    }

    public void delete(ArrayList<String> listOfWords ){
        if(listOfWords != null){
            int i= 0;
            while (!listOfWords.isEmpty() && i < listOfWords.size()){
                String word = listOfWords.get(i);
                if(AmIStopWord(word)){
                    listOfWords.remove(i);
                }
                else{
                    i++;
                }
            }
        }
    }

}
