/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class Renderer extends JPanel implements Runnable {
    
    private Thread renderLoop;
    private BufferStrategy bs;
    
    private int xsize = 10;
    
    public Renderer (BufferStrategy bs){
        this.bs = bs;
        renderLoop = new Thread(this);
        
    }
    
    public void rendererStart(){
        renderLoop.start();
    }
    
    private void render(Graphics2D g2d){
        g2d.fillRect(0, 0, xsize, 200);
        xsize+=1;
    }

    @Override
    public void run() {
        
        Graphics2D g2d =null;
        while (true){
            try{
                g2d = (Graphics2D) bs.getDrawGraphics();
                render(g2d);
                g2d.dispose();
                if (!bs.contentsLost()) {
                    bs.show();
                }
           }
           catch (IllegalStateException e) { e.printStackTrace();}
 
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) { }
        }
        
    }
    
}
