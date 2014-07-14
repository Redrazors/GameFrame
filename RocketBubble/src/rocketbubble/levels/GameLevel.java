/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.levels;

import straightedge.geom.KPoint;

/**
 *
 * @author david
 */
public class GameLevel {
    
    private KPoint startPoint;
    
    public GameLevel(KPoint startPoint){
        
        this.startPoint = startPoint;
        
    }
    
    public KPoint getStartPoint(){
        return startPoint;
    }
    
}
