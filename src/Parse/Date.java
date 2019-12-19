package Parse;


import java.util.Dictionary;
import java.util.Hashtable;

public class Date implements IRules {

    private Dictionary<String, String> months;

    public Date() {
        months = new Hashtable<>();
        getMonths((Hashtable<String, String>) months);
    }


    /**
     * Gets a word and create the right term for her.
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        Term term = null;
        String termName;
        String[] words = word.split(" ");
        if (isMonth(words[0])) {
            if (isDay(words[1])) {//checks for MM-DD pattern
                if (words[1].length() != 1) {
                    termName = months.get(words[0])+ "-" + words[1];
                    term = new Term(termName, "Date");
                    return term;
                }
                else {
                    termName = months.get(words[0])+ "-0" + words[1];
                    term = new Term(termName, "Date");
                    return term;
                }
            }
            else if(isYear(words[1])) { //checks for MM-YYYY pattern
                termName = words[1] + "-" + words[0];
                term = new Term(termName, "Date");
                return term;
            }
        }
        else if (isMonth(words[1])) {
            if (isDay(words[0])) {//checks for DD-MM pattern
                if (words[0].length() != 1) {
                    termName = months.get(words[1]) + "-" + words[0];
                    term = new Term(termName, "Date");
                    return term;
                }
                else {
                    termName = months.get(words[1]) + "-0" + words[0];
                    term = new Term(termName, "Date");
                    return term;
                }
            }
        }
        return term;
    }

    /**
     * Gets a word and checks if it a Date term.
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word){
        return false;
    }

    /**
     * Gets two words and checks if they are match the Date term conditions
     * @param wordone
     * @param wordtwo
     * @return
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        if (wordone != null && wordtwo != null) {
            if (isMonth(wordone)) {
                if (isDay(wordtwo)) //checks for MM-DD pattern
                    return true;
                else if(isYear(wordtwo)) //checks for MM-YYYY pattern
                    return true;
            }
            else if (isMonth(wordtwo)) {
                if (isDay(wordone)) //checks for DD-MM pattern
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets three words and checks if they are match the Date term conditions
     * @param word1
     * @param word2
     * @param word3
     * @return
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        return false;
    }

    /**
     * Gets four words and checks if they are match the Date term conditions
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return false;
    }

    /**
     * checks if a word is representing a month
     * @param word
     * @return
     */
    private boolean isMonth(String word) {
        return ((Hashtable<String, String>)months).containsKey(word);
    }

    /**
     * checks if a word is representing a day
     * @param word
     * @return
     */
    private boolean isDay(String word) {
        try {
            int day = Integer.parseInt(word);
            if (day <= 31 && day > 0)
                return true;
        }
        catch (Exception e) {
        }
        return false;
    }

    /**
     * checks if a word is representing a year
     * @param word
     * @return
     */
    private boolean isYear(String word) {
        try {
            int num = Integer.parseInt(word);
            int count = 0;
            while(num != 0)
            {
                num /= 10;
                count++;
            }
            if (count == 4)
                return true;
        }
        catch (Exception e) {
        }
        return false;
    }

    /**
     * return all the months
     * @param hash
     */
    private void getMonths(Hashtable<String, String> hash) {
        hash.put("January" , "01");
        hash.put("February" , "02");
        hash.put("March" , "03");
        hash.put("April" , "04");
        hash.put("May" , "05");
        hash.put("June" , "06");
        hash.put("July" , "07");
        hash.put("August" , "08");
        hash.put("September" , "09");
        hash.put("October" , "10");
        hash.put("November" , "11");
        hash.put("December" , "12");
        hash.put("JANUARY" , "01");
        hash.put("FEBRUARY" , "02");
        hash.put("MARCH" , "03");
        hash.put("APRIL" , "04");
        hash.put("MAY" , "05");
        hash.put("JUNE" , "06");
        hash.put("JULY" , "07");
        hash.put("AUGUST" , "08");
        hash.put("SEPTEMBER" , "09");
        hash.put("OCTOBER" , "10");
        hash.put("NOVEMBER" , "11");
        hash.put("DECEMBER" , "12");
        hash.put("Jan" , "01");
        hash.put("Feb" , "02");
        hash.put("Mar" , "03");
        hash.put("Apr" , "04");
        hash.put("Jun" , "06");
        hash.put("Jul" , "07");
        hash.put("Aug" , "08");
        hash.put("Sep" , "09");
        hash.put("Oct" , "10");
        hash.put("Nov" , "11");
        hash.put("Dec" , "12");
    }
}

