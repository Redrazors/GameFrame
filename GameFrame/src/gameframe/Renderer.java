/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.Color;
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
    
    private MoveableObject testObject, testObject2;
    
   
    
    public Renderer (BufferStrategy bs, MoveableObject testObject, MoveableObject testObject2){
        this.bs = bs;
        renderLoop = new Thread(this);
        
        this.testObject = testObject;
        this.testObject2 = testObject2;
        
    }
    
    public void rendererStart(){
        renderLoop.start();
    }
    
    private void render(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 500, 500);
        // test
        g2d.setColor(Color.red);
        int xPos1 = (int)testObject.getTransform().getTranslationX();
        int yPos1 = (int)testObject.getTransform().getTranslationY();
        g2d.fillOval(xPos1-10, yPos1-10, 20, 20);
        
        g2d.setColor(Color.blue);
        int xPos2 = (int)testObject2.getTransform().getTranslationX();
        int yPos2 = (int)testObject2.getTransform().getTranslationY();
        g2d.fillOval(xPos2-10, yPos2-10, 20, 20);
 
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
