package wordsuggestor.DataProcessor;

import wordsuggestor.FileFilter;
import java.io.IOException;
import wordsuggestor.Tokenizer.TokenizerHandler_Files;

/**
 *
 * @author Gaurav Bansal
 */

//pipeline to process text files to format of tokens and tags and save to disk
public class DataProcessorPipeline {

    //input directory
    private static final String root = "D:\\OpenANC\\IN\\written_1\\journal\\verbatim";
    
    public DataProcessorPipeline() throws IOException {
        FileFilter ff = new FileFilter();
        ff.fileFinder(root);
        System.out.println("Files found in corpus : " + ff.files.size() + "\n");
     
        StoreProcessedData spd = new StoreProcessedData();
        
        TokenizerHandler_Files tf = new TokenizerHandler_Files(ff.files, spd);
    }
}