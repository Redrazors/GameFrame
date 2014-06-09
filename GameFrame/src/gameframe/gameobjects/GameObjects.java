/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.awt.Color;
import java.awt.Dimension;
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
import straightedge.geom.KPoint;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;


/**
 *
 * @author David
 */
public final class GameObjects {
    private World world;
    private MoveableObject testObject[];
    private ArrayList<MoveableObject> moveableObjectsList;
    private ArrayList<StationaryObject> stationaryObjectsList;//list of my stationary object 
    
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    private Dimension screenSize;
    
    
    public GameObjects (World world, Dimension screenSize){
        this.world = world;
        this.screenSize = screenSize;
        testObject = new MoveableObject[20];
        moveableObjectsList = new ArrayList();
        stationaryObjectsList = new ArrayList();
        stationaryObstacles = new ArrayList();
        
        
        
        // init all the objects here
        initTestObjects();
        
        
        

        
        
    }
    
    private void initTestObjects(){
        
        // add walls
        Rectangle2D.Double rh1 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        Rectangle2D.Double rh2 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        Rectangle2D.Double rv1 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        Rectangle2D.Double rv2 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        
        StationaryObject boundingWalls = new StationaryObject(0, 0, Color.GREEN, 1, stationaryObstacles);
        boundingWalls.addFixture(rh1, 0, -screenSize.height/2);
        boundingWalls.addFixture(rh2, 0, screenSize.height/2);
        boundingWalls.addFixture(rv1, -screenSize.width/2, 0);
        boundingWalls.addFixture(rv2, screenSize.width/2, 0);
        boundingWalls.initObject();
        this.world.addBody(boundingWalls);
        stationaryObjectsList.add(boundingWalls);

        // home obstacle
        Rectangle2D.Double homeRec = new Rectangle2D.Double(-50,-30, 100, 60);
        StationaryObject home = new StationaryObject(0, -50, Color.gray, 1, stationaryObstacles);
        home.addFixture(homeRec, 0, 0);
        home.initObject();
        this.world.addBody(home);
        stationaryObjectsList.add(home);
        
        // annother obstalce
        Rectangle2D.Double smallRec = new Rectangle2D.Double(-30,-10, 60, 20);
        StationaryObject smallRecStat = new StationaryObject(0, 80, Color.gray, 1, stationaryObstacles);
        smallRecStat.addFixture(smallRec, 0, -50);
        smallRecStat.initObject();
        this.world.addBody(smallRecStat);
        stationaryObjectsList.add(smallRecStat);
        
        Ellipse2D.Double testBigCirc = new Ellipse2D.Double(-30,-30, 60, 60);
        testObject[0] = new MoveableObject(-200, 0, Color.red, 1, 15); 
        testObject[0].addFixture(testBigCirc, 0, 0);
        //Rectangle2D.Double hitRect = new Rectangle2D.Double(-15, -30, 30, 60);
        //testObject[0].addFixture(hitRect, -30, 0);
        testObject[0].initObject();
        //add to world and game object list
        this.world.addBody(testObject[0]);
        moveableObjectsList.add(testObject[0]);
        //set test destination
        testObject[0].setPathDestination(new KPoint(200, 0));
        
        Ellipse2D.Double testSmallCirc = new Ellipse2D.Double(-15,-15, 30, 30);
        //testObject[1] = new MoveableObject(-200, 0, Color.BLUE, 1,15);
        //testObject[1].addFixture(testSmallCirc, 0, 0);
        //testObject[1].initObject();
        //add to world and game object list
        //this.world.addBody(testObject[1]);
        //moveableObjectsList.add(testObject[1]);
        //testObject[1].setPathDestination(new KPoint(200, 0));
        //set test speed
        //testObject[1].getLinearVelocity().set(-9000.0, 0);
              
    }
    
    public ArrayList<MoveableObject> getMoveableObjectsList(){
        return moveableObjectsList;
    }
    
    public ArrayList<StationaryObject> getStationaryObjectsList(){
        return stationaryObjectsList;
    }
    
    public ArrayList<PathBlockingObstacle> getStationaryObstacles(){
        return stationaryObstacles;
    }
    
    
    

    

    
   
    
}
