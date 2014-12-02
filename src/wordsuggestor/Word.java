package wordsuggestor;

/**
 *
 * @author Gaurav Bansal
 */
public class Word {
    public double prob;
    public String word;
    public String pos;
    public String pword;
    public int type;
    
    public Word(String w, String p) {
        word = w;
        pos = p;
    }
    
    public Word(String w, double p, String pw, int t) {
        word = w;
        prob = p;
        pword = pw;
        type = t;
    }
}
