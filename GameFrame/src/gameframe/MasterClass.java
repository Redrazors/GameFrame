/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import gameframe.gameobjects.GameObjects;
import static gameframe.StaticFields.NANO_TO_BASE;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

/**
 *
 * @author David
 */
public class MasterClass implements Runnable {
    
    private Renderer renderer;
    protected World world;
    private GameObjects gameObjects;
    /** The time stamp for the last iteration */
    protected long last;
    private Thread mainThread;
    
    
    
    public MasterClass (JFrame gameFrame, BufferStrategy bs){
        world = new World();
        world.setGravity(new Vector2(0,0));
        
        mainThread = new Thread (this);
        
        gameObjects = new GameObjects(world);
        
        
        renderer = new Renderer(bs, gameObjects);
        renderer.setIgnoreRepaint(true);
        gameFrame.add(renderer);
        renderer.rendererStart();
        
        
        
        
        
    }
    
    public void gameInit(){
        mainThread.start();
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
            updateWorld();
 
            try {
                mainThread.sleep(20);
            } catch (InterruptedException e) { }
        }
    }
    
}
