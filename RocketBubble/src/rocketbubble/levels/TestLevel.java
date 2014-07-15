/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.levels;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.World;
import rocketbubble.gameobjects.StationaryObject;
import straightedge.geom.KPoint;
import straightedge.geom.path.PathBlockingObstacle;

/**
 *
 * @author david
 */
public class TestLevel extends GameLevel {
    
    private KPoint startPoint;
    private ArrayList<StationaryObject>stationaryObjectList;
    private StationaryObject floor;
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    private World world;
    
    public TestLevel(ArrayList<PathBlockingObstacle> stationaryObstacles, World world){
        this.stationaryObstacles = stationaryObstacles;
        this.world = world;
        
        stationaryObjectList = new ArrayList<>();
        
    }
    
    @Override
    public void setLevel(){
        startPoint = new KPoint(0,285);
        
        Rectangle2D.Double homeRec = new Rectangle2D.Double(-100,-5, 200, 10);
        floor = new StationaryObject(0, 295, Color.gray, 1, stationaryObstacles);
        floor.addFixture(homeRec, 0, 0);
        floor.initObject();
        
        this.world.addBody(floor);
        
        
        Rectangle2D.Double rh1 = new Rectangle2D.Double(-10, -200, 20, 400);
        StationaryObject wall1 = new StationaryObject(-110, 100, Color.gray, 1, stationaryObstacles);
        wall1.addFixture(rh1, 0, 0);
        wall1.initObject();
        this.world.addBody(wall1);
        stationaryObjectList.add(wall1);
        
        //Rectangle2D.Double rh2 = new Rectangle2D.Double(0, 0, 20, 400);
        StationaryObject wall2 = new StationaryObject(110, 100, Color.gray, 1, stationaryObstacles);
        wall2.addFixture(rh1,0, 0);
        wall2.initObject();
        this.world.addBody(wall2);
        stationaryObjectList.add(wall2);
   
        
    }
    
    @Override
    public KPoint getStartPoint(){
        return startPoint;
    }
    
    @Override
    public StationaryObject getFloor(){
        return floor;
    }
    
    @Override
    public ArrayList<StationaryObject> getStationaryObjectList(){
        return stationaryObjectList;
    }
    
}
