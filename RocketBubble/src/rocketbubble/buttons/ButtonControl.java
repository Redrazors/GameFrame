/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.buttons;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import rocketbubble.MasterClass;
import rocketbubble.actions.ActionAngleChange;
import rocketbubble.actions.ActionAngleDecrease;
import rocketbubble.actions.ActionAngleIncrease;
import rocketbubble.actions.ActionClearOrders;
import rocketbubble.actions.ActionExecuteOrders;
import rocketbubble.actions.ActionGameMenu;
import rocketbubble.actions.ActionOrderFirst;
import rocketbubble.actions.ActionOrderLast;
import rocketbubble.actions.ActionOrderNext;
import rocketbubble.actions.ActionOrderPrevious;
import rocketbubble.actions.ActionResetLevel;
import rocketbubble.actions.ActionSetOrders;
import rocketbubble.actions.ActionThrustDecrease;
import rocketbubble.actions.ActionThrustIncrease;
import rocketbubble.actions.ActionThrustMax;
import rocketbubble.actions.ActionThrustMin;
import rocketbubble.actions.ActionTimeDecrease;
import rocketbubble.actions.ActionTimeIncrease;
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
    
    private double holdDownTimer=0, holdDownNextTick=0;
    private int buttonIncreaseMultiplier=1;
    
    private AbstractAction holdDownAction;
    private boolean buttonHeld=false;
    
    
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
        buttonOrderPanelClick = new GameButton(x, y, new Dimension(200, 250), null, 
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
        
        // thrust buttons
        ActionThrustMin actionThrustMin = new ActionThrustMin(masterClass);
        ActionThrustDecrease actionThrustDecrease = new ActionThrustDecrease(masterClass);
        ActionThrustIncrease actionThrustIncrease = new ActionThrustIncrease(masterClass);
        ActionThrustMax actionThrustMax = new ActionThrustMax(masterClass);
        
        GameButton buttonThrustMin = new GameButton(5, 60 , new Dimension(30, 15),actionThrustMin, 
            "Min");
        GameButton buttonThrustDecrease = new GameButton(40, 60 , new Dimension(15, 15),actionThrustDecrease, 
            "-");
        GameButton buttonThrustIncrease = new GameButton(145, 60 , new Dimension(15, 15),actionThrustIncrease, 
            "+");
        GameButton buttonThrustMax = new GameButton(165, 60 , new Dimension(30, 15),actionThrustMax, 
            "Max");
        
        orderButtonList.add(buttonThrustMin);
        orderButtonList.add(buttonThrustDecrease);
        orderButtonList.add(buttonThrustIncrease);
        orderButtonList.add(buttonThrustMax);
        
        ActionAngleChange actionAngleChange = new ActionAngleChange(masterClass);
        ActionAngleDecrease actionAngleDecrease = new ActionAngleDecrease(masterClass);
        ActionAngleIncrease actionAngleIncrease = new ActionAngleIncrease(masterClass);
        
        GameButton buttonAngleChange = new GameButton(65, 110 , new Dimension(70, 70),actionAngleChange, 
            "Change Angle");
        GameButton buttonAngleDecrease = new GameButton(15, 110 , new Dimension(20, 20),actionAngleDecrease, 
            "-");
        GameButton buttonAngleIncrease = new GameButton(165, 110 , new Dimension(20, 20),actionAngleIncrease, 
            "+");
        
        orderButtonList.add(buttonAngleChange);
        orderButtonList.add(buttonAngleDecrease);
        orderButtonList.add(buttonAngleIncrease);
        
        ActionTimeDecrease actionTimeDecrease = new ActionTimeDecrease(masterClass);
        ActionTimeIncrease actionTimeIncrease = new ActionTimeIncrease(masterClass);
        
        GameButton buttonTimeDecrease = new GameButton(15, 200 , new Dimension(20, 20),actionTimeDecrease, 
            "-");
        GameButton buttonTimeIncrease = new GameButton(165, 200 , new Dimension(20, 20),actionTimeIncrease, 
            "+");
        orderButtonList.add(buttonTimeDecrease);
        orderButtonList.add(buttonTimeIncrease);
        
        
        
        
    }
    
    public ArrayList<GameButton> getOrderButtonList(){
        return orderButtonList;
    }
    
    public int getBarWidth(){
        return barWidth;
    }
    
    public void checkButtons (KPoint checkPoint){
        //boolean alreadyFound=false;
        
        // check if we are in the top button area
        if (checkPoint.x >= barWidth && checkPoint.x<=barWidth+900 &&
                checkPoint.y>=5 && checkPoint.y<=35){
            
            for (GameButton gameButton : buttonList) {

                int x1 = gameButton.getTopLeftX();
                int x2 = x1+gameButton.getButtonDimension().width;
                int y1 = gameButton.getTopLeftY();
                int y2 = y1+gameButton.getButtonDimension().height;
            
                if (checkPoint.x>=x1 && checkPoint.x<=x2 && checkPoint.y>=y1 && checkPoint.y<=y2){
                        // button is found
                        //System.out.println("found button");
                        gameButton.getButtonAction().actionPerformed(null);
                        //alreadyFound=true;
                        break;
                
                } 
            
            }// end button for
            // check to find the order panel
        } else if (checkPoint.x>buttonOrderPanelClick.getTopLeftX() &&
                checkPoint.x <buttonOrderPanelClick.getTopLeftX()+buttonOrderPanelClick.getButtonDimension().width &&
                checkPoint.y>buttonOrderPanelClick.getTopLeftY() &&
                checkPoint.y<buttonOrderPanelClick.getTopLeftY()+buttonOrderPanelClick.getButtonDimension().height){
            //System.out.println("found order panel");
            checkOrderPanelButtons(checkPoint);
        }      
    }
    
    private void checkOrderPanelButtons(KPoint checkPoint){
        // subtract the affine transform translation from the kpoint
        checkPoint.x-=masterClass.getOrderControl().getOrdersTransform().getTranslateX();
        checkPoint.y-=masterClass.getOrderControl().getOrdersTransform().getTranslateY();
        
        //System.out.println("Order panel clicked "+ checkPoint);
        for (GameButton orderButton: orderButtonList){
            int x1 = orderButton.getTopLeftX();
            int x2 = x1+orderButton.getButtonDimension().width;
            int y1 = orderButton.getTopLeftY();
            int y2 = y1+orderButton.getButtonDimension().height;
            
            if (checkPoint.x>=x1 && checkPoint.x<=x2 && checkPoint.y>=y1 && checkPoint.y<=y2){
        
                    orderButton.getButtonAction().actionPerformed(null);
                    break;
                
            }
        }
    }
    
    public void adjustHoldDownTimer(double amount){
        holdDownTimer+=amount;
        holdDownNextTick+=amount;
        
        int mult=(int)(Math.floor(holdDownTimer));
        buttonIncreaseMultiplier = 1+mult*mult;
        
        
        if (holdDownNextTick>=0.2){
            holdDownButtonTick();
            holdDownNextTick=0;
        }

        
    }
    
    private void holdDownButtonTick(){
        // activate the clicked on buttons here again
        if (holdDownAction!=null){
            holdDownAction.actionPerformed(null);
        }
    }
    
    public void resetHoldDownTimer(){
        holdDownTimer=0;
        holdDownNextTick=0;
        buttonHeld=false;
        buttonIncreaseMultiplier=1;
    }
    
    public void setHoldDownAction(AbstractAction action){
        holdDownAction=action;
        buttonHeld=true;
        buttonIncreaseMultiplier=0;
    }
    public boolean getButtonHeldBoolean(){
        return buttonHeld;
    }
    public int getButtonIncreaseMultiplier(){
        return buttonIncreaseMultiplier;
    }
    
    public ArrayList<GameButton> getButtonList(){
        return buttonList;
    }
    
}
