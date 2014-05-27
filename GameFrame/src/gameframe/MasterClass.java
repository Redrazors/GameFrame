/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import static gameframe.StaticFields.NANO_TO_BASE;

/**
 *
 * @author David
 */
public class MasterClass implements Runnable {
    
    private Renderer renderer;
    protected World world;
    /** The time stamp for the last iteration */
    protected long last;
    private Thread mainThread;
    
    private MoveableObject testObject, testObject2;
    
    public MasterClass (JFrame gameFrame, BufferStrategy bs){
        world = new World();
        world.setGravity(new Vector2(0,0));
        
        mainThread = new Thread (this);
        initObjects();
        
        renderer = new Renderer(bs, testObject, testObject2);
        renderer.setIgnoreRepaint(true);
        gameFrame.add(renderer);
        renderer.rendererStart();
        
        
        
        
        
    }
    
    public void gameInit(){
        mainThread.start();
    }
    
    private void initObjects(){
          
        testObject = new MoveableObject();
        Rectangle hitBox = new Rectangle(20.0, 20.0);
        
        testObject.addFixture(hitBox);
        testObject.setMass();
        testObject.translate(100, 100); // these are on screen coords    
        testObject.setAngularDamping(0);
        testObject.setLinearDamping(0.1);
        world.addBody(testObject);
        
        testObject.getLinearVelocity().set(100.0, 0.0);
        
        testObject2 = new MoveableObject();
        testObject2.addFixture(hitBox);
        testObject2.setMass();
        testObject2.translate(200, 105); // these are on screen coords    
        testObject2.setAngularDamping(0);
        testObject2.setLinearDamping(0.1);
        world.addBody(testObject2);
        
        testObject2.getLinearVelocity().set(-100.0, 0.0);
      
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
                Thread.sleep(10);
            } catch (InterruptedException e) { }
        }
    }
    
}
