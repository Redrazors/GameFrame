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
public class ActionThrustMin extends AbstractAction {
    
    private final MasterClass masterClass;
    
    public ActionThrustMin(MasterClass masterClass){
        this.masterClass = masterClass;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("min pressed");
        int orderNum = masterClass.getOrderControl().getCurrentViewOrder();
        masterClass.getOrderControl().getOrderList().get(orderNum).setThrustMin();
    }
    
}
