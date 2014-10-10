package mini.DataPreProcessor;

import mini.Suggester.Suggester;
import java.util.ArrayList;
import java.util.PriorityQueue;
import mini.Training.DataAnalyser;
import mini.Word;

public class TokenFilter {

    /*Do not romove
    *   ?   |
    */
    // e.g. vs. etc.    i.e.   Dr.   Mr.   Miss. Mrs.   .com   st.   .c .java
    /** split into parts tokens
     * (    )   [   ]   ;   :
     * */
    /** Skip tokens(if in center split)
     *  ,   --  "   '(at start) -(at end)   =   _   *
     * Numbers  @   Single Letter Except(a, i)
     */
    /** Special tokens
     *  /   -   Numbers
     */
    /** Special endings
     *  's  've 't  'll 'st 're 'd  'm
     */
    DataAnalyser da;
    Suggester st;
    
    char [] single = {'"', '@', '-', ',', '\'', '&', 'a', 'i', 'o'};
    
    public TokenFilter(DataAnalyser da, Suggester st) {
        this.da = da;
        this.st = st;
    }
    
    private void filter(ArrayList<String> token) {
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
    }
    
    public void filter_data(ArrayList<String> token) {
        filter(token);
        da.addTokens(token);
    }
    
    public PriorityQueue<Word> filter_input(ArrayList<String> token) {
        filter(token);
        //System.out.println(token);
        return st.sugg(token);
    }

    private boolean isSingle(char c) {
        for(char re : single)
            if(re == c) return true;
        return false;
    }
}