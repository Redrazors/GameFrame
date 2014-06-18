/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt;

import static bubblehunt.StaticFields.TILESIZE;
import bubblehunt.gameobjects.Bubble;
import bubblehunt.gameobjects.BubbleTile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class TileRenderer extends Thread {
    private boolean renderTile = false;
    private CopyOnWriteArrayList<BubbleTile> nextTileToRender;
    
    public TileRenderer(){
        nextTileToRender = new CopyOnWriteArrayList();
    }
    
    
    //public void setRenderTile(boolean bool){
        //renderTile = bool;
    //}
    
    public void addTileToRender(BubbleTile tile){
        nextTileToRender.add(tile);
                
    }
    
    private void render(){
        for (BubbleTile nextTile: nextTileToRender){
            changeTileImage(nextTile);
            nextTileToRender.remove(nextTile);
        }
        
    }
    
    public void changeTileImage(BubbleTile bubbleTile){
        BufferedImage tileImage = new BufferedImage(TILESIZE+30, TILESIZE+30, BufferedImage.TYPE_INT_ARGB);
        for (Bubble bubble: bubbleTile.getBubble()){
            int radius = bubble.getRadius();
            // eg point is -958, -940...  top left point is -1000, -1000
            // 
            KPoint topLeftPoint = bubbleTile.getTopLeftPoint();
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
            //paintBubble(g2d, bubble.getBubblePoint(), bubble.getRadius());
            g2d.draw(bubbleShape);
            g2d.setTransform(normal);
            g2d.dispose();
        }
        bubbleTile.setTileImage(tileImage);
        //bubbleTile.setActive(false);
        
    }
    
    public void run(){
        while (true){//lazy me, add a condition for an finishable thread
            
            try{
                render();
                Thread.sleep(10); // longer than one frame
            }
            catch (InterruptedException e){

            }
        }
    }
}
