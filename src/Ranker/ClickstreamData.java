package Ranker;

import Parse.Document;

import java.util.HashMap;

public class ClickstreamData implements IRanker {
    @Override
    public double rankDoc(Document document, HashMap<String, Integer> tf) {
        return 0;
    }
}
