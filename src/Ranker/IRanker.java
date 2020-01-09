package Ranker;

import Parse.Document;

import java.util.HashMap;
import java.util.Map;

public interface IRanker {
    double rankDoc(Document query ,Document document, HashMap<String, Integer> tf, HashMap<String, Integer> documentIdAndSize, Map<String, Integer> docIdAndNuberOfUniqeTermInDoc);
}
