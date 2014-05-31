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
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;


/**
 *
 * @author David
 */
public class MoveableObject extends Body {
    
    private int pathListIndex;
    
    private ArrayList<Shape> shapeList;
    
    public MoveableObject(Shape shape, int xPos, int yPos){
        shapeList = new ArrayList();       
        
        //convert to dyn4j shape and add to fixture
       
        addFixture(shape, 0, 0);

        this.translate(xPos, yPos); 
        
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);

        // convert to kpolygon and add to straightedge collision
        
    }
    
    
    public void addFixture(Shape shape, int offsetX, int offsetY){
        switch (shape.getClass().getCanonicalName()){
            case "java.awt.geom.Rectangle2D.Double":
                // create and add fixture
                Rectangle2D.Double newRect = (Rectangle2D.Double)shape;
                Rectangle collisionRect = new Rectangle(newRect.width, newRect.height);
                collisionRect.translate(offsetX, offsetY);
                this.addFixture(collisionRect); 
                
                //offset shape and add to drawing ahape list
                newRect.x+=offsetX;
                newRect.y+=offsetY;
                shapeList.add(newRect);
                
                break;
            case "java.awt.geom.Ellipse2D.Double":
                
                Ellipse2D.Double newEllipse = (Ellipse2D.Double)shape;
                Circle collisionCirc = new Circle(newEllipse.width/2);
                //BodyFixture circFix = new BodyFixture(collisionCirc);
                collisionCirc.translate(offsetX, offsetY);
                this.addFixture(collisionCirc);
                
                //offset shape and add to drawing ahape list
                newEllipse.x+=offsetX;
                newEllipse.y+=offsetY;
                shapeList.add(newEllipse);
                break;           
        }
    }
    
    public void addCentredFixture(Shape shape){
        int x = (int)this.getTransform().getTranslation().x;
    }
    
    public ArrayList<Shape> getObjectShapes(){
        return shapeList;
    }
    
    
    public int getPathListIndex(){
        return pathListIndex;
    }
    

    
}
