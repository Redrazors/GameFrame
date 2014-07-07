/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author David
 */
public class BubbleRenderer {
    
    private BufferedImage bubbles[];
    public BubbleRenderer(){
        
        bubbles = new BufferedImage[6];
        
        generateBubbleImages();
        
    }
    
    private void generateBubbleImages(){
        
        for (int i=0; i<6; i++){
            int size = (10+i)*2;
            bubbles[i] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bubbles[i].createGraphics();
            RenderingHints rh = g2d.getRenderingHints(); rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints (rh);
            AffineTransform middle = new AffineTransform();
            middle.translate(10+i, 10+i);
            g2d.transform(middle);
            paintBubble(g2d, 10+i);
            g2d.dispose();
            
            
        }
    }
    
    private void paintBubble(Graphics2D g2d, int radius){
        //Color trans = new Color(0, 0, 0, 0);
        Color bluelight = new Color(102, 255, 255);
        Color bluemid = new Color(0, 128, 255);
        Point2D center = new Point2D.Float(0.0f, 0.0f);
	Point2D focus = new Point2D.Float(0.0f, 0.0f);
	float[] dist = { 0.4f, 0.99f };
	Color[] colors = { bluelight, bluemid };
	RadialGradientPaint p = new RadialGradientPaint(center, radius, focus,
			dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g2d.setPaint(p);
        g2d.fillOval(-radius, -radius, radius*2, radius*2);
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(-radius/2, -radius/2, 8, 4);
    }
    
    public BufferedImage getPaintedBubble(int radius){
        return bubbles[radius-10];
    }
    
}
