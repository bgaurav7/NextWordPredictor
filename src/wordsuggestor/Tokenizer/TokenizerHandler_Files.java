package wordsuggestor.Tokenizer;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import wordsuggestor.DataProcessor.StoreProcessedData;

/**
 *
 * @author Gaurav Bansal
 */

//for all files tokeize them, filter them by tokenizer filter and then pass it to StoreProcessedData(write to disk) 
public final class TokenizerHandler_Files {
    
    // # used for numbers
    // <start> <end> for line start and end
    
    ArrayList<File> files;
    ArrayList<String> tokens = new ArrayList<>();
    TokenFilter tf;
    StoreProcessedData spd;
    
    /*Do not romove
    *   .  where relevant
    */
    // e.g. vs. etc.    i.e.   Dr.   Mr.   Miss. Mrs.   .com   st.   .c .java
    String [] spclCase = {"e.g.", "mr.", "mrs.", "vs.", "i.e.", "dr.", "etc."};
    
    //Delimeters which are mark to sentence end. Refer - Eng. punctuation rules
    char [] endDel = {'.', '!', '?', '(', ')', '[', ']', ':', ';'};
    
    //Allowed special characters
    char [] keep = {'"', '@', '#', '&'};
    
    int a = 0, b = 0;
    
    public TokenizerHandler_Files(ArrayList<File> files, StoreProcessedData spd) throws IOException {
        this.files = files;
        this.tf = new TokenFilter();
        this.spd = spd;
        
        this.tokenizer();
    }
    
    @SuppressWarnings("empty-statement")
    public void tokenizer() throws FileNotFoundException, IOException {
        String line;
        String token = "";
        int n = 0;
        int fsize = files.size();
        
        JFrame frame = new JFrame("Processing Data");
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
                for(int i = 0; i < line.length(); i++) {
                    char c = Character.toLowerCase(line.charAt(i));

                    if(isWhiteCharacter(c)) {
                        addToken(token);
                        token = "";
                        while(++i < line.length() && isWhiteCharacter(line.charAt(i)));
                        i--;
                        continue;
                    }
                    if(isNumber(c)) {
                        addToken(token);
                        addToken("#");
                        token = "";
                        while(++i < line.length() && isNumber(line.charAt(i)));
                        i--;
                        continue;
                    }
                    if(isNotDelimeter(c)) {
                        token += c;
                        continue;
                    }
                    if(isEndingDelimeter(c)) {
                        if(isThisDelimeter(c, '.')) {
                            if(isSpecialCase(token + c)) {
                                token += c;
                                continue;
                            }
                        }
                        addToken(token);
                        token = "";

                        forwardTokens();

                        continue;
                    }
                    if(isKeep(c)) {
                        addToken(token);
                        addToken(c + "");
                        token = "";
                    }
                }

                addToken(token);
                token = "";
            }
            //System.out.println(f);
            if(n%1 == 0) {
                spd.newFile();
                
                double pr = (double) n / fsize;
                pr *= 100;
                pb.setValue((int)pr);
            }
        }
        
        pb.setValue(100);
        
        forwardTokens();
        
        frame.setVisible(false);
        frame.dispose();
    }
    
    private void forwardTokens() throws IOException {
        if(tokens != null && tokens.size() > 0) {
            tokens = tf.filter(tokens);
            spd.storeData(tokens);
            tokens.clear();
        }
    }

    private void addToken(String t) {
        if(t != null && t.length() > 0)
            tokens.add(t);
    }
    
    private boolean isWhiteCharacter(char c) {
        return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f' || c == '\b');
    }
    
    private boolean isNotDelimeter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
    
    private boolean isNumber(char c) {
        return c >= '0' && c < '9';
    }
    
    private boolean isEndingDelimeter(char c) {
        for(char ed : endDel)
            if(ed == c) return true;
        return false;
    }
    
    private boolean isKeep(char c) {
        for(char re : keep)
            if(re == c) return true;
        return false;
    }
    
    private boolean isThisDelimeter(char c, char d) {
        return c == d;
    }
    
    private boolean isSpecialCase(String c) {
        for(String sc : spclCase)
            if(sc.startsWith(c)) return true;
        return false;
    }
}
