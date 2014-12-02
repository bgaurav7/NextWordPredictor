package wordsuggestor;

import java.io.IOException;
import java.util.Scanner;
import wordsuggestor.DataProcessor.DataProcessorPipeline;
import wordsuggestor.Suggester.SuggestionHandler;

/**
 *
 * @author Gaurav Bansal
 */
public class WordSuggestor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome..!\n");
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Do you want to process data(y/n): ");
        if(sc.next().compareTo("y") == 0) {
            System.out.println("");
            System.out.println("Starting Data Processor");
            new DataProcessorPipeline();
        }
        System.out.println("");
        System.out.println("Starting Suggester");
        new SuggestionHandler();
    }
    
}
