/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import gameframe.gameobjects.GameObjects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;
import org.dyn4j.geometry.Circle;

/**
 *
 * @author David
 */
public class Renderer extends JPanel implements Runnable {
    
    private Thread renderLoop;
    private BufferStrategy bs;
    
    private GameObjects gameObjects;
    
   
    
    public Renderer (BufferStrategy bs, GameObjects gameObjects){
        this.bs = bs;
        renderLoop = new Thread(this);
        this.gameObjects = gameObjects;
       
        
    }
    
    public void rendererStart(){
        renderLoop.start();
    }
    
    private void render(Graphics2D g2d){
        
        RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints (rh);
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 500, 500);
        
        
        // test
        int size = gameObjects.getGameObjects().size();
        
        for (int i =0; i<size; i++){
            g2d.setColor(Color.red);
            int xPos1 = (int)gameObjects.getGameObjects().get(i).getTransform().getTranslationX();
            int yPos1 = (int)gameObjects.getGameObjects().get(i).getTransform().getTranslationY();
            g2d.translate(xPos1, yPos1);

            for (int j =0; j<gameObjects.getGameObjects().get(i).getObjectShapes().size(); j++){
                g2d.draw(gameObjects.getGameObjects().get(i).getObjectShapes().get(j));
                g2d.drawOval(-5, -5, 10, 10);
            }
            //g2d.fillOval(xPos1-30, yPos1-30, 60, 60);
            g2d.translate(-xPos1, -yPos1);
            
            System.out.println(gameObjects.getGameObjects().get(i).getTransform().getTranslation());
        }
        

        
 
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
                renderLoop.sleep(1);
            } catch (InterruptedException e) { }
        }
        
    }
    
}
