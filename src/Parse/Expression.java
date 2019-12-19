package Parse;

public class Expression implements IRules {

    /**
     * Gets a word and checks if they are match the term conditions
     * @param word
     * @return
     */
    @Override
    public boolean amIThis(String word) {
        String[] words = word.split("-");
        if (words != null && words.length > 0) {
            if (words.length == 2) {
                for (String str: words) {
                    if (!(isWord(str) || isNumber(str)))
                        return false;
                }
                return true;
            }
            else if (words.length == 3) {
                for (String str: words) {
                    if (!(isWord(str)))
                        return false;
                }
                return true;
            }
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
        return (word1.equals("Between") && isNumber(word2) && word3.equals("and") && isNumber(word4));
    }

    /**
     * Gets a word and create the right term for her
     * @param word
     * @return
     */
    @Override
    public Term makeTerm(String word) {
        Term term = new Term(word, "Expression");
        return term;
    }

    /**
     * Gets a String and check if all her characters are letters
     * @param str
     * @return
     */
    private boolean isWord(String str) {
        for (char c: str.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }
        return true;
    }

    /**
     * Gets a String and checks if all her characters are numbers
     * @param str
     * @return
     */
    private boolean isNumber(String str) {
        return str.matches("^[0-9]+\\.?[0-9]*$");
    }
}
