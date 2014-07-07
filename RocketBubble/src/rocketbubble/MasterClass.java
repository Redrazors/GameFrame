/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import static rocketbubble.StaticFields.NANO_TO_BASE;
import rocketbubble.gameobjects.GameObjects;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import javax.swing.JComponent;
import javax.swing.JFrame;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

/**
 *
 * @author David
 */
public class MasterClass implements Runnable {
    
    private Renderer renderer;
    private PathControl pathControl;
    protected World world;
    private GameObjects gameObjects;
    /** The time stamp for the last iteration */
    protected long last;
    private Thread mainThread;
    
    private double rotateTest=0.04;
    private int spiralCounter =0;
    private Dimension screenSize;
    
    private SoundControl soundControl;
    private OrderControl orderControl;
    
    private boolean executeOrders=false;
    
    
    
    public MasterClass (JFrame gameFrame, BufferStrategy bs, Dimension screenSize, JComponent drawPanel){
        this.screenSize = screenSize;
        world = new World();
        world.setGravity(new Vector2(0,0));
         
        ActionControl actionControl = new ActionControl(drawPanel);
        MouseControl mouseControl = new MouseControl();
        drawPanel.addMouseListener(mouseControl);
        drawPanel.addMouseMotionListener(mouseControl);
        
        mainThread = new Thread (this);
        soundControl = new SoundControl();
        gameObjects = new GameObjects(world, screenSize);
        pathControl=new PathControl(gameObjects);
        orderControl = new OrderControl();
        renderer = new Renderer(bs, gameObjects, pathControl, screenSize, soundControl, drawPanel, orderControl);
        //renderer.setIgnoreRepaint(true);
        //gameFrame.add(renderer);
        renderer.rendererStart();
        
        //
        //song1.play(true);
        
        // test
        //
        testSetOrders();
        
        
    }
    
    private void testSetOrders(){
        executeOrders=true;
    }
    
    
    public void gameInit(){
        mainThread.start();
        
        //world.shiftCoordinates(new Vector2(screenSize.width/2, screenSize.height/2));
    }
    
    public Renderer getRenderer(){
        return renderer;
    }
    
    public GameObjects getGameObjects(){
        return gameObjects;
    }
    
    public PathControl getPathControl(){
        return pathControl;
    }
    
    
    private void executeOrders(){
        
        // check timer, is there another order to execute
        
        if (orderControl.getOrderTimer()==0  && orderControl.getCurrentExecuteOrder()<orderControl.getOrderList().size()){
            orderControl.adjustCurrentExecuteOrder(1);
        } else {
            executeOrders=false;
            orderControl.resetCurrentExecuteOrder();
        }
        
        // if there are still orders to execute, do them
        if (executeOrders){
            
            
        }
        
        
        
        
        
    }
    
    
    private void moveObjects(){
        //gameObjects.getMoveableObjectsList().get(0).applyImpulse(new Vector2(1000.0, 100.0));
        //gameObjects.getMoveableObjectsList().get(0).rotateAboutCenter(rotateTest);
        //pathControl.moveObjectForward(gameObjects.getMoveableObjectsList().get(0), 100);
        //spiralCounter+=1;
        //if (spiralCounter==100){
            //rotateTest-=rotateTest/10;
            //spiralCounter=0;
            //System.out.println(rotateTest);
        //}
        
        pathControl.moveObjectsAlongPath();
        pathControl.bubbleCollision();
    }
    

    
    private void updateWorld(){
         // update the World
        long time = System.nanoTime();// get the elapsed time from the last iteration
        long diff = time - this.last;// set the last time
        this.last = time;// convert from nanoseconds to seconds
        double elapsedTime = (double)diff / NANO_TO_BASE;// update the world with the elapsed time
        this.world.update(elapsedTime);//<-- tell world to update with new positions of objects
        
        
    }

    @Override
    public void run() {
        while (true){
            if (executeOrders){
               executeOrders(); 
            }
            
            moveObjects();
            updateWorld();
            
 
            try {
                mainThread.sleep(10);
            } catch (InterruptedException e) { }
        }
    }
    
}
