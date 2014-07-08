/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.buttons;

import java.awt.Dimension;
import javax.swing.AbstractAction;
import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class GameButton {
    private KPoint topLeft;
    private Dimension size;
    private String buttonName;
    private AbstractAction buttonAction;
    
    public GameButton(KPoint topLeft, Dimension size, AbstractAction buttonAction, String buttonName){
        
        this.topLeft = topLeft;
        this.size =size;
        this.buttonAction = buttonAction;
        this.buttonName = buttonName;
        
    }
    
    public KPoint getButtonKPoint(){
        return topLeft;
    }
    public Dimension getButtonDimension(){
        return size;
    }
    
    public AbstractAction getButtonAction (){
        return buttonAction;
    }
    public String getButtonName(){
        return buttonName;
    }
    
}
