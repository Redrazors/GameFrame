/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.gameobjects;

import static rocketbubble.StaticFields.ANGULAR_DAMPING;
import static rocketbubble.StaticFields.LINEAR_DAMPING;
import static rocketbubble.StaticFields.CLIP_CLEARANCE;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import straightedge.geom.KPolygon;
import static straightedge.geom.KPolygon.createRect;
import static straightedge.geom.KPolygon.createRegularPolygon;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;

/**
 *
 * @author David
 */
public class StationaryObject extends Body {
    
    private ArrayList<Shape> shapeList;
    private int paintType;
    private Color paintColor;
    private int xPos, yPos;
    
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    
    public StationaryObject(int xPos, int yPos, Color paintColor, int paintType, ArrayList<PathBlockingObstacle> stationaryOb){
        shapeList = new ArrayList();       
        this.paintColor = paintColor;
        this.paintType = paintType;
        this.xPos = xPos;
        this.yPos = yPos;
        
        this.stationaryObstacles = stationaryOb;
        
    }
    
    public void initObject(){
        this.translate(xPos, yPos);         
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.setMass(Mass.Type.INFINITE);
    }
    
    public void addFixture(Shape shape, int offsetX, int offsetY){
        BodyFixture collFix;
        
        KPolygon pathPoly;
        PathBlockingObstacle pathBlockingObstacle;
        
        switch (shape.getClass().getCanonicalName()){
            case "java.awt.geom.Rectangle2D.Double":
                // create and add fixture
                Rectangle2D.Double newRect = (Rectangle2D.Double)shape;
                Rectangle collisionRect = new Rectangle(newRect.width, newRect.height);
                collFix = new BodyFixture(collisionRect);
                collisionRect.translate(offsetX, offsetY); 
                collFix.setDensity(1);
                this.addFixture(collFix); 
                
                //offset shape and add to drawing ahape list
                newRect.x+=offsetX;
                newRect.y+=offsetY;
                shapeList.add(newRect);
                
                // add to obstacle list
                // do not add offsset again as it is added directly to x and y above
                double x1 = newRect.x;
                double y1 = newRect.y;
                double x2 = newRect.x+newRect.width;
                double y2 = newRect.y+newRect.height;
                pathPoly = new KPolygon(createRect(x1, y1, x2, y2));
                
                pathPoly.translate(xPos, yPos);
                pathBlockingObstacle = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(pathPoly);
                stationaryObstacles.add(pathBlockingObstacle);
                
                break;
                
            case "java.awt.geom.Ellipse2D.Double":
                
                Ellipse2D.Double newEllipse = (Ellipse2D.Double)shape;
                Ellipse collisionEllipse = new Ellipse(newEllipse.width, newEllipse.height);
                collFix = new BodyFixture(collisionEllipse);
                collisionEllipse.translate(offsetX, offsetY);
                collFix.setDensity(1);
                this.addFixture(collFix);
               
                //offset shape and add to drawing ahape list
                newEllipse.x+=offsetX;
                newEllipse.y+=offsetY;
                shapeList.add(newEllipse);
                
                // add to obstacle list
                pathPoly = new KPolygon(createRegularPolygon(20, newEllipse.width/2));
                pathBlockingObstacle = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(pathPoly);
                stationaryObstacles.add(pathBlockingObstacle);
                break;           
        }
        
    }
    
    public ArrayList<Shape> getObjectShapes(){
        return shapeList;
    }
    
    
    public int getPaintType(){
        return paintType;
    }
    
    public Color getPaintColor(){
        return paintColor;
    }
    
}
