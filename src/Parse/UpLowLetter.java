package Parse;

public class UpLowLetter implements IRules {

    @Override
    public boolean amIThis(String word) {
        for (char c: word.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }
        return true;
    }

    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        return false;
    }

    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        return false;
    }

    @Override
    public Term makeTerm(String word) {
        Term term;
        String lowWord = toLowerCase(word);
        String upWord = toUpLetter(word);
        if (isUpper(word)) {
            term = new Term(upWord, "UpLowLetter");
            return term;
        }
        else {
            term = new Term(lowWord, "UpLowLetter");
            return term;

        }
    }

    private String toLowerCase(String str) {
        String ans = "";
        for (char c: str.toCharArray()) {
            c = Character.toLowerCase(c);
            ans += c;
        }
        return ans;
    }

    private String toUpLetter(String str) {
        String ans = "";
        for (char c: str.toCharArray()) {
            c = Character.toUpperCase(c);
            ans += c;
        }
        return ans;
    }

    private boolean isUpper(String s) {
        return Character.isUpperCase(s.charAt(0));
    }

    //public HashMap<String,Term> makeTerm(HashMap<String,Term> allTerms, String word) {
    //    Term term;
    //    String lowWord = toLowerCase(word);
    //    String allUpLetter = toUpLetter(word);
    //    String firstLetterUp = firstLetterUp(word);
    //    if (isUpper(word)) {
    //        if (!allTerms.containsKey(lowWord)) {
    //            term = new Term(word);
    //            allTerms.put(term.getTerm(), term);
    //            return allTerms;
    //        }
    //        else {
    //            term = new Term(lowWord);
    //            allTerms.put(term.getTerm(), term);
    //            return allTerms;
    //        }
    //    }
    //    else {
    //        if (allTerms.containsKey(allUpLetter)) {
    //
    //        }
    //    }
    //    return allTerms;
    //}

    //private String firstLetterUp(String str) {
    //    String ans = toLowerCase(str);
    //    char upChar = Character.toUpperCase(ans.charAt(0));
    //    ans = upChar + ans.substring(1);
    //    return ans;
    //}


}
