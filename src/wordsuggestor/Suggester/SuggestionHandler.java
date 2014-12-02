package wordsuggestor.Suggester;

import java.io.IOException;
import javax.swing.SwingUtilities;
import wordsuggestor.FileFilter;
import wordsuggestor.Training.DataStore_POSTags;
import wordsuggestor.Training.DataStore_Tokens;
import wordsuggestor.Training.Generate_NGram;
import wordsuggestor.UI.UI;

/**
 *
 * @author Gaurav Bansal
 */

//Generate bigrams from processed data and store to corresponding data stores
//Start GUI after data processing

public class SuggestionHandler {
    
    //input directory of processed data
    private static final String root = "D:\\OpenANC\\OUT\\final";
    
    public SuggestionHandler() throws IOException {
        FileFilter ff = new FileFilter();
        ff.fileFinder(root);
        System.out.println("Processed files found in corpus : " + ff.files.size() + "\n");
        
        DataStore_Tokens dst = new DataStore_Tokens();
        
        DataStore_POSTags dsp = new DataStore_POSTags();
        
        Generate_NGram gnm = new Generate_NGram(ff.files, dst, dsp);
        
        /*HashMap<String, Integer> pwTags = dsp.getTags("<start>");
        System.out.println(pwTags);
        pwTags = dsp.getTags("i");
        System.out.println(pwTags);
        pwTags = dsp.getTags("hello");
        System.out.println(pwTags);*/
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                UI ui = new UI(dst, dsp);
            }
        });
        
        /*ArrayList<String> a = new ArrayList<String>();
        a.add("i");
        
        s.sugg(a);
        
        System.out.println(dsp.wTag);*/
    }
}