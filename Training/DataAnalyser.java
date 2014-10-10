
package mini.Training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataAnalyser {

    private List<HashMap<String, Integer>> uniG;
    private List<HashMap<String, HashMap<String, Integer>>> biG;
    
    public DataAnalyser() {
        uniG = new ArrayList<>();
        biG = new ArrayList<>();
        
        for(int i = 0; i < 27; i++) {
            uniG.add(new HashMap<>());
            biG.add(new HashMap<>());
        }
    }
    
    public void addTokens(ArrayList<String> t) {
        if(t == null || t.size() == 0) {
            return;
        }
        
        String prev;
        String cur = null;
        int ip;
        
        for(String token : t) {
            prev = cur;
            cur = token;
            
            /**
             *  Insert uni-grams in Hashmap
            **/
            if(prev != null) {
                ip = getIndex(prev);
                int v = 0;
                if(uniG.get(ip).containsKey(prev))
                    v = uniG.get(ip).get(prev);
                uniG.get(ip).put(prev, v + 1);
            } else {
                prev = "~";
            }
            
            /**
             *  Insert bi-grams in Hashmap
            **/
            if(prev != null && cur != null)  {
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
        }
        
        /**
         *  Add last token that is not added
        **/
        prev = t.get(t.size() - 1);
        int v = 0;
        ip = getIndex(prev);
        if(uniG.get(ip).containsKey(prev))
            v = uniG.get(ip).get(prev);
        uniG.get(ip).put(prev, v + 1);
    }
    
    private int getIndex(String s) {
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