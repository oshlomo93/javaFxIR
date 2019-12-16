package Parse;

public class UnknownType implements IRules {
    @Override
    public boolean amIThis(String word) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2) {
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
        Term term;
        term = new Term(word, "UnknownType");
        return term;
    }
}
