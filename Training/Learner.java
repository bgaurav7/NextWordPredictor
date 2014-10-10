/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini.Training;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Gaurav Bansal
 */
public class Learner {
    private static final String root = "D:\\OpenANC\\Out";
    
    
    public void learn(ArrayList<String> to) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(root + "\\learn.txt", true));
        
        for(String s : to) {
            bw.write(s + " ");
        }
        bw.write(".");
        bw.newLine();
        bw.flush();
        bw.close();
    }
}
