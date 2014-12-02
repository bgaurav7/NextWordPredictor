package wordsuggestor.POS;


import java.util.ArrayList;
import java.util.List;
import wordsuggestor.Word;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 *
 * @author Gaurav Bansal
 */

//Add POS Tags to tokens of a sentence
public class POSTagger {
    
    MaxentTagger tagger;
    
    public POSTagger() {
        //Loading trained model of english
        tagger = new MaxentTagger("models/english-bidirectional-distsim.tagger");
    }
    
    public ArrayList<Word> posTagger(ArrayList<String> t) {
        ArrayList<Word> p = new ArrayList<>();
    
        List<HasWord> sent = Sentence.toWordList(t.toArray(new String[0]));
        
        List<TaggedWord> taggedSent = tagger.tagSentence(sent);
        
        sent.clear();
        
        for (TaggedWord tw : taggedSent) {
            p.add(new Word(tw.word(), tw.tag()));
        }
        
        taggedSent.clear();
        
        return p;
    }
}