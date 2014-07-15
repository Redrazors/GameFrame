/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.levels;

import java.util.ArrayList;
import rocketbubble.gameobjects.StationaryObject;
import straightedge.geom.KPoint;

/**
 *
 * @author david
 */
public class GameLevel {
    private KPoint startPoint;
    private ArrayList<StationaryObject>stationaryObjectList;
    private StationaryObject floor;
    
    public GameLevel(){
        
    }
    
    public KPoint getStartPoint(){
        return startPoint;
    }
    
    public StationaryObject getFloor(){
        return floor;
    }
    
    public void setLevel(){
        
    }
    public ArrayList<StationaryObject> getStationaryObjectList(){
        return stationaryObjectList;
    }

    
}
