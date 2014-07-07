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
    private int thrust, angle, time;
    public Order (int thrust, int angle, int time){
        this.thrust = thrust;
        this.angle = angle;
        this.time = time;
        
    }
    
    public int getAngle(){
        return angle;
    }
    public int getThrust(){
        return thrust;
    }
    public int getTime(){
        return time;
    }
    
}
