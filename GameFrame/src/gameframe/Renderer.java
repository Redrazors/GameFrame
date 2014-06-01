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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;

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
        
        // test grid
        g2d.setColor(Color.gray.brighter());
        for (int i=0; i<5; i++){
      
            g2d.drawLine(i*100, 0, i*100, 500);
            g2d.drawLine(0, i*100, 500, i*100);
        }
        
        // draw stationary objects
        
        for (int i=0; i< gameObjects.getStationaryObjectsList().size(); i++){
            // grab original transform
            AffineTransform ot = g2d.getTransform();
            
            //get object transform
            AffineTransform lt = new AffineTransform();
            lt.translate(gameObjects.getStationaryObjectsList().get(i).getTransform().getTranslationX(), 
                     gameObjects.getStationaryObjectsList().get(i).getTransform().getTranslationY());
            lt.rotate(gameObjects.getStationaryObjectsList().get(i).getTransform().getRotation());
            g2d.transform(lt);
           
            g2d.setColor(gameObjects.getStationaryObjectsList().get(i).getPaintColor());
            
            switch(gameObjects.getStationaryObjectsList().get(i).getPaintType()){
                case (0):
                    for (int j =0; j<gameObjects.getStationaryObjectsList().get(i).getObjectShapes().size(); j++){
                        g2d.draw(gameObjects.getStationaryObjectsList().get(i).getObjectShapes().get(j));
                    }
                    break;
                case(1):
                    for (int j =0; j<gameObjects.getStationaryObjectsList().get(i).getObjectShapes().size(); j++){
                        g2d.fill(gameObjects.getStationaryObjectsList().get(i).getObjectShapes().get(j));
                    }
                    break;
                        
            }           
            g2d.setTransform(ot);
            
        }
        
        
        // draw movable objects
        
        for (int i =0; i<gameObjects.getMoveableObjectsList().size(); i++){
            // grab original transform
            AffineTransform ot = g2d.getTransform();
            
            //get object transform
            AffineTransform lt = new AffineTransform();
            lt.translate(gameObjects.getMoveableObjectsList().get(i).getTransform().getTranslationX(), 
                     gameObjects.getMoveableObjectsList().get(i).getTransform().getTranslationY());
            lt.rotate(gameObjects.getMoveableObjectsList().get(i).getTransform().getRotation());
            g2d.transform(lt);
           
            g2d.setColor(gameObjects.getMoveableObjectsList().get(i).getPaintColor());
            
            switch(gameObjects.getMoveableObjectsList().get(i).getPaintType()){
                case (0):
                    for (int j =0; j<gameObjects.getMoveableObjectsList().get(i).getObjectShapes().size(); j++){
                        g2d.draw(gameObjects.getMoveableObjectsList().get(i).getObjectShapes().get(j));
                    }
                    break;
                case(1):
                    for (int j =0; j<gameObjects.getMoveableObjectsList().get(i).getObjectShapes().size(); j++){
                        g2d.fill(gameObjects.getMoveableObjectsList().get(i).getObjectShapes().get(j));
                    }
                    break;
                        
            }

            
            
            g2d.setTransform(ot);

        }
        
        // test find the rect fix
        
        int yPos1 = (int)gameObjects.getMoveableObjectsList().get(0).getTransform().getTranslationY();
        
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
