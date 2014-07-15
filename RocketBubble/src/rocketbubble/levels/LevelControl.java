/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.levels;

import java.util.ArrayList;
import org.dyn4j.dynamics.World;
import rocketbubble.MasterClass;
import rocketbubble.gameobjects.StationaryObject;
import straightedge.geom.KPoint;
import straightedge.geom.path.PathBlockingObstacle;

/**
 *
 * @author david
 */
public class LevelControl {
    private MasterClass masterClass;
    private World world;
    
    private ArrayList<GameLevel> levelList;
    private int currentLevel=0;
    
    private ArrayList<StationaryObject> stationaryObjectsList;//list of my stationary object 
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    
    
    public LevelControl(World world, MasterClass masterClass){
        this.world = world;
        this.masterClass = masterClass;
        levelList = new ArrayList<>();
        
        stationaryObjectsList = new ArrayList<>();
        stationaryObstacles = new ArrayList<>();
        
        addLevels();
    }
    
    public ArrayList<StationaryObject> getStationaryObjectsList(){
        return levelList.get(currentLevel).getStationaryObjectList();
    }
    
    public ArrayList<PathBlockingObstacle> getStationaryObstacles(){
        return stationaryObstacles;
    }
    
    public void addLevels(){
        TestLevel testLevel = new TestLevel(stationaryObstacles, world);
        levelList.add(testLevel);
        
    }
    
    public void setLevel(int level){
        
        levelList.get(level).setLevel();
        currentLevel=level;
        
    }
    
    public ArrayList<GameLevel> getGameLevels(){
        return levelList;
    }
    public int getCurrentLevel(){
        return currentLevel;
    }
    
}
