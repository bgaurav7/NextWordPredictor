/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini;

import mini.Training.DataAnalyser;
import mini.DataPreProcessor.Tokenizer;
import mini.DataPreProcessor.TokenFilter;
import mini.DataPreProcessor.FileFilter;
import mini.DataPreProcessor.InputFilter;
import mini.Suggester.Suggester;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Gaurav Bansal
 */
public class Mini {

    private static final String root = "D:\\OpenANC\\Out";
    
    public static void main(String[] args) throws IOException {
        
        FileFilter f = new FileFilter();
        f.fileFinder(root);
        System.out.println("Files found in corpus : " + f.files.size() + "\n");
        
        DataAnalyser da = new DataAnalyser();
        
        Suggester st = new Suggester(da);
        
        TokenFilter tf = new TokenFilter(da, st);
        
        Tokenizer to = new Tokenizer(f.files, tf);
        System.out.println("All files read and analysed\n");
        
        InputFilter ifl = new InputFilter(to);
        //ifl.input("Hello mr dj");
        //ifl.input("Hello mr dj. Mera gaana please play");
        //to.tokenizer_input("Hello mr dj");
        //to.tokenizer_input("Hello mr dj. Mera gaana please play");
        //to.tokenizer_input("Hello mr dj. Mera gaana don't please play");
      
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                UI ui = new UI(ifl);
            }
        });
        /*Scanner sc = new Scanner(System.in);
        
        while(true) {
            ArrayList<String> t = new ArrayList<>();
            System.out.println("Enter new word");
            int n = sc.nextInt();
            
            while(n-- > 0)
                t.add(sc.next());

            Suggester st = new Suggester(da);
            st.sugg(t);
        }*/
    }
}