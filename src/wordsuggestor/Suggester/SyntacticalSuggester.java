package wordsuggestor.Suggester;

import java.util.HashMap;
import java.util.PriorityQueue;
import wordsuggestor.Training.DataStore_POSTags;
import wordsuggestor.Word;

/**
 *
 * @author Gaurav Bansal
 */

//Suggestion from stastical analysis are re-probabilitized acc to syntactic property
public class SyntacticalSuggester {
    //Max Priority queue
    PriorityQueue<Word> wordQueue = new PriorityQueue<>(50000, (Word w1, Word w2) -> (w1.prob < w2.prob ? -1 : 1));
    
    DataStore_POSTags dsp;
    
    public SyntacticalSuggester(DataStore_POSTags dsp) {
        this.dsp = dsp;
    }
    
    public PriorityQueue<Word> syntacSugg(PriorityQueue<Word> wa) {
        wordQueue.clear();
        
        Word w;
        
        for(int i = 0; i < wa.size(); i++) {
            w = wa.remove();
            HashMap<String, Integer> pwTags = dsp.getTags(w.pword);
            HashMap<String, Integer> wTags = dsp.getTags(w.word);
            w.prob = probability(pwTags, wTags);
            wordQueue.add(w);
        }
        
        return wordQueue;
    }
    
    //max prob from POS Tags. Viterbi algorithm
    double probability(HashMap<String, Integer> pwTags, HashMap<String, Integer> wTags) {
        double minP = 1000;
        double p;
        
        for(String pwT : pwTags.keySet()) {
            for(String wT : wTags.keySet()) {
                p = -Math.log10(probWordTag(wT, wTags.get(wT)));
                p -= Math.log10(probTagTag(pwT, wT));
                if(p < minP) minP = p;
            }
        }
        return minP;
    }
    
    double probWordTag(String wT, int cwt) {
        return cwt / (1 + dsp.getCount(wT));
    }
    
    double probTagTag(String pwT, String wT) {
        return dsp.getCount(pwT, wT) / (1 + dsp.getCount(pwT));
    }
    
}
