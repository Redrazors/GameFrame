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
import rocketbubble.actions.ExecuteOrders;
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
        ExecuteOrders executeOrdersButton = new ExecuteOrders(masterClass);
        GameButton buttonExecute = new GameButton(new  KPoint(0,0), new Dimension(100, 20),executeOrdersButton, 
            "Execute Orders");
        buttonList.add(buttonExecute);
    }
    
    public void checkButtons (KPoint checkPoint){
 
        for (GameButton gameButton : buttonList) {
            // check if the 
            int x1 = (int)gameButton.getButtonKPoint().x;
            int x2 = x1+gameButton.getButtonDimension().width;
            int y1 = (int)gameButton.getButtonKPoint().y;
            int y2 = x1+gameButton.getButtonDimension().height;
            
            if (checkPoint.x>=x1 && checkPoint.x<=x2){
                if (checkPoint.y>=y1 && checkPoint.y<=y2){
                    // button is found
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
