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
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import static straightedge.geom.KPolygon.createRect;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;

/**
 *
 * @author David
 */
public class MoveableObject extends Body {
    
    private Shape objectShape;
    private int pathListIndex;
    
    public MoveableObject(Circle hitCircle, PathingObstacles pathingObstacles, int xPos, int yPos){
        
        // create the render drawing shape
        int circleRad = (int)hitCircle.getRadius();
        objectShape = new Ellipse2D.Double(-circleRad, -circleRad, circleRad*2, circleRad*2);
        
        // adjust the collision detection details
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitCircle); 
        this.translate(xPos, yPos);
        
        // add object to path blocking obstacles
        Rectangle2D br = objectShape.getBounds();
        KPolygon pathingShape = new KPolygon(createRect(-circleRad, -circleRad, circleRad, circleRad));
        pathingShape.translate(xPos, yPos);
        //System.out.println(pathingShape);
        PathBlockingObstacle pathBlockingObstacle = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(pathingShape);
        pathingObstacles.getPathingObstacles().add(pathBlockingObstacle);
        pathListIndex = pathingObstacles.getPathingObstacles().size()-1; // take off 1 to start from array value of 0
        
        
        
        
    }
    
    public MoveableObject(Rectangle hitRect, PathingObstacles pathingObstacles, int xPos, int yPos){
        
        // create the render drawing shape
        double rectHeight = hitRect.getHeight();
        double rectWidth = hitRect.getWidth();
        objectShape = new Rectangle2D.Double(0, 0, rectWidth, rectHeight);
        
        // adjust the collision detection details
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.addFixture(hitRect);    
        
        // add object to path blocking obstacles
        KPolygon pathingShape = new KPolygon(createRect(0, 0, rectWidth, rectHeight));
        pathingShape.translate(xPos, yPos);
        //System.out.println(pathingShape);
        PathBlockingObstacle pathBlockingObstacle = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(pathingShape);
        pathingObstacles.getPathingObstacles().add(pathBlockingObstacle);
        pathListIndex = pathingObstacles.getPathingObstacles().size();
    }
    

    
    public Shape getObjectShape(){
        return (Shape)objectShape;
    }
    
    public int getPathListIndex(){
        return pathListIndex;
    }
    

    
}
