package Parse;

public class Percent implements IRules {

    /**
     * Gets a word and checks if it represent percent
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word) {
        if(word.length()>1) {
            return (word.charAt(word.length() - 1) == ('%'));
        }
        return false;
    }

    /**
     * Gets two words and checks if they are match the term conditions
     * @param wordone
     * @param wordtwo
     * @return
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        if (wordone != null && wordtwo != null) {
            if (wordtwo == "percent" || wordtwo == "percentage") {
                for (char c: wordone.toCharArray()) {
                    if (Character.isDigit(c) || c == '.' || c == ',')
                        continue;
                    else
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Gets three words and checks if they are match the term conditions
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
     * Gets four words and checks if they are match the term conditions
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
     * Gets a word and create the right term for her
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        Term term = null;
        String[] words = word.split(" ");
        String termName = words[0] + "%";
        term = new Term(termName, "Percent");
        return term;
    }
}
