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
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
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
    private CopyOnWriteArrayList<Bubble> tileBubbles;
    
    private CopyOnWriteArrayList<Bubble> bubblesToRemove;
    private BufferedImage testTile;
    private BubbleRenderer bubbleRenderer;
    
    private BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        try {
		return ImageIO.read(GameFrame.class.getResource(pathOnClasspath));
        } catch (IOException e) {return null;}}
    
    public BubbleTile(KPoint topleft, BubbleRenderer bubbleRenderer){
        this.topLeftPoint = topleft;
        this.bubbleRenderer = bubbleRenderer;
        
        tileBubbles = new CopyOnWriteArrayList();
        bubblesToRemove = new CopyOnWriteArrayList();

        testTile = getImageSuppressExceptions("img/test50tile.png");
    }
    
    
    public KPoint getTopLeftPoint(){
        return topLeftPoint;
    }
    
    public void addBubbleToRemove(Bubble bubble){
        bubblesToRemove.add(bubble);
    }
    public CopyOnWriteArrayList<Bubble> getBubblesToRemove(){
        return bubblesToRemove;
    }
    
    public BufferedImage getTileImage(){
        //return testTile;
        //readyTileImage = tileImage;
        return tileImage;
    }
    
    public void addBubble (Bubble bubble){
        tileBubbles.add(bubble);
    }
    
    public CopyOnWriteArrayList<Bubble> getBubble(){
        return tileBubbles;
    }
    
    
    
    
    public void initTileImage(){
        // this should only  be used at game init so bubble count should not be altered by this after first use
        tileImage = new BufferedImage(TILESIZE+30, TILESIZE+30, BufferedImage.TYPE_INT_ARGB);
        for (Bubble bubble: tileBubbles){
            int radius = bubble.getRadius();
            // eg point is -958, -940...  top left point is -1000, -1000
            // 
            int localX = (int)(bubble.getBubblePoint().x-topLeftPoint.x)+15;//+15 for the buffer around each image
            int localY = (int)(bubble.getBubblePoint().y-topLeftPoint.y)+15;
            //Ellipse2D.Double bubbleShape = new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
            AffineTransform bubblePoint = new AffineTransform();
            Graphics2D g2d = tileImage.createGraphics();
            RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints (rh);
            AffineTransform normal = g2d.getTransform();
            g2d.setColor(Color.gray);
            
            bubblePoint.translate(localX, localY);
            g2d.transform(bubblePoint);
            //paintBubble(g2d, radius );
            g2d.drawImage(bubbleRenderer.getPaintedBubble(radius), -radius, -radius, null);
            //g2d.draw(bubbleShape);
            g2d.setTransform(normal);
            g2d.dispose();
        }
    }
    
    private void paintBubble(Graphics2D g2d, int radius){
        Color trans = new Color(0, 0, 0, 0);
        Point2D center = new Point2D.Float(0.0f, 0.0f);
	Point2D focus = new Point2D.Float(0.0f, 0.0f);
	float[] dist = { 0.6f, 0.9f };
	Color[] colors = { trans, Color.BLUE.brighter().brighter().brighter() };
	RadialGradientPaint p = new RadialGradientPaint(center, radius, focus,
			dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g2d.setPaint(p);
        g2d.fillOval(-radius, -radius, radius*2, radius*2);
    }
    
    public void setTileImage(BufferedImage image){
        tileImage = image;
    }



    
    
}
