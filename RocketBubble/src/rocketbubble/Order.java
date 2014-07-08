/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

/**
 *
 * @author david
 */
public class Order {
    private int thrust, time, angleDegrees;
    private double angleRadians;
    
    
    public Order (int thrust, int angle, int time){
        this.thrust = thrust;
        this.angleDegrees = angle;
        this.time = time;
        
        // convert angle to radians
       angleRadians = Math.toRadians(angle)-Math.PI/2;
        
    }
    
    
    public double getAngleRadians(){
        return angleRadians;
    }
    public int getAngleDegrees(){
        return angleDegrees;
    }
    public int getThrust(){
        return thrust;
    }
    public int getTime(){
        return time;
    }
    
}
