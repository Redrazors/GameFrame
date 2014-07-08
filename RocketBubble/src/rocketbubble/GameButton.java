/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import java.awt.Dimension;
import straightedge.geom.KPoint;

/**
 *
 * @author David
 */
public class GameButton {
    private KPoint topLeft;
    private Dimension size;
    public GameButton(KPoint topLeft, Dimension size){
        
        this.topLeft = topLeft;
        this.size =size;
        
    }
    
}
