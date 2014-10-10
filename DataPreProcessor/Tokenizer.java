/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini.DataPreProcessor;

import mini.Training.Learner;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import mini.Word;

/**
 *
 * @author Gaurav Bansal
 */
public final class Tokenizer {
    ArrayList<File> files;
    ArrayList<String> tokens = new ArrayList<>();
    TokenFilter tf;
    String [] spclCase = {"e.g.", "mr.", "mrs.", "vs.", "i.e.", "dr.", "etc."};
    char [] endDel = {'.', '!', '?', '(', ')', '[', ']', ':', ';'};
    //char [] keep = {};
    char [] keep = {'"', '@', '#', '-', ',', '\'', '&'};
    
    public Tokenizer(ArrayList<File> files, TokenFilter tf) throws IOException {
        this.files = files;
        this.tf = tf;
        
        this.tokenizer();
    }
    
    @SuppressWarnings("empty-statement")
    public void tokenizer() throws FileNotFoundException, IOException {
        String line;
        String token = "";
        int n = 0;
        int fsize = files.size();
        
        JFrame frame = new JFrame("Loading Data");
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        // add progress bar
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(pb);
 
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
            if(n%200 == 0) {
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
    
    @SuppressWarnings("empty-statement")
    public PriorityQueue<Word> tokenizer_input(String line) throws IOException {
        if(line == null || line.length() <= 0) return null;
        ArrayList to = new ArrayList<>();
        
        String token = "";
        for(int i = 0; i < line.length(); i++) {
            char c = Character.toLowerCase(line.charAt(i));

            if(isWhiteCharacter(c)) {
                to.add(token);
                token = "";
                while(++i < line.length() && isWhiteCharacter(line.charAt(i)));
                i--;
                continue;
            }
            if(isNumber(c)) {
                to.add(token);
                to.add("#");
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
                to.add(token);
                token = "";
                
                new Learner().learn(to);
                
                to.clear();
                continue;
            }
            if(isKeep(c)) {
                to.add(token);
                to.add(c + "");
                token = "";
            }
        }

        to.add(token);
        //System.out.println(token);
        if(to.isEmpty()) return null;
        return tf.filter_input(to);
        //System.out.println(to);
        
    }
    
    private void forwardTokens() {
        if(tokens != null && tokens.size() > 0) {
            tf.filter_data(tokens);
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
