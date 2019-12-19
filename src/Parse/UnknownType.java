package Parse;

/**
 * This class created to help us to deal with noise in the corpus.
 * By catching all the terms that our parse methods could not
 * identified their type.
 */
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
