package Parse;

public class Expression implements IRules {

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

    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return (word1.equals("Between") && isNumber(word2) && word3.equals("and") && isNumber(word4));
    }

    @Override
    public Term makeTerm(String word) {
        Term term = new Term(word, "Expression");
        return term;
    }

    private boolean isWord(String str) {
        for (char c: str.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }
        return true;
    }

    private boolean isNumber(String str) {
        return str.matches("^[0-9]+\\.?[0-9]*$");
    }
}
