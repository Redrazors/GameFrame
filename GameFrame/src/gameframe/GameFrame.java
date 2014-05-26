/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class GameFrame extends JFrame {
    
    public GameFrame(){
        setIgnoreRepaint(true);
        setTitle("Game Framework");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setResizable(false);
        setVisible(true);
        createBufferStrategy(2);
        BufferStrategy bs = getBufferStrategy();
        
        MasterClass masterClass = new MasterClass (this, bs);
        
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
