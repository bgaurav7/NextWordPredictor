/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mini;

/**
 *
 * @author Gaurav Bansal
 */

import mini.DataPreProcessor.InputFilter;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UI {
    
    private JTextArea txt;
    private final Keyboard keyboard;
    
    public JFrame f = new JFrame();
    public JPanel p1 = new JPanel();
    public JPanel p2 = new JPanel();
    
    private static Suggestion s;
    InputFilter ilf;
    Thread th;
    
    public UI (InputFilter ilf) {
        this.ilf = ilf;
        
        f.setLayout(null);
        f.setVisible(true);
        f.setBounds(0,0,550,610);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                            s = new Suggestion(ilf.input(txt.getText()));
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
                            s = new Suggestion(ilf.input(txt.getText()));
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
                            s = new Suggestion(ilf.input(txt.getText()));
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
            p1.setVisible(false);
            f.remove(p1);
            f.revalidate();
            p1.removeAll();
            p1.revalidate();
            f.add(p1);
            
            int i = 0;
            String prev = "";
            String cur;
            
            while(w.size() > 0 && i < 20) {
                Word wt = w.remove();
                cur = wt.word;
                if(cur.compareTo(prev) != 0) {
                    createSuggestionButton(cur);
                    prev = cur;
                    i++;
                }
                //System.out.println(wt.word + " " + wt.prob);
            }
            p1.setVisible(true);
        }
            
        private void createSuggestionButton(String st) {    
            JButton btn = new JButton(st);
            btn.addActionListener(this);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            p1.setLayout(new GridLayout(10,2));
            p1.add(btn);
        }
       
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            txt.setText(txt.getText() + " " + actionCommand);
            p1.setVisible(false);
        }    
    }
}
