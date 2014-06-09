/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import kuusisto.tinysound.TinySound;
/**
 *
 * @author David
 */
public class GameFrame extends JFrame {
    
    public GameFrame(){
        setIgnoreRepaint(true);
        setTitle("Game Framework");
        TinySound.init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //height of the task bar
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;

        screenSize.height-=taskBarSize;

        
        setSize(screenSize);
        setResizable(false);
        
        this.setUndecorated(true);
        
        setVisible(true);
        createBufferStrategy(2);
        BufferStrategy bs = getBufferStrategy();
        
        JComponent drawPanel = new JComponent() {};
        drawPanel.setIgnoreRepaint(true);
        this.add(drawPanel);
        
        MasterClass masterClass = new MasterClass (this, bs, screenSize, drawPanel);
        
        
        
        masterClass.gameInit();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GameFrame gameFrame = new GameFrame();
    }
    
}
