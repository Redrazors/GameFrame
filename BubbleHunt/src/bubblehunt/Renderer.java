/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt;

import static bubblehunt.StaticFields.PITCHSIZE;
import static bubblehunt.StaticFields.TILESIZE;
import bubblehunt.gameobjects.Bubble;
import bubblehunt.gameobjects.BubbleTile;
import bubblehunt.gameobjects.GameObjects;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private FPSCounter fPSCounter;
    
    private KPoint activeTopLeft, activeBottomRight;
    
    private BufferedImage testTile;
    
    private BufferedImage transOval[];
    private SoundControl soundControl;
    
    
    private BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        try {
		return ImageIO.read(GameFrame.class.getResource(pathOnClasspath));
        } catch (IOException e) {return null;}}
   
    
    public Renderer (BufferStrategy bs, GameObjects gameObjects, PathControl pathControl, Dimension screenSize, 
            SoundControl soundControl){
        this.bs = bs;
        renderLoop = new Thread(this);
        this.gameObjects = gameObjects;
        this.pathControl = pathControl;
        this.screenSize = screenSize;
        this.soundControl = soundControl;
        
        fPSCounter = new FPSCounter();
        fPSCounter.start();
        
        activeTopLeft= new KPoint(0-screenSize.width/2, 0-screenSize.height/2);
        activeBottomRight = new KPoint (screenSize.width/2, screenSize.height/2);
        
        transOval = new BufferedImage[6];
        
        testTile = getImageSuppressExceptions("img/test50tile.png");
        
        initTransOvals();
    }
    
    public void rendererStart(){
        renderLoop.start();
    }
    
    private void initTransOvals(){
        for (int i =0; i<6; i++){
            int size = (i+10)*2;
            int radius=10+i;
            transOval[i]= new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            AffineTransform centre = new AffineTransform();
            centre.translate(i+10, i+10);
            Graphics2D g2d = transOval[i].createGraphics();
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            g2d.fillOval(-radius, -radius, radius*2, radius*2);
            g2d.dispose();
            
        }
    }
    
    
    
    private void redrawTiles(){
        for (BubbleTile bubbleTile: pathControl.getTilesToRedraw()){
            changeTileImage(bubbleTile);
            pathControl.getTilesToRedraw().remove(bubbleTile);

        }
    }
    
    public void changeTileImage(BubbleTile bubbleTile){
        Graphics2D g2d = bubbleTile.getTileImage().createGraphics();
        // set the drawing to transparent
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        AffineTransform bubblePoint = new AffineTransform();
        RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints (rh);
        AffineTransform normal = g2d.getTransform();
        
        for (Bubble bubble: bubbleTile.getBubblesToRemove()){

            
            int radius = bubble.getRadius();
            // eg point is -958, -940...  top left point is -1000, -1000
            // 
            KPoint topLeftPoint = bubbleTile.getTopLeftPoint();
            int localX = (int)(bubble.getBubblePoint().x-topLeftPoint.x)+15;//+15 for the buffer around each image
            int localY = (int)(bubble.getBubblePoint().y-topLeftPoint.y)+15;
            //Ellipse2D.Double bubbleShape = new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
            
            bubblePoint.translate(localX, localY);
            g2d.transform(bubblePoint);
            g2d.drawImage(getTransOval(radius), -radius, -radius, null);
            //g2d.fillOval(-radius, -radius, radius*2, radius*2);
            //g2d.draw(bubbleShape);
            g2d.setTransform(normal);
            
            
            // remove this from the list
            bubbleTile.getBubblesToRemove().remove(bubble);
            
            
            // play pop
            soundControl.playPop(bubble);
        }
        
        g2d.dispose();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }
    
    private BufferedImage getTransOval(int radius){
        int ref = radius-10;
        return transOval[ref];
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
        // set x and y of each tiles
        for (int x =0; x<tileCount; x++){
            for (int y=0; y<tileCount; y++){
                
                // is the tile on screen?
                
                if (bubbleTile[x][y].getTopLeftPoint().x+TILESIZE+30>activeTopLeft.x &&
                        bubbleTile[x][y].getTopLeftPoint().y+TILESIZE+30>activeTopLeft.y &&
                        bubbleTile[x][y].getTopLeftPoint().x<activeBottomRight.x &&
                        bubbleTile[x][y].getTopLeftPoint().y<activeBottomRight.y){

                            g2d.drawImage(bubbleTile[x][y].getTileImage(), -PITCHSIZE/2+x*TILESIZE-15, -PITCHSIZE/2+y*TILESIZE-15, null);

                } //end is tile on screen if

                
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
        
        
          
        
        
        // set transform back to precentred
        g2d.setTransform(preCentred);
        g2d.drawString(Double.toString(fPSCounter.fps()), 50, 50);
        
        
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
                redrawTiles();
                fPSCounter.interrupt();
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
