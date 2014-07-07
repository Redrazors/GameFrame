/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author david
 */
public class TestPanel extends JPanel {
    
    private JButton buttonStart;
    
    public TestPanel(){
        this.setSize(100, 100);
        this.setBackground(Color.red);
        this.setVisible(true);
 
        Font smallText = new Font("Arial",Font.PLAIN, 12);
        Dimension buttonSize = new Dimension (89, 30);
        buttonStart = new JButton();
        buttonStart.setFont(smallText);
        buttonStart.setText("Start");
        buttonStart.setPreferredSize(buttonSize);
        buttonStart.addActionListener(new ActionListener()
        {
            @Override            
            public void actionPerformed(ActionEvent event)
            {

            }           
        });
        this.add(buttonStart);
    }
    
    public void redraw(){
        buttonStart.revalidate();
        buttonStart.setVisible(true);
        
    }
    
}
