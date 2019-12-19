package Parse;

public class UpLowLetter implements IRules {

    /**
     * Gets a word and checks if she match the term conditions
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word) {
        for (char c: word.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }
        return true;
    }

    /**
     * Gets two words and checks if they match the term conditions
     * @param wordone
     * @param wordtwo
     * @return
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        return false;
    }

    /**
     * Gets four words and checks if they match the term conditions
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
     * Gets three words and checks if they match the term conditions
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
     Gets a word and creates the right term for her
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        Term term;
        String lowWord = word.toLowerCase();
        String upWord = word.toUpperCase();
        if (isUpper(word)) {
            term = new Term(upWord, "UpLowLetter");
            return term;
        }
        else {
            term = new Term(lowWord, "UpLowLetter");
            return term;

        }
    }

    /**
     * checks if a String starts with upper case
     * @param s
     * @return
     */
    private boolean isUpper(String s) {
        return Character.isUpperCase(s.charAt(0));
    }

}
