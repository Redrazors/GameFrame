/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;


import static gameframe.StaticFields.ANGULAR_DAMPING;
import static gameframe.StaticFields.LINEAR_DAMPING;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;

/**
 *
 * @author David
 */
public class MoveableObject extends Body {
    
    private Shape objectShape;
    
    public MoveableObject(Circle hitCircle){
        
        int circleRad = (int)hitCircle.getRadius();
        objectShape = new Ellipse2D.Double(-circleRad, -circleRad, circleRad*2, circleRad*2);
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitCircle);     
    }
    
    public MoveableObject(Rectangle hitRect){
        
        double rectHeight = hitRect.getHeight();
        double rectWidth = hitRect.getWidth();
        objectShape = new Rectangle2D.Double(0, 0, rectWidth, rectHeight);
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitRect);     
    }
    
    public Shape getObjectShape(){
        return (Shape)objectShape;
    }
    

    
}
