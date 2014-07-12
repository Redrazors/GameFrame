/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import com.vividsolutions.jts.geom.Polygon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.dyn4j.geometry.Vector2;
import static rocketbubble.StaticFields.PITCHSIZE;
import static rocketbubble.StaticFields.TILESIZE;
import rocketbubble.buttons.ButtonControl;
import rocketbubble.buttons.GameButton;
import rocketbubble.gameobjects.Bubble;
import rocketbubble.gameobjects.BubbleTile;
import rocketbubble.gameobjects.GameObjects;
import straightedge.geom.KPoint;

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
    private OrderControl orderControl;
    private ButtonControl buttonControl;
    
    private boolean showOrders = true;
    
    
    private BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        try {
		return ImageIO.read(GameFrame.class.getResource(pathOnClasspath));
        } catch (IOException e) {return null;}}
   
    
    public Renderer (BufferStrategy bs, GameObjects gameObjects, PathControl pathControl, Dimension screenSize, 
            SoundControl soundControl, JComponent drawPanel, OrderControl orderControl, ButtonControl buttonControl){
        this.bs = bs;
        renderLoop = new Thread(this);
        this.gameObjects = gameObjects;
        this.pathControl = pathControl;
        this.screenSize = screenSize;
        this.soundControl = soundControl;
        this.orderControl = orderControl;
        this.buttonControl = buttonControl;
        
        fPSCounter = new FPSCounter();
        fPSCounter.start();
        
        activeTopLeft= new KPoint(0-screenSize.width/2, 0-screenSize.height/2);
        activeBottomRight = new KPoint (screenSize.width/2, screenSize.height/2);
        
        transOval = new BufferedImage[6];
        setButtonPoints();
        
        testTile = getImageSuppressExceptions("img/test50tile.png");
        
        initTransOvals();
        
 
        
    }
    
    private void setButtonPoints(){
        Graphics2D g2d =null;
        try{
                g2d = (Graphics2D) bs.getDrawGraphics();
                for (GameButton gameButton: buttonControl.getButtonList()){
                    gameButton.calculateTextPoint(g2d);
                }
                g2d.dispose();
                if (!bs.contentsLost()) {
                    bs.show();
                }
           }
           catch (IllegalStateException e) { e.printStackTrace();}
    }
    
    private void renderOrdersBox(Graphics2D g2d){
        // transform to wherever the orders box is
        AffineTransform original = g2d.getTransform();
        
       
        g2d.transform(orderControl.getOrdersTransform());
        // draw the box
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 200, 200);
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, 200, 200);
        
        AffineTransform buttons = new AffineTransform();
        
        g2d.translate(buttonControl.getOrderButtonList().get(0).getTopLeftX(), buttonControl.getOrderButtonList().get(0).getTopLeftY());
        renderArrowBox(g2d, 2, 10);
        g2d.setTransform(orderControl.getOrdersTransform());
        g2d.translate(buttonControl.getOrderButtonList().get(1).getTopLeftX(), buttonControl.getOrderButtonList().get(1).getTopLeftY());
        renderArrowBox(g2d, 1, 10);
        g2d.setTransform(orderControl.getOrdersTransform());
        g2d.translate(buttonControl.getOrderButtonList().get(2).getTopLeftX(), buttonControl.getOrderButtonList().get(2).getTopLeftY());
        renderArrowBox(g2d, 1, 0);
        g2d.setTransform(orderControl.getOrdersTransform());
        g2d.translate(buttonControl.getOrderButtonList().get(3).getTopLeftX(), buttonControl.getOrderButtonList().get(3).getTopLeftY());
        renderArrowBox(g2d, 2, 0);
        g2d.setTransform(orderControl.getOrdersTransform());
        
        
        g2d.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        g2d.setColor(Color.black);
        String current = Integer.toString(orderControl.getCurrentViewOrder()+1);
        String total = Integer.toString(orderControl.getOrderList().size());
        g2d.drawString("Order " + current + " of " + total, 60, 20);
        
        // thrust 
        g2d.drawString("Thrust", 30, 60);
        int thrust = orderControl.getOrderList().get(orderControl.getCurrentViewOrder()).getThrust();
        g2d.drawString(Integer.toString(thrust), 120, 60);
        //angle
        g2d.drawString("Angle", 30, 120);
        
        // time
        g2d.drawString("For " + " seconds.", 30, 150);
        
        
        
        
        //Vector2 linVec = gameObjects.getHero().getLinearVelocity();
        //int xVel=(int)Math.round(linVec.x * 100);
        //int yVel=(int)Math.round(linVec.y * 100);
        //g2d.drawString("Force:" + Integer.toString(xVel) + " , " + Integer.toString(yVel), 10, 30);
        
        g2d.setTransform(original);
    }
    
    private void renderArrowBox(Graphics2D g2d, int num, int direction){
        g2d.drawRect(0, 0, 0+10*num, 20);
        int[] x = new int[3];
        int[] y = new int[3];
        
        for (int i=0; i<num; i++){
            x[0]=(direction+10*i); x[1]=(direction+10*i); x[2]=(10-direction+10*i);
            y[0]=0; y[1]=20; y[2]=10;
            int n = 3;

            g2d.fillPolygon(x, y, n);
        }
        
    }
    
    private void renderButtons(Graphics2D g2d){
        for (GameButton gameButton: buttonControl.getButtonList()){
            
            g2d.setColor(Color.white);
            g2d.setFont(gameButton.getButtonFont());
            g2d.fillRect(gameButton.getTopLeftX(), gameButton.getTopLeftY()+4, 
                    gameButton.getButtonDimension().width, gameButton.getButtonDimension().height);
            
            g2d.setColor(Color.black);
            g2d.drawRect(gameButton.getTopLeftX(),gameButton.getTopLeftY(), 
                    gameButton.getButtonDimension().width, gameButton.getButtonDimension().height);
            
            g2d.drawString(gameButton.getButtonName(), gameButton.getTextPointX(), gameButton.getTextPointY());
        }
        
    }
    
    private void renderStatusBars(Graphics2D g2d){
        g2d.setColor(Color.black);
        // health box
        g2d.drawRect(0, 5, buttonControl.getBarWidth(), 30);

        // fuel box
        g2d.drawRect(buttonControl.getBarWidth()+900, 5, buttonControl.getBarWidth(), 30);
        
        
        //health bar
        g2d.setColor(Color.red);
        int hBarW = (int)((buttonControl.getBarWidth()-6)*(gameObjects.getHero().getHealthPercent()/100));
        g2d.fillRect(buttonControl.getBarWidth()-hBarW-3, 8, hBarW, 24);
        //fuel bar
        g2d.setColor(Color.blue);
        g2d.fillRect(buttonControl.getBarWidth()+903, 8, (int)( (buttonControl.getBarWidth()-6)*(gameObjects.getHero().getFuelPercent()/100)), 24);
    
        g2d.setColor(Color.white);
        g2d.drawString("Integrity", buttonControl.getBarWidth()-75, 24);
        g2d.drawString("Fuel", buttonControl.getBarWidth()+910, 24);
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
        //g2d.setColor(Color.gray.brighter());
        //for (int i=0; i<15; i++){
      
            //g2d.drawLine(i*100, 0, i*100, screenSize.height);
            //g2d.drawLine(0, i*100, screenSize.width, i*100);
        //}
        
        
        
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
        
        
        // FPS Counter
        //g2d.drawString(Double.toString(fPSCounter.fps()), 50, 50);
        
        if (showOrders){
            renderOrdersBox(g2d);
        }
        
        renderButtons(g2d);
        
        renderStatusBars(g2d);

        
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
