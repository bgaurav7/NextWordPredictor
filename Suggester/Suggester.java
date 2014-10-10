/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini.Suggester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import mini.Training.DataAnalyser;
import mini.Word;

/**
 *
 * @author Gaurav Bansal
 */
public class Suggester {
    PriorityQueue<Word> wordQueue = new PriorityQueue<>(50000, (Word w1, Word w2) -> (w1.prob > w2.prob ? -1 : 1));
    DataAnalyser da;
    
    public Suggester(DataAnalyser da) {
        this.da = da;
    }
    
    public PriorityQueue<Word> sugg(ArrayList<String> to) {
        System.out.println("Suggesting wait...");
        searchSuggestions(to);
        System.out.println("Complete");
        System.out.println("");
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
            map = da.getMap(word);
            System.out.println(word); 
            if(map != null) {
                System.out.println(map.size());
                for(String key : map.keySet()) {
                    wordQueue.add(new Word(key, probability(word, key)));
                }
            } else if (to.size() > 1) {
                //word not found complete last word
                //System.out.println("autocomplete");
                String pword = to.get(to.size() - 2);
                map = da.getMap(pword);
                
                for(String key : map.keySet()) {
                    if(key.startsWith(word)) {
                        wordQueue.add(new Word(key, probability(pword, key)));
                    }
                }
            } else {
                System.out.println("No Suggestions");
            }
        } else {
            //starting word
            //System.out.println("unigram");
            String word = "~";
            map = da.getMap(word);
            if(map != null) {
                System.out.println(map.size());
                for(String key : map.keySet()) {
                    wordQueue.add(new Word(key, probability(word, key)));
                }
            } else {
                System.out.println("No Suggestion");
            }
        }
    }
    
    private double probability(String w) {
        return (double) ((1 + da.getCount(w)) / (1 + da.getTokenCount()));
    }
    
    private double probability(String w1, String w2) {
        double prob = 1 + da.getCount(w1, w2);
        prob /= da.getCount(w1) + da.getTokenCount(w1);
        return prob;
    }
}
