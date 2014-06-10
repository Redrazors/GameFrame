/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt.gameobjects;

import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class Bubble {
    
    private KPoint bubblePoint;
    private int radius;
    public Bubble (KPoint bubblePoint, int radius){
        
        this.bubblePoint = bubblePoint;
        this.radius = radius;
    }
    
    public KPoint getBubblePoint(){
        return bubblePoint;
    }
    public int getRadius(){
        return radius;
    }
    
    
}
