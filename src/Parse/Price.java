package Parse;

/**
 * This class creates terms that represent price
 */
public class Price implements IRules {

    /**
     * Testing the word whether it represents price
     * @param word
     * @return boolean-Is the term a price type
     */
    @Override
    public boolean amIThis(String word) {
        if(word != null && word.length()>1) {
            if( isANum(word.substring(1 , word.length()-1))){
                if(word.charAt(0) =='$' && isANum(word.substring(1 , word.length()-1))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 2-word test whether it represents price
     * @param wordone -Check if this is the word number
     * @param wordtwo -Check if the word is a dollar
     * @return boolean-Is the term a price type
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        if(wordone != null && wordtwo != null && wordone.length()>0 && wordtwo.length()>0){
            if(wordtwo.equals("Dollars") || wordtwo.equals("dollars")){
                if(isANum(wordone)){
                    return true;
                }
                if( wordone.length()>3 && ((isANum(wordone.substring(0 , wordone.length()-1)) && wordone.charAt(wordone.length()-1)=='m') ||
                        (isANum(wordone.substring(0 , wordone.length()-2)) && wordone.substring(wordone.length()-2).equals("bn"))) ){
                    return true;
                }
            }
            if(amIThis(wordone)&& (wordtwo.equals("million") ||wordtwo.equals("billion"))){
                return true;
            }
        }
        return false;
    }

    /**
     * 3-word test whether it represents price
     * @param word1
     * @param word2
     * @param word3
     * @return boolean-Is the term a price type
     */
    public boolean amIThis(String word1 , String word2 , String word3){
        if(word1!=null && word2!=null && word3!=null && word1.length()>0 && word2.length()>0 && word3.length()>0){
            if(isANum(word1) && ( word2.equals("m") ||word2.equals("bn") ) && word3.equals("Dollars")){
                return true;
            }
            if(isFun(word2)&& isANum(word1) && word3.equals("Dollars") ){
                return true;
            }
        }
        return false;
    }


    /**
     * 4-word test whether it represents price
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return boolean-Is the term a price type
     */
    public boolean amIThis(String word1 , String word2 , String word3 , String word4){
        if(word1!=null && word2!=null && word3!=null && word1.length()>0&& word4!=null &&
                word4.length()>0 && word2.length()>0 && word3.length()>0) {
            if(isANum(word1) && (word2.equals("million") ||word2.equals("billion")  ||word2.equals("trillion")) && word3.equals("U.S.")
                    &&(word4.equals("Dollars") || word4.equals("dollars")) ){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the string is a fragment type number
     * @param word
     * @return
     */
    private boolean isFun(String word){
        if(word != null && word.length()>0) {
            String [] nums = word.split("/");
            if( nums.length>1 && isANum(nums[0]) && isANum(nums[1])){
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a price type term
     * @param word - the term
     * @return - Object Term
     */
    @Override
    public Term makeTerm(String word) {
        Term term = null;
        if(word != null && word.length()>0) {
            String [] words = word.split(" ");
            if(words.length ==1){
                if(amIThis(word)){
                    if(numUpToM(word.substring(1))){
                        double num = makeNum(word.substring(1));
                        num = num/1000000;
                        String termS = num + " M" + " Dollars";
                        term = new Term(termS, "Price");
                    }
                    else {
                        String termS = word.substring(1) +  " Dollars";
                        term = new Term(termS, "Price");
                    }
                }
            }
            else if(words.length ==2){
                if(amIThis(words[0] ,words[1] )){
                    if(words[1].equals("Dollars") && isANum(words[0])){
                        if(numUpToM(words[0])){
                            double num = makeNum(words[0]);
                            num = num/1000000;
                            String termS = num + " M" + " Dollars";
                            term = new Term(termS, "Price");
                            return term;
                        }
                        else{
                            String termS = words[0]+  " Dollars";
                            term = new Term(termS, "Price");
                            return term;
                        }
                    }
                    if(words[1].equals("million") && isANum(words[0].substring(1))){
                        String termS = words[0].substring(1)+ " M" + " Dollars";
                        term = new Term(termS, "Price");
                        return term;
                    }
                    if( words[0].length()>0 && words[1].equals("billion")){
                        if(isANum(words[0].substring(1))) {
                                double num = makeNum(words[0].substring(1));
                                num = num * 1000;
                                String termS = num + " M" + " Dollars";
                                term = new Term(termS, "Price");
                                return term;

                        }
                    }
                    if(words[1].equals("Dollars") || words[1].equals("dollars")){
                        if(words[0].length()>2 && words[0].charAt(words[0].length()-1)=='m' && isANum(words[0].substring(0 , words[0].length()-2))){
                            String termS = words[0].substring(0 ,words[0].length()-1 )+ " M" + " Dollars";
                            term = new Term(termS, "Price");
                            return term;

                        }
                        if(words[0].length()>2 && (( words[0].substring(words[0].length()-2).equals("bn")) && isANum(words[0].substring(0 , words[0].length()-2)))){
                            double num =Double.parseDouble(words[0].substring(0 , words[0].length()-2));
                            num = num * 1000;
                            String termS = String.format("%.3f", num);
                            termS = termS + " M" + " Dollars";
                            term = new Term(termS, "Price");
                            return term;
                        }
                    }

                }

            }
            else if(words.length ==3){
                if(amIThis(words[0] ,words[1] , words[2])){
                    if(words[2].equals("Dollars") || words[2].equals("dollars")) {
                        if (isANum(words[0])) {
                            if(isFun(words[1])){
                                String termS = words[0] +" "+ words[1] +" Dollars";
                                term = new Term(termS, "Price");
                                return term;
                            }
                            if (words[1].equals("m")){
                                String termS = words[0] +" M Dollars";
                                term = new Term(termS, "Price");
                                return term;
                            }
                            if (words[1].equals("bn")){
                                double num =Double.parseDouble(words[0]);
                                num = num * 1000;
                                String termS = String.format("%.3f", num);
                                termS = termS + " M Dollars";
                                term = new Term(termS, "Price");
                                return term;
                            }
                        }
                    }
                }

            }
            else if(words.length ==4){
                if(amIThis(words[0] ,words[1] ,words[2] ,words[3])){
                    if(isANum(words[0])) {
                        if (words[1].equals("million")) {
                            String termS = words[0] + " M Dollars";

                            term = new Term(termS, "Price");
                            return term;

                        }
                        if (words[1].equals("billion")) {
                            double num = Double.parseDouble(words[0]);
                            num = num * 1000;
                            String termS = String.format("%.3f", num);
                            termS = termS + " M Dollars";
                            term = new Term(termS, "Price");
                            return term;
                        }
                        if (words[1].equals("trillion")) {
                            double num = Double.parseDouble(words[0]);
                            num = num * 1000000;
                            String termS = String.format("%.3f", num);
                            termS = termS + " M Dollars";
                            term = new Term(termS, "Price");
                            return term;
                        }
                    }
                }
            }

        }
        return term;
    }

    /**
     * Gets a string and returns the number it represents
     * @param word
     * @return number
     */
    private  double makeNum (String word) {
        double numThis = 0;
        String[] words = word.split(",");
        String num = "";
        for (String s : words) {
            num = num + s;
        }
        if (isANum(num)) {
            numThis = Double.parseDouble(num);
            return numThis;
        }
        return numThis;
    }

    /**
     * Is the word a number
     * @param word
     * @return boolean- Is the word a number
     */
    private boolean isANum(String word){
        boolean therIsPoint = false;
        if(word != null && word.length()>0) {
            for (int i = 0; i < word.length(); i++) {
               //[ if(i!=0 && i!= word.length()-1) {
                if(word.charAt(i) == '.' && !therIsPoint){
                    therIsPoint =true;
                    continue;
                }
                if(word.charAt(i)=='.'){
                    return false;
                }
                if (word.charAt(i)!= ',') {
                    if (48 > word.charAt(i) || word.charAt(i) > 57 ) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether the number is greater than one million
     * @param word
     * @return boolean- is number greater than one million
     */
    private boolean numUpToM(String word){
        if(word != null && word.length()>0){
            if(isANum(word)){
                String[] numWord =word.split(",");
                String numThis = "";
                for (String s:numWord) {
                    numThis = numThis +s;
                }
                double num = makeNum(numThis);
                if (num >= 1000000) {
                    return true;
                }
            }
        }
        return false;
    }




}
