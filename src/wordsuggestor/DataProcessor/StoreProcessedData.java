package wordsuggestor.DataProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import wordsuggestor.POS.POSTagger;
import wordsuggestor.Word;

/**
 *
 * @author Gaurav Bansal
 */

//write processed files to disk
public class StoreProcessedData {
    
    POSTagger pos;
    File file;
    FileWriter  fw;
    int index = 1;
    
    //output directory
    static String BASE = "D:\\OpenANC\\OUT\\verbatim\\processedDataG";
    
    public StoreProcessedData() throws IOException {
        pos = new POSTagger();
        index = 1;
        
        file = new File(BASE + index + ".txt");
        fw = new FileWriter(file);
    }
    
    public void storeData(ArrayList<String> t) throws IOException {
        ArrayList<Word> a = pos.posTagger(t);
        t  = null;
        
        fw.write("<start> ");
        for(Word w : a) {
            //System.out.println(w.word + " " + w.pos);
            fw.write(w.word + "~" + w.pos + " ");
        }
        fw.write("<end>\r\n");
    }
    
    public void newFile() throws IOException {
        fw.close();
        index++;
        file = new File(BASE + index + ".txt");
        fw = new FileWriter(file);
    }
    
    /**
     *
     * @throws IOException
     * @throws Throwable
     */
    @Override
    protected void finalize () throws IOException, Throwable  {
        try {
            fw.close();
        } finally {
            super.finalize();
        }
    }
}
