package Parse;

/**
 * This class produces number-type terms without units and converts the numbers by the rules
 */
public class NumNoUnits implements IRules {

    String [] PrefixOfNumber = {"Thousand", "Million" , "Billion" , "K" , "M" , "B"};

    private char NumMark(double num){
        if(num >= 1000 && num <1000000 ) {
            return 'K';
        }
        else if(num >= 1000000 && num <1000000000 ) {
            return 'M';
        }
        else if(num >= 1000000000 ) {
            return 'B';
        }
        return '-';
    }

    /**
     * Get a word and check if it represents a number
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word){
        if(word!=null && word.length()>0){
            char [] wordChar = word.toCharArray();
            for(int i = 0 ; i < wordChar.length ; i++){
                if( wordChar[i] != ',' &&  wordChar[i] != '.') {
                    if (48 > wordChar[i] || wordChar[i] > 57) {
                        if (i == wordChar.length - 1) {
                            if (wordChar[i] == 'B' && wordChar[i] == 'M' && wordChar[i] == 'K') {
                                return true;
                            }
                            return false;
                        }
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get 2 words and check if they represent a number
     * @param wordone
     * @param wordtwo
     * @return boolean
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        if(amIThis(wordone)){
            for(int i= 0 ; i <PrefixOfNumber.length ; i++){
                if(PrefixOfNumber[i].equals(wordtwo)){
                    return true;
                }
            }
            if(isFun(wordtwo)){
                return true;
            }
        }
        return false;
    }

    /**
     * Get 3 words and check if they represent a number
     * @param word1
     * @param word2
     * @param word3
     * @return boolean
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        return false;
    }

    /**
     * Get 4 words and check if they represent a number
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return boolean
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return false;
    }


    /**
     * You get a string and check if it represents a fracture
     * @param word -String
     * @return boolean
     */
    private boolean isFun(String word){
        if(word != null && word.length()>0) {
            String [] nums = word.split("/");
            if (nums.length > 1) {
                if((isANum(nums[0]) && isANum(nums[1]))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the string is a number
     * @param word
     * @return
     */
    private boolean isANum(String word){
        if(word != null && word.length()>0) {
            for (int i = 0; i < word.length(); i++) {
                if(i!=0 && i!= word.length()-1) {
                    if (48 > word.charAt(i) || word.charAt(i) > 57 || word.charAt(i) != ',') {
                        return false;
                    }
                }
                else if(word.charAt(i) != '.' || word.charAt(i) !=','){
                    if (48 > word.charAt(i) || word.charAt(i) > 57){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Accept a string and get a number type term without units
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        Term term = null;
        if(word != null && word.length() > 0 ){
            String [] words = word.split(" ");
            if(words.length == 1) {
                if (amIThis(words[0])) {
                    if(words[0].charAt(words.length-1) == 'B' && words[0].charAt(words.length-1) == 'M' && words[0].charAt(words.length-1) == 'K' ){
                        words[0] = words[0].substring(0 , words[0].length()-2);
                    }
                    if(isANum(words[0])) {
                        double num = realNumber(words[0]);
                        char p = NumMark(num);
                        if (num >= 1000 && num < 1000000) {
                            num = num / 1000;
                        } else if (num >= 1000000 && num < 1000000000) {
                            num = num / 1000000;
                        } else if (num >= 1000000000) {
                            num = num / 1000000000;
                        }
                        String numWord = String.format("%.3f", num);
                        if (p != '-') {
                            numWord = numWord + p;
                        }
                        term = new Term(numWord, "NumNoUnits");
                        return term;
                    }
                }
            }
            else if(words.length == 2){
                if(amIThis(words[0] , words[1])) {
                    if (words[1].equals( "K") || words[1].equals( "Thousand")) {
                        term = new Term(words[0]+"K" ,"NumNoUnits");
                        return term;
                    }
                    else if (words[1].equals( "M") || words[1].equals( "Million")) {
                        term = new Term(words[0]+"M" , "NumNoUnits");
                        return term;

                    }
                    else if( words[1].equals( "B") || words[1].equals( "Billion")){
                        term = new Term(words[0]+"B" , "NumNoUnits");
                        return term;
                    }
                    else if(isFun(words[1])){
                        String termS = words[0] +" "+ words[1];
                        term = new Term(termS ,"NumNoUnits");
                        return term;
                    }
                }
            }
        }
        return term;
    }

    /** Get a string and produce a number according to the rules
     * @param wordNum
     * @return String
     */
    public String makeNum(String wordNum){
        String wordMakeNum = null;
        if(amIThis(wordNum)) {
            if (wordNum != null && wordNum.length() > 0) {
                wordMakeNum = "";
                char[] allChar = wordNum.toCharArray();
                for (int i = 0; i < allChar.length; i++) {
                    if ( allChar[i] != ',') {
                        if(i == allChar.length -1) {
                            if (allChar[i] != 'B' && allChar[i] != 'M' && allChar[i] != 'K' && allChar[i] != '.' ) {
                                wordMakeNum = wordMakeNum + allChar[i];
                            }
                        }
                        else{
                            if(i ==0 ) {
                                if(allChar[0] != '.') {
                                    wordMakeNum = wordMakeNum + allChar[i];
                                }
                            }
                            else {
                                wordMakeNum = wordMakeNum + allChar[i];
                            }
                        }
                    }
                }
            }
        }

        return wordMakeNum;
    }

    /** Does the resulting string represent a number
     * @param word -String
     * @return double
     */
    private double realNumber(String word){
            String numWord = makeNum(word);
            return Double.parseDouble(numWord);
    }

}