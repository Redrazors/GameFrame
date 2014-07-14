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
    
    public GameLevel(KPoint startPoint, ArrayList<StationaryObject> stationaryObjectList){
        this.stationaryObjectList = stationaryObjectList;
        this.startPoint = startPoint;
        
    }
    
    public KPoint getStartPoint(){
        return startPoint;
    }
    
}
