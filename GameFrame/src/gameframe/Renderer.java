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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JPanel;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

/**
 *
 * @author David
 */
public class Renderer extends JPanel implements Runnable {
    
    private Thread renderLoop;
    private BufferStrategy bs;
    
    private GameObjects gameObjects;
    private PathControl pathControl;
   
    
    public Renderer (BufferStrategy bs, GameObjects gameObjects, PathControl pathControl){
        this.bs = bs;
        renderLoop = new Thread(this);
        this.gameObjects = gameObjects;
        this.pathControl = pathControl;
       
        
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
        int size = gameObjects.getStationaryObstacles().size();
        for (int i =0; i<size; i++){           
            // test draw the stationary obstacles
            
            
            g2d.setColor(Color.black);
            KPolygon drawKPolygon = gameObjects.getStationaryObstacles().get(i).getPolygon();
            g2d.draw(drawKPolygon);
            
        }
        
        //test draw path from testObject[0] to 350, 350
        KPoint startPoint = new KPoint(gameObjects.getMoveableObjectsList().get(0).getTransform().getTranslationX(),
        gameObjects.getMoveableObjectsList().get(0).getTransform().getTranslationY());
        
        KPoint endPoint = new KPoint(350, 350);
        ArrayList<KPoint> pathPoints = pathControl.getPathPoints(startPoint, endPoint);
        if (pathPoints.size() > 0){
            KPoint p = pathPoints.get(0);
            for (int i = 1; i < pathPoints.size(); i++) {
                KPoint p2 = pathPoints.get(i);
                //p2 = pathControl.avoidClippingCorners(p2, 64);
                g2d.draw(new Line2D.Double(p.x, p.y, p2.x, p2.y));
                float d = 5f;
                g2d.fill(new Ellipse2D.Double(p2.x - d / 2f, p2.y - d / 2f, d, d));
                p = p2;
            }
        }      
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
                Thread.sleep(1);
            } catch (InterruptedException e) { }
        }
        
    }
    
}
