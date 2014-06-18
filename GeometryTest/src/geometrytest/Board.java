/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geometrytest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class Board extends JPanel implements Runnable {
    private Thread geoLoop;
    private FPSCounter fpsCounter;
    
    public Board(){
        geoLoop = new Thread(this);
        fpsCounter=new FPSCounter();
        fpsCounter.start();
    }
    
    public void boardInit(){
        geoLoop.start();
    }
    
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 500, 500);
        g2d.setColor(Color.red);
        g2d.drawString(Double.toString(fpsCounter.fps()), 50, 50);
        //g2d.fillRect(0, 0, 50, 50);
        
        Rectangle2D.Double testRect = new Rectangle2D.Double(200, 170, 100, 100);
        Ellipse2D.Double testEllipse = new Ellipse2D.Double(200, 0, 50, 50);
        
        Shape testShape = testRect;
        //g2d.fill(testShape);
        switch (testShape.getClass().getCanonicalName()){
            case "java.awt.geom.Rectangle2D.Double":
                Rectangle2D.Double newRect = (Rectangle2D.Double)testShape;
                
                g2d.fill(newRect);
                //System.out.println(testShape.getBounds().x + testShape.getBounds().y);
                break;
            case "java.awt.geom.Ellipse2D.Double":
                Ellipse2D.Double newEllipse = (Ellipse2D.Double)testShape;
                g2d.fill(newEllipse);
                break;           
        }
        
        
        
        
        //System.out.println(testShape.getClass().getCanonicalName());
        
    }

    @Override
    public void run() {
        while (true){
            repaint();
            fpsCounter.interrupt();
            try {
                
                geoLoop.sleep(1);
            } catch (InterruptedException e) { }
        }
    }
    
}
