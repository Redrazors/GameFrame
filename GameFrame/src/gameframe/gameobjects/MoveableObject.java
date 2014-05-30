/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;


import static gameframe.StaticFields.ANGULAR_DAMPING;
import static gameframe.StaticFields.LINEAR_DAMPING;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;


/**
 *
 * @author David
 */
public class MoveableObject extends Body {
    
    private Shape objectShape;
    private int pathListIndex;
    
    public MoveableObject(Circle hitCircle, int xPos, int yPos){
        
        // create the render drawing shape
        int circleRad = (int)hitCircle.getRadius();
        objectShape = new Ellipse2D.Double(-circleRad, -circleRad, circleRad*2, circleRad*2);
        
        // adjust the collision detection details
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitCircle); 
        this.translate(xPos, yPos);
        

        
        
    }
    
    public MoveableObject(Rectangle hitRect, int xPos, int yPos){
        
        // create the render drawing shape
        double rectHeight = hitRect.getHeight();
        double rectWidth = hitRect.getWidth();
        objectShape = new Rectangle2D.Double(0, 0, rectWidth, rectHeight);
        
        // adjust the collision detection details
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitRect);    
        
    }
    

    
    public Shape getObjectShape(){
        return objectShape;
    }
    
    public int getPathListIndex(){
        return pathListIndex;
    }
    

    
}
