package Ranker;

import Parse.Document;

import java.util.HashMap;

public interface IRanker {
    double rankDoc(Document document, HashMap<String, Integer> tf);
}
