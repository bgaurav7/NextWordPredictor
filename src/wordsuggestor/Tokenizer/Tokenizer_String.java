package wordsuggestor.Tokenizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Gaurav Bansal
 */

//Tokenize input and filter them.
//Return filtered tokens of last written sentence
public final class Tokenizer_String {
    // # used for numbers
    // <start> <end> for line start and end
    
    ArrayList<String> tokens = new ArrayList<>();
    TokenFilter tf;
    
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
    public Tokenizer_String() {
        this.tf = new TokenFilter();
    }
    
    @SuppressWarnings("empty-statement")
    public ArrayList<String> tokenize(String line) throws FileNotFoundException, IOException {
        String token = "";
        
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

                tokens.clear();
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
            
        filterTokens();
        return tokens;
    }
    
    private void filterTokens() {
        if(tokens != null && tokens.size() > 0) {
            tokens = tf.filter(tokens);
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
