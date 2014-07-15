/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import rocketbubble.buttons.ButtonControl;
import straightedge.geom.KPoint;


/**
 *
 * @author David
 */
public class MouseControl implements MouseListener, MouseMotionListener{
    
    private ButtonControl buttonControl;
    private JComponent drawPanel;
    
    public MouseControl (ButtonControl buttonControl, JComponent drawPanel){
        this.buttonControl = buttonControl;
        this.drawPanel = drawPanel;
    }
    
    
    public KPoint getMouseScreenPos(){
       KPoint mouseScreenPos = new KPoint();
       Point mousePos = MouseInfo.getPointerInfo().getLocation();  
       Point panelPosition = drawPanel.getLocationOnScreen();
       
       mouseScreenPos.x= mousePos.x-panelPosition.x;
       mouseScreenPos.y= mousePos.y-panelPosition.y;
       //System.out.println(mouseScreenPos);
       return mouseScreenPos;      
   }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("clicked");
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonControl.checkButtons(getMouseScreenPos());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonControl.resetHoldDownTimer();
        buttonControl.setAngleBoxClicked(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
}
