/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import javax.swing.JComponent;
import javax.swing.JFrame;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import static rocketbubble.StaticFields.NANO_TO_BASE;
import rocketbubble.actions.ActionControl;
import rocketbubble.actions.ActionExecuteOrders;
import rocketbubble.buttons.ButtonControl;
import rocketbubble.buttons.GameButton;
import rocketbubble.gameobjects.GameObjects;
import straightedge.geom.KPoint;

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
    private Dimension screenSize;
    
    private SoundControl soundControl;
    private OrderControl orderControl;
    
    private boolean executeOrders=false;
    
    
    
    public MasterClass (JFrame gameFrame, BufferStrategy bs, Dimension screenSize, JComponent drawPanel){
        this.screenSize = screenSize;
        world = new World();
        world.setGravity(new Vector2(0,1));
        gameObjects = new GameObjects(world, screenSize);
        orderControl = new OrderControl(gameObjects);
        ButtonControl buttonControl = new ButtonControl(this);
        ActionControl actionControl = new ActionControl(drawPanel, this);
        MouseControl mouseControl = new MouseControl(buttonControl, drawPanel);
        gameFrame.addMouseListener(mouseControl);
        gameFrame.addMouseMotionListener(mouseControl);
        
        mainThread = new Thread (this);
        soundControl = new SoundControl();
        
        pathControl=new PathControl(gameObjects);
        
        testSetOrders();
        
        renderer = new Renderer(bs, gameObjects, pathControl, screenSize, soundControl, drawPanel, orderControl, buttonControl);

        renderer.rendererStart();
        
        //
        //song1.play(true);
        
        // test
        //
        
        
        
        
        
    }
    
    public Dimension getScreenSize(){
        return screenSize;
    }
    
    private void testSetOrders(){
        orderControl.addOrder(2000, 0, 2);
        orderControl.addOrder(1000, 50, 6);
        orderControl.addOrder(2000, 0, 2);
    }
   
    
    public void gameInit(){
        mainThread.start();
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
    
    public OrderControl getOrderControl(){
        return orderControl;
    }
    
    public void setInitialOrder(){
        orderControl.resetCurrentExecuteOrder();
        orderControl.setOrderTimer();
    }
    
    public void resetLevel(){
        System.out.println("levle reset test");
    }
    
    private void executeOrders(){
        // if the timer is up and this is the last order in the list
        if (orderControl.getOrderTimer()<=0 && orderControl.getCurrentExecuteOrder()+1 == orderControl.getOrderList().size()){
            executeOrders=false;
            orderControl.resetCurrentExecuteOrder();
        } else {
            // if there is a new order to execute
            if (orderControl.getOrderTimer()<=0  && orderControl.getCurrentExecuteOrder()<orderControl.getOrderList().size()){  // take one off size to adjust to 0 count
                //System.out.println("adjusted order count");
                orderControl.adjustCurrentExecuteOrder(1);
                orderControl.setOrderTimer();
            }
            // execute the orders
            orderControl.executeOrders();
            
        }   
    }
    
    
    private void moveObjects(){
        pathControl.moveObjectsAlongPath();
        pathControl.bubbleCollision();
    }
    

    
    private void updateWorld(){
         // update the World
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = (double)diff / NANO_TO_BASE;
        //System.out.println(elapsedTime);
        // update the world with the elapsed time
        this.world.update(elapsedTime);//<-- tell world to update with new positions of objects
        
        // adjust order time
        if (executeOrders){
            
            orderControl.adjustOrderTimer(-elapsedTime);
        }
        
        
    }
    
    public void setExecuteOrders(boolean bool){
        executeOrders = bool;
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
