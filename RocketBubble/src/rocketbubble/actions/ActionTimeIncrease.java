/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import rocketbubble.MasterClass;

/**
 *
 * @author David
 */
public class ActionTimeIncrease extends AbstractAction {
    
    private final MasterClass masterClass;
    
    public ActionTimeIncrease(MasterClass masterClass){
        this.masterClass = masterClass;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        masterClass.getOrderControl().getOrderList().get(masterClass.getOrderControl().getCurrentViewOrder()).adjustTime(masterClass.getButtonControl().getButtonIncreaseMultiplier());
        masterClass.getButtonControl().setHoldDownAction(this);
    }
    
}
