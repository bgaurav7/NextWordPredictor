package wordsuggestor.Suggester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import wordsuggestor.Training.DataStore_Tokens;
import wordsuggestor.Word;

/**
 *
 * @author Gaurav Bansal
 */

//Generate suggestions from input tokens(input string by user tokenized and processed)
public class StasticalSuggester {
    //Max Priority Queue
    PriorityQueue<Word> wordQueue = new PriorityQueue<>(50000, (Word w1, Word w2) -> (w1.prob > w2.prob ? -1 : 1));
    DataStore_Tokens dst;
    
    public StasticalSuggester(DataStore_Tokens dst) {
        this.dst = dst;
    }
    
    public PriorityQueue<Word> statSugg(ArrayList<String> to) {
        searchSuggestions(to);
        return (this.wordQueue);
        /*int i = 0;
        while(wordQueue.size() > 0 && i++ < 20) {
            Word w = wordQueue.remove();
            System.out.println(w.word + " " + w.prob);
        }
        System.out.println("");*/
    }
    
    private void searchSuggestions(ArrayList<String> to) {
        HashMap<String, Integer> map;
        
        if(to != null && to.size() > 0) {
            //System.out.println("bigram");
            String word = to.get(to.size() - 1);
            //Bigram
            map = dst.getMap(word);
            System.out.println(word); 
            if(map != null) {
                System.out.println(map.size());
                for(String key : map.keySet()) {
                    wordQueue.add(new Word(key, probability(word, key), word, 1));
                }
            } else if (to.size() > 1) {
                //word not found complete last word
                //System.out.println("autocomplete");
                String pword = to.get(to.size() - 2);
                map = dst.getMap(pword);
                
                for(String key : map.keySet()) {
                    if(key.startsWith(word)) {
                        wordQueue.add(new Word(key, probability(pword, key), pword, 2));
                    }
                }
            } else {
                System.out.println("No Suggestions");
            }
        } else {
            //starting word
            //System.out.println("unigram");
            String word = "<start>";
            map = dst.getMap(word);
            if(map != null) {
                System.out.println(map.size());
                for(String key : map.keySet()) {
                    wordQueue.add(new Word(key, probability(word, key), word, 1));
                }
            } else {
                System.out.println("No Suggestion");
            }
        }
    }
    
    private double probability(String w) {
        return (double) ((1 + dst.getCount(w)) / (1 + dst.getTokenCount()));
    }
    
    private double probability(String w1, String w2) {
        double prob = 1 + dst.getCount(w1, w2);
        prob /= dst.getCount(w1) + dst.getTokenCount(w1);
        return prob;
    }
}
