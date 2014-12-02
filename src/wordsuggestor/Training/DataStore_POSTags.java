package wordsuggestor.Training;

import java.util.HashMap;

/**
 *
 * @author Gaurav Bansal
 */

//Unigram and bigram of POSTag to POStag

/*
* wTag
* Word - (POSTag, Count)
* Stores Possible tags for each word in corpus and there corresponding occurence count
*/
public class DataStore_POSTags extends DataStore {
    
    public HashMap<String, HashMap<String, Integer>> wTag;
    
    public DataStore_POSTags() {
        wTag = new HashMap();
    }
    
    public void addTag(String w, String t) {
        HashMap<String, Integer> tmp;
        
        if(wTag.containsKey(w)) {
            tmp = wTag.get(w);
            
            if(tmp.containsKey(t)) {
                tmp.put(t, tmp.get(t) + 1);
            } else {
                tmp.put(t, 1);
            }
        } else {
            tmp = new HashMap();
            wTag.put(w, tmp);
        }
    }
    
    public HashMap<String, Integer> getTags(String s) {
        if(wTag.containsKey(s)) {
            return wTag.get(s);
        }
        return new HashMap<String, Integer>();
    }
}
