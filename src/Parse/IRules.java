package Parse;

public  interface IRules {
     boolean amIThis(String word);
     boolean amIThis(String word1 , String word2);
     boolean amIThis(String word1 , String word2 , String word3);
     boolean amIThis(String word1 , String word2 , String word3 , String word4);
     Term makeTerm(String word);
}
