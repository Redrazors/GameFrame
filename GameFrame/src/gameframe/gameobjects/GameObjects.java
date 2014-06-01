/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;


/**
 *
 * @author David
 */
public final class GameObjects {
    private World world;
    private MoveableObject testObject[];
    private ArrayList<MoveableObject> moveableObjectsList;
    private ArrayList<StationaryObject> stationaryObjectsList;
    
    
    
    public GameObjects (World world){
        this.world = world;
        testObject = new MoveableObject[20];
        moveableObjectsList = new ArrayList();
        stationaryObjectsList = new ArrayList();
        initTestObjects();
        
        
    }
    
    private void initTestObjects(){
        
        // add walls
        Rectangle2D.Double rh1 = new Rectangle2D.Double(-250,-10, 500, 20);
        Rectangle2D.Double rh2 = new Rectangle2D.Double(-250,-10, 500, 20);
        Rectangle2D.Double rv1 = new Rectangle2D.Double(-10,-250, 20, 500);
        Rectangle2D.Double rv2 = new Rectangle2D.Double(-10,-250, 20, 500);
        
        StationaryObject boundingWalls = new StationaryObject(rh1, 250, 10, Color.GREEN, 1);
        boundingWalls.addFixture(rh2, 0, 480);
        boundingWalls.addFixture(rv1, -240, 250);
        boundingWalls.addFixture(rv2, 240, 250);
        
        this.world.addBody(boundingWalls);
        stationaryObjectsList.add(boundingWalls);

        // home obstacle
        Rectangle2D.Double homeRec = new Rectangle2D.Double(-50,-30, 100, 60);
        StationaryObject home = new StationaryObject(homeRec, 250, 250, Color.BLACK, 1);
        this.world.addBody(home);
        stationaryObjectsList.add(home);
        
        Ellipse2D.Double hitCircle = new Ellipse2D.Double(-30,-30, 60, 60);
        testObject[0] = new MoveableObject(hitCircle, 50, 200, Color.red, 1);    
        Rectangle2D.Double hitRect = new Rectangle2D.Double(-15, -30, 30, 60);
        testObject[0].addFixture(hitRect, 30, 0); 
        //add to world and game object list
        this.world.addBody(testObject[0]);
        moveableObjectsList.add(testObject[0]);
        //set test speed
        //testObject[0].getLinearVelocity().set(9000.0, 0);
             
        testObject[1] = new MoveableObject(hitCircle, 400, 180, Color.BLUE, 1);         
        //add to world and game object list
        this.world.addBody(testObject[1]);
        moveableObjectsList.add(testObject[1]);
        //set test speed
        //testObject[1].getLinearVelocity().set(-9000.0, 0);
              
    }
    
    public ArrayList<MoveableObject> getMoveableObjectsList(){
        return moveableObjectsList;
    }
    
    public ArrayList<StationaryObject> getStationaryObjectsList(){
        return stationaryObjectsList;
    }
    
    
    

    

    
   
    
}
