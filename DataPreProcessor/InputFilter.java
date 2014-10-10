/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini.DataPreProcessor;

import java.io.IOException;
import java.util.PriorityQueue;
import mini.Word;

/**
 *
 * @author Gaurav Bansal
 */
public class InputFilter {
    
    Tokenizer to;
    
    public InputFilter(Tokenizer to) {
        this.to = to;
    }
    
    public PriorityQueue<Word> input(String line) throws IOException {
        PriorityQueue<Word> w = to.tokenizer_input(line);
        
        return w;
        /*int i = 0;
        while(w.size() > 0 && i++ < 20) {
            Word wt = w.remove();
            System.out.println(wt.word + " " + wt.prob);
        }
        System.out.println("");*/
    }
}
