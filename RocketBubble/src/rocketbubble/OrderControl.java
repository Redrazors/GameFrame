/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class OrderControl {
    private ArrayList<Order> orderList;
    private int currentViewOrder=0, currentExecuteOrder=-1;
    private double orderTimer;
    
    public OrderControl(){
        orderList = new ArrayList();
        orderTimer=0;
    }
    
    public double getOrderTimer(){
        return orderTimer;
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
        currentViewOrder+=adjust;
    }
    public void rewindViewOrder(){
        currentViewOrder=0;
    }
    public void forwardViewOrder(){
        currentViewOrder = orderList.size();
    }
    public void adjustCurrentExecuteOrder(int adjust){
        currentExecuteOrder+=adjust;
    }
    public void resetCurrentExecuteOrder(){
        currentExecuteOrder=-1;
    }
    public int getCurrentExecuteOrder(){
        return currentExecuteOrder;
    }
    
}
