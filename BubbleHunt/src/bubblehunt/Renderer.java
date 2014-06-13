/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt;

import static bubblehunt.StaticFields.TILESIZE;
import static bubblehunt.StaticFields.PITCHSIZE;

import bubblehunt.gameobjects.Bubble;
import bubblehunt.gameobjects.BubbleTile;
import bubblehunt.gameobjects.GameObjects;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

/**
 *
 * @author David
 */
public class Renderer implements Runnable {
    
    private Thread renderLoop;
    private BufferStrategy bs;
    
    private GameObjects gameObjects;
    private PathControl pathControl;
    
    private Dimension screenSize;
    
    
    private BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        try {
		return ImageIO.read(GameFrame.class.getResource(pathOnClasspath));
        } catch (IOException e) {return null;}}
   
    
    public Renderer (BufferStrategy bs, GameObjects gameObjects, PathControl pathControl, Dimension screenSize){
        this.bs = bs;
        renderLoop = new Thread(this);
        this.gameObjects = gameObjects;
        this.pathControl = pathControl;
        this.screenSize = screenSize;
    
    }
    
    public void rendererStart(){
        renderLoop.start();
    }
    
    
    
    private void render(Graphics2D g2d){
        
        RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints (rh);
        
        // screen background
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, screenSize.width, screenSize.height);
        
        // test grid
        g2d.setColor(Color.gray.brighter());
        for (int i=0; i<15; i++){
      
            g2d.drawLine(i*100, 0, i*100, screenSize.height);
            g2d.drawLine(0, i*100, screenSize.width, i*100);
        }
        
        
        
        AffineTransform preCentred = g2d.getTransform();
        
        
        // centre 0,0 in the middle of user screen
        AffineTransform centred = new AffineTransform();
        centred.translate(screenSize.width/2, screenSize.height/2);
        g2d.transform(centred);
        
        g2d.setColor(Color.red);
        BubbleTile[][] bubbleTile = gameObjects.getBubbleTile();
        int tileCount = gameObjects.getTileCount();
        for (int x =0; x<tileCount; x++){
            for (int y=0; y<tileCount; y++){
                // if the tile is inactive just draw the image
                if (!bubbleTile[x][y].getActive()){
                    g2d.drawImage(bubbleTile[x][y].getTileImage(), -PITCHSIZE/2+x*TILESIZE-15, -PITCHSIZE/2+y*TILESIZE-15, null);
                } else{
                    // draw the bubbles on that tile
                    for (Bubble bubble: bubbleTile[x][y].getBubble()){
                        int radius = bubble.getRadius();
                        int bubbleX = (int)bubble.getBubblePoint().x;
                        int bubbleY = (int)bubble.getBubblePoint().y;
                        Ellipse2D.Double bubbleShape = new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
                        AffineTransform bubblePoint = new AffineTransform();
                        // decide which bubbleImage to use based on x,y coords
                        bubblePoint.translate(bubbleX, bubbleY);
                        g2d.transform(bubblePoint);
                        g2d.draw(bubbleShape);
                        g2d.setTransform(centred);
                        
                    }
                }
                
            }
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
            
            int type = gameObjects.getStationaryObjectsList().get(i).getPaintType();
            ArrayList<Shape> shapeList = gameObjects.getStationaryObjectsList().get(i).getObjectShapes();
            paintShapeByType(g2d, type, shapeList);
            
                      
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
            
            int type = gameObjects.getMoveableObjectsList().get(i).getPaintType();
            ArrayList<Shape> shapeList = gameObjects.getMoveableObjectsList().get(i).getObjectShapes();
            paintShapeByType(g2d, type, shapeList);
         
            g2d.setTransform(ot);

        }
        
        // test draw the stationary obstacles for pathing
        // stationary obstacles kpolygons are drawn in absolute space 0,0 top left
        int size = pathControl.getStationaryObstacles().size();
        for (int i =0; i<size; i++){           
            // test draw the stationary obstacles
            
            
            g2d.setColor(Color.black);
            KPolygon drawKPolygon = pathControl.getStationaryObstacles().get(i).getPolygon();
            g2d.draw(drawKPolygon);
            
        }
        
        
        
      for (int i=0; i<gameObjects.getMoveableObjectsList().size(); i++){
          KPoint startPoint = new KPoint(gameObjects.getMoveableObjectsList().get(i).getTransform().getTranslationX(),
        gameObjects.getMoveableObjectsList().get(i).getTransform().getTranslationY());
        
        if (gameObjects.getMoveableObjectsList().get(i).getCurrentPath()!=null){
            ArrayList<KPoint> pathPoints = gameObjects.getMoveableObjectsList().get(i).getCurrentPath();
            if (pathPoints.size() > 0){
                KPoint p = pathPoints.get(0);
                for (int j = 1; j < pathPoints.size(); j++) {
                    KPoint p2 = pathPoints.get(j);
                    //p2 = pathControl.avoidClippingCorners(p2, 64);
                    g2d.draw(new Line2D.Double(p.x, p.y, p2.x, p2.y));
                    float d = 5f;
                    g2d.fill(new Ellipse2D.Double(p2.x - d / 2f, p2.y - d / 2f, d, d));
                    p = p2;
                }
            }
        }
      }
      //test draw path from testObject[0] to 350, 350
          
        
        
        // set transform back to precentred
        g2d.setTransform(preCentred);
    }

    
    private void paintShapeByType(Graphics2D g2d, int type, ArrayList<Shape> shapeList){
        switch(type){
                case (0):
                    for (Shape shapeList1 : shapeList) {
                        g2d.draw(shapeList1);
                    }
                    break;
                case(1):
                    for (Shape shapeList1 : shapeList) {
                        g2d.fill(shapeList1);
                    }
                    break;     
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
