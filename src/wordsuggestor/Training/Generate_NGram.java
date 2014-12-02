package wordsuggestor.Training;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import wordsuggestor.Word;

/**
 *
 * @author Gaurav Bansal
 */

//Read processes files and store them in DataStore(unigram & bigram)
public final class Generate_NGram {
    
    DataStore_Tokens dst;
    DataStore_POSTags dsp;
    ArrayList<File> files;
    ArrayList<Word> word;
    
    public Generate_NGram(ArrayList<File> f, DataStore_Tokens dst, DataStore_POSTags dsp) throws IOException {
        this.dst = dst;
        this.files = f;
        this.dsp = dsp;
        this.word = new ArrayList<>();
        
        this.generate();
    }
    
    public void generate() throws FileNotFoundException, IOException {
        String line;
        String token = "";
        int n = 0;
        int fsize = files.size();
        
        String [] w, t;
        
        JFrame frame = new JFrame("Loading Data");
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        // add progress bar
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(pb);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(300, 100);
        frame.setVisible(true);
        frame.setResizable(false);
        
        for(File f : files) {
            n++;
            BufferedReader reader = new BufferedReader(new FileReader(f));
            
            while ((line = reader.readLine()) != null) {
                w = line.split("\\s+");
                
                if(w != null && w.length > 1 && w[0].compareTo("<start>") == 0 
                        && w[w.length - 1].compareTo("<end>") == 0) {
                    word.add(new Word("<start>", "<start>"));
                    for(int i = 1; i < w.length - 1; i++) {
                        t = w[i].split("~");
                        if(t.length == 2)
                            word.add(new Word(t[0], t[1]));
                    }
                    word.add(new Word("<end>", "<end>"));
                }
                addToStore();
                if(n%10 == 0) {
                    double pr = (double) n / fsize;
                    pr *= 100;
                    pb.setValue((int)pr);
                }
            }
        }
        
        pb.setValue(100);
        
        frame.setVisible(false);
        frame.dispose();
    }
    
    public void addToStore() {
        addTokensToStore(word);
        word.clear();
    }
    
    public void addTokensToStore(ArrayList<Word> t) {
        if(t == null || t.isEmpty()) {
            return;
        }
        
        Word prev = null;
        Word cur = null;
        int ip;
        
        for(Word token : t) {
            prev = cur;
            cur = token;
            
            /**
             *  Insert uni-grams in Hashmap
            **/
            if(prev != null) {
                dst.add(prev.word);
                dsp.add(prev.pos);
                dsp.addTag(prev.word, prev.pos);
            }
            
            /**
             *  Insert bi-grams in Hashmap
            **/
            if(prev != null && cur != null)  {
                dst.add(prev.word, cur.word);
                dsp.add(prev.pos, cur.pos);
            }
        }
        
        /**
         *  Add last token that is not added
        **/
        prev = t.get(t.size() - 1);
        dst.add(prev.word);
        dsp.add(prev.pos);
        dsp.addTag(prev.word, prev.pos);
    }
}