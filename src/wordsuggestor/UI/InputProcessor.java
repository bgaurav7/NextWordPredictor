package wordsuggestor.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import wordsuggestor.Suggester.StasticalSuggester;
import wordsuggestor.Suggester.SyntacticalSuggester;
import wordsuggestor.Tokenizer.Tokenizer_String;
import wordsuggestor.Training.DataStore_POSTags;
import wordsuggestor.Training.DataStore_Tokens;
import wordsuggestor.Word;

/**
 *
 * @author Gaurav Bansal
 */

//On each key down of user(input change)
//Pass it for suggestions
//First generate suggestions by stastical analysis
//Then filter probabilities by syntactic analysis
public class InputProcessor {
    
    Tokenizer_String ts;
    //POSTagger pos;
    StasticalSuggester stS;
    SyntacticalSuggester syS;
    
    public InputProcessor(DataStore_Tokens dst, DataStore_POSTags dsp) {
        //this.pos = pos;
        
        this.ts = new Tokenizer_String();
        this.stS = new StasticalSuggester(dst);
        this.syS = new SyntacticalSuggester(dsp);
    }

    public PriorityQueue<Word> input(String text) throws IOException {
        
        ArrayList<String> t = ts.tokenize(text);
        
        //ArrayList<Word> w = pos.posTagger(t);
        
        PriorityQueue<Word> statw = stS.statSugg(t);
        PriorityQueue<Word> synw = syS.syntacSugg(statw);
        
        System.out.println(synw.size());
        /*Word wb = statw.peek();
        
        System.out.println(wb.word);
        System.out.println(wb.prob);
        System.out.println(wb.pword);
        System.out.println(wb.pos);
        */
        
        return synw;
    }
    
}