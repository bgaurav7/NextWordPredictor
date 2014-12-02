package wordsuggestor.UI;

/**
 *
 * @author Gaurav Bansal
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import wordsuggestor.Training.DataStore_POSTags;
import wordsuggestor.Training.DataStore_Tokens;
import wordsuggestor.Word;

//UserInterface
public class UI {
    
    private JTextArea txt;
    private final Keyboard keyboard;
    
    public JFrame f = new JFrame();
    public JPanel p1 = new JPanel();
    public JPanel p2 = new JPanel();
    
    private static Suggestion s;
    static Suggestion su;
    Thread th;
    DataStore_Tokens dst;
    DataStore_POSTags dsp;
    
    public UI (DataStore_Tokens dst, DataStore_POSTags dsp) {
        this.dst = dst;
        this.dsp = dsp;
        
        f.setLayout(null);
        f.setVisible(true);
        f.setBounds(0,0,550,610);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        txt = new JTextArea();
        txt.setBounds(10,10,515,195);
        txt.setAutoscrolls(true);
        txt.setWrapStyleWord(true);
        txt.setLineWrap(true);
        keyboard = new Keyboard(txt);
        f.add(txt);
        
        txt.getDocument().addDocumentListener( new DocumentListener(){    
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(th != null)
                    th.stop();
                th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            InputProcessor ip = new InputProcessor(dst, dsp);
                            s = new Suggestion(ip.input(txt.getText()));
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                th.start();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if(th != null)
                    th.stop();
                th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            InputProcessor ip = new InputProcessor(dst, dsp);
                            s = new Suggestion(ip.input(txt.getText()));
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                th.start();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                if(th != null)
                    th.stop();
                th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            InputProcessor ip = new InputProcessor(dst, dsp);
                            s = new Suggestion(ip.input(txt.getText()));
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                th.start();
            }
        });
        
        keyboard.setBackground(Color.BLACK);
        keyboard.setSize(30,200);
        keyboard.setBounds(2,440,530,130);
        f.add(keyboard);
        p1.setBounds(2,210,530,230);
        p1.setBackground(Color.GRAY);
        f.add(p1);
    }

    private class Keyboard extends JPanel implements ActionListener  {
        private final JTextArea txt;
        String str = "1234567890qwertyuiopasdfghjklzxcvbnm";
        
        public Keyboard(JTextArea txt) {
            this.txt = txt;  
            for(int i = 0; i < str.length(); i++) {
                createButton(str.charAt(i)+ "");
            }
        }
        
        private void createButton(String label) {
            JButton btn = new JButton(label);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            btn.addActionListener(this);
            setLayout(new FlowLayout());
            add(btn);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            txt.setText(txt.getText() + actionCommand);
        }
    }
    
    private class Suggestion implements ActionListener {
        
        private Suggestion(PriorityQueue<Word> w){
            if(w == null) return;
           
            p1.setVisible(false);
            f.remove(p1);
            f.revalidate();
            p1.removeAll();
            p1.revalidate();
            f.add(p1);
            
            int i = 0;
            String prev = "";
            String cur;
            int type;
            
            while(w.size() > 0 && i < 20) {
                Word wt = w.remove();
                cur = wt.word;
                type = wt.type;
                
                if(cur.compareTo(prev) != 0) {
                    if(cur.compareTo("<end>") == 0) {
                        createSuggestionButton(".");
                    } else if(type == 2) {
                        //auto complete
                        createSuggestionButton("~" + cur);
                    } else {
                        //new word
                        createSuggestionButton(cur);
                    }
                    prev = cur;
                    i++;
                }
                //System.out.println(wt.word + " " + wt.prob);
            }
            p1.setVisible(true);
        }
            
        private void createSuggestionButton(String st) {
            //String st1  = txt.toString();
            JButton btn = new JButton(st);
           
            btn.addActionListener(this);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            p1.setLayout(new GridLayout(10,2));
            p1.add(btn);
            
            
        }
       
        @Override
        public void actionPerformed(ActionEvent e) {
            String word = e.getActionCommand();
            if(word.length() > 1 && word.charAt(0) == '~') {
                //autocomplete
                word = word.substring(1);
                String t = txt.getText();
                String auto = "(.*)(\\s+)\\w+(\\s+)?";
        
                Pattern p = Pattern.compile(auto);

                Matcher ef = p.matcher(t);

                if(ef.find()) {
                    txt.setText(ef.group(1) + " " + word);
                } else {
                    txt.setText(txt.getText() + " " + word);
                }
            } else {
                //add word
                txt.setText(txt.getText() + " " + word);
            }
            p1.setVisible(false);
        }
    }
}
