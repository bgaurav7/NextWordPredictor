package mini.DataPreProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileFilter {

    public static String ext = ".txt";
    public ArrayList<File> files = new ArrayList<>();
    public Queue<String> dQ = new LinkedList();
    
    public void fileFinder(String dir) {
        dQ.add(dir);
        
        while(dQ.size() > 0) {
            String curD = dQ.remove();
            
            File folder = new File(curD);
            
            if(isDirectory(folder)) {
                if(folder.canRead()) {
                    for(File f : folder.listFiles()) {
                        if(f.isDirectory()) {
                            dQ.add(f.getAbsolutePath());
                        } else {
                            if(f.toString().endsWith(ext)) {
                                files.add(f);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isDirectory(File f) {
        return f.isDirectory();
    }
}
