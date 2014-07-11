/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.buttons;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.swing.AbstractAction;
import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class GameButton {
    private Dimension size;
    private String buttonName;
    private AbstractAction buttonAction;
    private Font buttonFont;
    
    private int topLeftX, topLeftY, textPointX, textPointY;
    
    public GameButton(int topLeftX, int topLeftY, Dimension size, AbstractAction buttonAction, String buttonName){
        
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.size =size;
        this.buttonAction = buttonAction;
        this.buttonName = buttonName;
        
        buttonFont = new Font("Lucida Console", Font.PLAIN, 12);
        
        
        
    }
    
    
    public  void calculateTextPoint(Graphics2D g2d){
        FontMetrics metrics = g2d.getFontMetrics(buttonFont);
        int height = metrics.getAscent();
        int width = metrics.stringWidth(buttonName);
        
        textPointX = (size.width-width)/2 + topLeftX;
        
        textPointY = height+(size.height-height)/2 + topLeftY;
       //System.out.println(textPointY);
    }
    
    public int getTextPointX(){
        return textPointX;
    }
    public int getTextPointY(){
        return textPointY;
    }
    
    public Font getButtonFont(){
        return buttonFont;
    }
    public int getTopLeftX(){
        return topLeftX;
    }
    public int getTopLeftY(){
        return topLeftY;
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
