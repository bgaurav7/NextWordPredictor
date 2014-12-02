
package wordsuggestor.Training;

/**
 *
 * @author Gaurav Bansal
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//General Data Store to store bigrams and unigrams
//use indexing based on first character
//with setters(adders) and getters
public class DataStore {

    public List<HashMap<String, Integer>> uniG;
    public List<HashMap<String, HashMap<String, Integer>>> biG;
    
    public DataStore() {
        uniG = new ArrayList<>();
        biG = new ArrayList<>();
        
        for(int i = 0; i < 27; i++) {
            uniG.add(new HashMap<>());
            biG.add(new HashMap<>());
        }
    }
    
    public void add(String u) {
        if(u == null || u.length() == 0) return;
        int ip;
        
        ip = getIndex(u);
        int v = 0;
        if(uniG.get(ip).containsKey(u))
            v = uniG.get(ip).get(u);
        uniG.get(ip).put(u, v + 1);
    
    }
    
    public void add(String prev, String cur) {
        if(prev == null || prev.length() == 0) return;
        if(cur == null || cur.length() == 0) return;
        
        int ip;
        HashMap <String, Integer> tmp;
        
        ip = getIndex(prev);
        if(biG.get(ip).containsKey(prev)) {
            tmp = biG.get(ip).get(prev);
            if(tmp.containsKey(cur)) {
                tmp.put(cur, tmp.get(cur) + 1);
            } else {
                tmp.put(cur, 1);
            }
        } else {
            tmp = new HashMap<>();
            tmp.put(cur, 1);
            biG.get(ip).put(prev, tmp);
        }
    }
    
    protected int getIndex(String s) {
        if(s.length() > 0) {
            char c = s.charAt(0);
            if(c >= 'a' && c <= 'z')
                return c - 'a';
        }
        return 26;
    }
    
    public HashMap<String, Integer> getMap(String w) {
        if(w.length() > 0) {
            int i = getIndex(w);
            if(biG.get(i).containsKey(w))
                return biG.get(i).get(w);
        }
        return null;
    }
    
    public int getCount(String w) {
        if(w.length() > 0) {
            int i = getIndex(w);
            if(uniG.get(i).containsKey(w))
                return uniG.get(i).get(w);
        }
        
        return 0;
    }
    
    public int getCount(String w1, String w2) {
        HashMap<String , Integer> tmp = getMap(w1);
        if(tmp != null && tmp.containsKey(w2))
            return tmp.get(w2);
        return 0;
    }
    
    public int getTokenCount() {
        int c = 0;
        for(int i = 0; i < 27; i++) {
            for(int n : uniG.get(i).values()) {
                c += n;
            }
        }
        return c;
    }
    
    public int getTokenCount(String w) {
        int c = 0;
        if(w.length() > 0) {
            HashMap<String, Integer> tmp = getMap(w);
            if(tmp != null) {
                for(int n : tmp.values()) {
                    c += n;
                }
            }
        }
        return c;
    }
}