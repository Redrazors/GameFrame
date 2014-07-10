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
    
    public ButtonControl(MasterClass masterClass){
        this.masterClass = masterClass;
        buttonList = new ArrayList();
        initButtons();
        
    }
    
    private void initButtons(){
        ActionExecuteOrders actionExecuteOrders = new ActionExecuteOrders(masterClass);
        ActionSetOrders actionSetOrders = new ActionSetOrders(masterClass);
        ActionClearOrders actionClearOrders = new ActionClearOrders(masterClass);
        ActionResetLevel actionResetLevel = new ActionResetLevel(masterClass);
        ActionGameMenu actionGameMenu = new ActionGameMenu(masterClass);
        ActionUpgradeShip actionUpgradeShip = new ActionUpgradeShip(masterClass);

        GameButton buttonExecuteOrders = new GameButton(200, 50 , new Dimension(150, 30),actionExecuteOrders, 
            "Execute Orders");
        GameButton buttonSetOrders = new GameButton(350, 50 , new Dimension(150, 30),actionSetOrders, 
            "Set Orders");
        GameButton buttonClearOrders = new GameButton(500, 50 , new Dimension(150, 30),actionClearOrders, 
            "Clear Orders");
        GameButton buttonResetLevel = new GameButton(650, 50 , new Dimension(150, 30),actionResetLevel, 
            "Reset Level");
        GameButton buttonGameMenu = new GameButton(800, 50 , new Dimension(150, 30),actionGameMenu, 
            "Game Menu");
        GameButton buttonUpgradeShip = new GameButton(950, 50 , new Dimension(150, 30),actionUpgradeShip, 
            "Upgrade Ship");
        
        buttonList.add(buttonExecuteOrders);
        buttonList.add(buttonSetOrders);
        buttonList.add(buttonClearOrders);
        buttonList.add(buttonResetLevel);
        buttonList.add(buttonGameMenu);
        buttonList.add(buttonUpgradeShip);
        
        
    }
    
    public void checkButtons (KPoint checkPoint){
 
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
                    break;
                }
                
            }
            
        }
        
    }
    
    public ArrayList<GameButton> getButtonList(){
        return buttonList;
    }
    
}
