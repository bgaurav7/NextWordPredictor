package wordsuggestor.Tokenizer;

import java.util.ArrayList;

/**
 *
 * @author Gaurav Bansal
 */

public class TokenFilter {
 
    char [] single = {'"', '@', '-', ',', '\'', '&', 'a', 'i', 'o'};
        
    ArrayList<String> filter(ArrayList<String> token) {
        String t;
        
        for(int i = 0; i < token.size(); i++) {
            t = token.get(i);
            
            if(t.length() == 1) {
                if(t.charAt(0) == '\'' || t.charAt(0) == '-') {
                    if(i > 0) {
                        if(token.size() <= i + 1) {
                            token.add("");
                        }
                        token.add(i, token.get(i - 1) + token.get(i) + token.get(i + 1));
                        token.remove(i - 1);
                        token.remove(i + 1);
                    }
                }
                if(!isSingle(t.charAt(0))) {
                    token.remove(i);
                }
            }
        }
        
        return token;
    }

    private boolean isSingle(char c) {
        for(char re : single)
            if(re == c) return true;
        return false;
    }
}