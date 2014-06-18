/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt.gameobjects;

import bubblehunt.GameFrame;
import bubblehunt.Renderer;
import static bubblehunt.StaticFields.TILESIZE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import straightedge.geom.KPoint;
/**
 *
 * @author David
 */
public class BubbleTile {
    private KPoint topLeftPoint;
    private BufferedImage tileImage;
    private BufferedImage readyTileImage;
    private CopyOnWriteArrayList<Bubble> tileBubbles;
    private boolean active=false;
    private boolean noLongerRequired = false;
    private boolean refreshImage = false;
    private int bubbleCount;
    
    private ArrayList<MoveableObject> objectsActivatingThis;
    private BufferedImage testTile;
    
    
    private BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        try {
		return ImageIO.read(GameFrame.class.getResource(pathOnClasspath));
        } catch (IOException e) {return null;}}
    
    public BubbleTile(KPoint topleft){
        this.topLeftPoint = topleft;
        
        
        tileBubbles = new CopyOnWriteArrayList();
        
        objectsActivatingThis = new ArrayList();
        testTile = getImageSuppressExceptions("img/test50tile.png");
    }
    
    
    public KPoint getTopLeftPoint(){
        return topLeftPoint;
    }
    
    
    public BufferedImage getTileImage(){
        //return testTile;
        readyTileImage = tileImage;
        return readyTileImage;
    }
    
    public void addBubble (Bubble bubble){
        tileBubbles.add(bubble);
    }
    
    public CopyOnWriteArrayList<Bubble> getBubble(){
        return tileBubbles;
    }
    
    
    public void setActive (boolean bool){
        active=bool;
    }
    
    public boolean getActive(){
        return active;
    }
    public boolean getNoLongerRequired(){
        return noLongerRequired;
    }
    public void setNoLongerRequired(boolean bool){
        noLongerRequired = bool;
    }
    
    
    public void initTileImage(){
        // this should only  be used at game init so bubble count should not be altered by this after first use
        bubbleCount = tileBubbles.size();
        tileImage = new BufferedImage(TILESIZE+30, TILESIZE+30, BufferedImage.TYPE_INT_ARGB);
        for (Bubble bubble: tileBubbles){
            int radius = bubble.getRadius();
            // eg point is -958, -940...  top left point is -1000, -1000
            // 
            int localX = (int)(bubble.getBubblePoint().x-topLeftPoint.x)+15;//+15 for the buffer around each image
            int localY = (int)(bubble.getBubblePoint().y-topLeftPoint.y)+15;
            Ellipse2D.Double bubbleShape = new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
            AffineTransform bubblePoint = new AffineTransform();
            Graphics2D g2d = tileImage.createGraphics();
            RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints (rh);
            AffineTransform normal = g2d.getTransform();
            g2d.setColor(Color.gray);
            
            bubblePoint.translate(localX, localY);
            g2d.transform(bubblePoint);
            g2d.draw(bubbleShape);
            g2d.setTransform(normal);
            g2d.dispose();
        }
        readyTileImage = tileImage;
    }
    
    public void setTileImage(BufferedImage image){
        tileImage = image;
        active=false;
    }
    public boolean getRefreshImage(){
        return refreshImage;
    }
    public void setRefreshImage(){
        int bubbleActual = tileBubbles.size();
        if (bubbleCount!= bubbleActual){
            refreshImage = true;
            bubbleCount = bubbleActual;
        } else {
            active=false;
            //refreshImage=false;
        }
        
    }
    
    
    
    public void addActivatingObject(MoveableObject movingOb){
        objectsActivatingThis.add(movingOb);
    }
    public ArrayList<MoveableObject> getObjectsActivatingThis(){
        return objectsActivatingThis;
    }
    
    
}
