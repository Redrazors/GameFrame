/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt.gameobjects;


import static bubblehunt.StaticFields.ANGULAR_DAMPING;
import static bubblehunt.StaticFields.LINEAR_DAMPING;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.Rectangle;
import straightedge.geom.KPoint;


/**
 *
 * @author David
 */
public class MoveableObject extends Body {
    
    private ArrayList<Shape> shapeList;
    private int paintType;
    private Color paintColor;
    private int xPos, yPos;
    private KPoint pathDestination;
    private KPoint currentTile;
    
    private ArrayList<KPoint> currentPath;
    
    private int objectRadius=0;
    
    private double speed=1.0;
    
    public MoveableObject(int xPos, int yPos, Color paintColor, int paintType, int objectRadius){
        shapeList = new ArrayList();       
        this.paintColor = paintColor;
        this.paintType = paintType;
        this.xPos = xPos;
        this.yPos = yPos;
        this.objectRadius = objectRadius;
        
        currentPath=new ArrayList();
        currentTile = new KPoint();        
    }
    
    public void initObject(){
        this.translate(xPos, yPos);         
        this.setAngularDamping(ANGULAR_DAMPING);
        this.setLinearDamping(LINEAR_DAMPING);
        this.setMass();
    }
    
    
    public void addFixture(Shape shape, int offsetX, int offsetY){
        BodyFixture collFix;
        
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
    
    public void setPathDestination(KPoint target){
       pathDestination = target;
   }
   
   public KPoint getPathDestination(){
       return pathDestination;
   } 
   
   public void clearPathFindingTarget(){
       pathDestination=null;
   }
   
   public KPoint getPathLocation(){
       return new KPoint(this.getTransform().getTranslationX(),this.getTransform().getTranslationY());
   }
   
   public double getSpeed(){
       return speed;
   }
   
   public void setSpeed(double speed){
       this.speed = speed;
   }
   
   // only for test drawing of path
   // can probably delete after testing
   public void setCurrentPathTest(ArrayList<KPoint> list){
       currentPath=list;
   }
   public ArrayList<KPoint> getCurrentPath(){
       return currentPath;
   }
   
   // test
   public int getObjectRadius(){
       return objectRadius;
   }
   
   public KPoint getCurrentTile(){
       return currentTile;
   }
   public void setCurrentTile(KPoint current){
       currentTile = current;
   }
   
            

    

    
}
