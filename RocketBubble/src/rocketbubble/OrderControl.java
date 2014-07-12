/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.dyn4j.geometry.Vector2;
import static rocketbubble.StaticFields.FORCE_AMOUNT;
import static rocketbubble.StaticFields.ROTATION_SPEED;
import rocketbubble.gameobjects.GameObjects;
import rocketbubble.gameobjects.MoveableObject;

/**
 *
 * @author David
 */
public class OrderControl {
    private ArrayList<Order> orderList;
    private int currentViewOrder=0, currentExecuteOrder=0;
    private double orderTimer;
    private MoveableObject heroRocket;
    private AffineTransform ordersTransform;
    
    public OrderControl(GameObjects gameObjects){
        heroRocket = gameObjects.getHero();
        orderList = new ArrayList();
        ordersTransform = new AffineTransform();
        orderTimer=0;
        
        
        ordersTransform.translate(300, 300);
        
    }
    
    public void setOrderTimer(){
        orderTimer = orderList.get(currentExecuteOrder).getTime();
    }
    
    public void executeOrders(){
        // rotate to given angle
        double targetAngle = orderList.get(currentExecuteOrder).getAngleRadians();
        double angleRemaining = heroRocket.getTransform().getRotation()-targetAngle;
        
        // fix for beyond pi range
        if (angleRemaining>Math.PI)angleRemaining -=2*Math.PI;
        if (angleRemaining<-Math.PI)angleRemaining +=2*Math.PI;
                //System.out.println(angleRemaining);
        if (angleRemaining > 0.01){ 
             heroRocket.rotateAboutCenter(-0.01);
        } else if (angleRemaining <-0.01){
            heroRocket.rotateAboutCenter(0.01);
        } else if (angleRemaining>0){
            heroRocket.rotateAboutCenter(-angleRemaining);
        } else if (angleRemaining<0){
            heroRocket.rotateAboutCenter(angleRemaining);
        }
        //
        //System.out.println(angleRemaining);
        double angle = heroRocket.getTransform().getRotation();
                    //
        int thrust = orderList.get(currentExecuteOrder).getThrust();
        //System.out.println(thrust);
        int xAdjust = (int)Math.ceil(Math.cos(angle)*thrust*heroRocket.getSpeed());
        int yAdjust = (int)Math.ceil(Math.sin(angle)*thrust*heroRocket.getSpeed());
        heroRocket.applyForce(new Vector2(xAdjust,yAdjust));
        //heroRocket.getLinearVelocity().set(xAdjust, yAdjust);
        
        
    }
    
    public double getOrderTimer(){
        return orderTimer;
    }
    
    public void adjustOrderTimer(double adjust){
        orderTimer+=adjust;
        //System.out.println(orderTimer);
    }
    
    
    public void addOrder(int thrust, int angle, int time){
        Order newOrder = new Order (thrust, angle, time);
        orderList.add(newOrder);
    }
    
    public ArrayList<Order> getOrderList(){
        return orderList;
    }
    public int getCurrentViewOrder(){
        return currentViewOrder;
    }
    public void adjustCurrentViewOrder(int adjust){
        if (currentViewOrder+adjust >=0 && currentViewOrder+adjust<=orderList.size()-1){
            currentViewOrder+=adjust;
        }
        
    }
    public void rewindViewOrder(){
        currentViewOrder=0;
    }
    public void forwardViewOrder(){
        currentViewOrder = orderList.size()-1;
    }
    public void adjustCurrentExecuteOrder(int adjust){
        currentExecuteOrder+=adjust;
    }
    public void resetCurrentExecuteOrder(){
        currentExecuteOrder=0;
    }
    public int getCurrentExecuteOrder(){
        return currentExecuteOrder;
    }
    
    public AffineTransform getOrdersTransform(){
        return ordersTransform;
    }
    
}
