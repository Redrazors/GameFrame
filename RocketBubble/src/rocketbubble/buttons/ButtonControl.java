/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.buttons;

import java.awt.Dimension;
import java.util.ArrayList;
import rocketbubble.MasterClass;
import rocketbubble.actions.ActionClearOrders;
import rocketbubble.actions.ActionExecuteOrders;
import rocketbubble.actions.ActionGameMenu;
import rocketbubble.actions.ActionOrderFirst;
import rocketbubble.actions.ActionOrderLast;
import rocketbubble.actions.ActionOrderNext;
import rocketbubble.actions.ActionOrderPrevious;
import rocketbubble.actions.ActionResetLevel;
import rocketbubble.actions.ActionSetOrders;
import rocketbubble.actions.ActionUpgradeShip;
import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class ButtonControl {
    private MasterClass masterClass;
    private ArrayList<GameButton> buttonList;
    private ArrayList<GameButton> orderButtonList;
    private GameButton buttonOrderPanelClick;
    
    private int barWidth, fuelBarStart;
    
    
    public ButtonControl(MasterClass masterClass){
        this.masterClass = masterClass;
        buttonList = new ArrayList();
        orderButtonList = new ArrayList();
        initButtons();
        
    }
    
    private void initButtons(){
        
        
        int buttonStartX = (masterClass.getScreenSize().width-900)/2;
        barWidth = buttonStartX;
        fuelBarStart = buttonStartX+900;
        
        ActionExecuteOrders actionExecuteOrders = new ActionExecuteOrders(masterClass);
        ActionSetOrders actionSetOrders = new ActionSetOrders(masterClass);
        ActionClearOrders actionClearOrders = new ActionClearOrders(masterClass);
        ActionResetLevel actionResetLevel = new ActionResetLevel(masterClass);
        ActionGameMenu actionGameMenu = new ActionGameMenu(masterClass);
        ActionUpgradeShip actionUpgradeShip = new ActionUpgradeShip(masterClass);
        
        

        GameButton buttonExecuteOrders = new GameButton(buttonStartX, 5 , new Dimension(150, 30),actionExecuteOrders, 
            "Execute Orders");
        GameButton buttonSetOrders = new GameButton(buttonStartX+150, 5 , new Dimension(150, 30),actionSetOrders, 
            "Set Orders");
        GameButton buttonClearOrders = new GameButton(buttonStartX+300, 5 , new Dimension(150, 30),actionClearOrders, 
            "Clear Orders");
        GameButton buttonResetLevel = new GameButton(buttonStartX+450, 5 , new Dimension(150, 30),actionResetLevel, 
            "Reset Level");
        GameButton buttonGameMenu = new GameButton(buttonStartX+600, 5 , new Dimension(150, 30),actionGameMenu, 
            "Game Menu");
        GameButton buttonUpgradeShip = new GameButton(buttonStartX+750, 5 , new Dimension(150, 30),actionUpgradeShip, 
            "Upgrade Ship");
             
        buttonList.add(buttonExecuteOrders);
        buttonList.add(buttonSetOrders);
        buttonList.add(buttonClearOrders);
        buttonList.add(buttonResetLevel);
        buttonList.add(buttonGameMenu);
        buttonList.add(buttonUpgradeShip);
        
        
        // order panel
        int x = (int)masterClass.getOrderControl().getOrdersTransform().getTranslateX();
        int y = (int)masterClass.getOrderControl().getOrdersTransform().getTranslateY();
        buttonOrderPanelClick = new GameButton(x, y, new Dimension(200, 200), null, 
            "Order Panel Click");
        
        
        
        
        // order buttons
        ActionOrderFirst actionOrderFirst = new ActionOrderFirst(masterClass);
        ActionOrderPrevious actionOrderPrevious = new ActionOrderPrevious(masterClass);
        ActionOrderNext actionOrderNext = new ActionOrderNext(masterClass);
        ActionOrderLast actionOrderLast = new ActionOrderLast(masterClass);
        
        GameButton buttonOrderFirst = new GameButton(5, 5 , new Dimension(20, 20),actionOrderFirst, 
            "First Order");
        GameButton buttonOrderPrevious = new GameButton(30, 5 , new Dimension(10, 20),actionOrderPrevious, 
            "Previous Order");
        GameButton buttonOrderNext = new GameButton(160, 5 , new Dimension(10, 20),actionOrderNext, 
            "Next Order");
        GameButton buttonOrderLast = new GameButton(175, 5 , new Dimension(20, 20),actionOrderLast, 
            "Last Order");
        orderButtonList.add(buttonOrderFirst);
        orderButtonList.add(buttonOrderPrevious);
        orderButtonList.add(buttonOrderNext);
        orderButtonList.add(buttonOrderLast);
        
        
        
    }
    
    public ArrayList<GameButton> getOrderButtonList(){
        return orderButtonList;
    }
    
    public int getBarWidth(){
        return barWidth;
    }
    
    public void checkButtons (KPoint checkPoint){
        boolean alreadyFound=false;
        for (GameButton gameButton : buttonList) {
            // check if the 
            //System.out.println("looking for button");
            int x1 = gameButton.getTopLeftX();
            int x2 = x1+gameButton.getButtonDimension().width;
            int y1 = gameButton.getTopLeftY();
            int y2 = x1+gameButton.getButtonDimension().height;
            
            if (checkPoint.x>=x1 && checkPoint.x<=x2){
                if (checkPoint.y>=y1 && checkPoint.y<=y2){
                    // button is found
                    //System.out.println("found button");
                    gameButton.getButtonAction().actionPerformed(null);
                    alreadyFound=true;
                    break;
                }
                
            }
            
        }
        
        if (!alreadyFound){
            int x1 = buttonOrderPanelClick.getTopLeftX();
            int x2 = x1+buttonOrderPanelClick.getButtonDimension().width;
            int y1 = buttonOrderPanelClick.getTopLeftY();
            int y2 = x1+buttonOrderPanelClick.getButtonDimension().height;
            
            if (checkPoint.x>=x1 && checkPoint.x<=x2){
                if (checkPoint.y>=y1 && checkPoint.y<=y2){
                    // button is found
                    //System.out.println("found button");
                    //buttonOrderPanelClick.getButtonAction().actionPerformed(null);
                    checkOrderPanelButtons(checkPoint);
                }
                
            }
        }
        
        
        
        
    }
    
    private void checkOrderPanelButtons(KPoint checkPoint){
        // subtract the affine transform translation from the kpoint
        checkPoint.x-=masterClass.getOrderControl().getOrdersTransform().getTranslateX();
        checkPoint.y-=masterClass.getOrderControl().getOrdersTransform().getTranslateY();
        
        //System.out.println("Order panel clicked");
        for (GameButton orderButton: orderButtonList){
            int x1 = orderButton.getTopLeftX();
            int x2 = x1+orderButton.getButtonDimension().width;
            int y1 = orderButton.getTopLeftY();
            int y2 = x1+orderButton.getButtonDimension().height;
            
            if (checkPoint.x>=x1 && checkPoint.x<=x2){
                if (checkPoint.y>=y1 && checkPoint.y<=y2){
                    // button is found
                    //System.out.println("found button");
                    orderButton.getButtonAction().actionPerformed(null);
                    break;
                }
                
            }
        }
    }
    
    public ArrayList<GameButton> getButtonList(){
        return buttonList;
    }
    
}
