package Parse;

public class Percent implements IRules {
    @Override
    public boolean amIThis(String word) {
        if(word.length()>1) {
            return (word.charAt(word.length() - 1) == ('%'));
        }
        return false;
    }

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

    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return false;
    }

    @Override
    public Term makeTerm(String word) {
        Term term = null;
        String[] words = word.split(" ");
        String termName = words[0] + "%";
        term = new Term(termName, "Percent");
        return term;
    }
}
