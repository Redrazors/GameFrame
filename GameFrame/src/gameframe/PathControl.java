/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import static gameframe.StaticFields.CLIP_CLEARANCE;
import static gameframe.StaticFields.ROTATION_SPEED;
import static gameframe.StaticFields.FORCE_AMOUNT;
import gameframe.gameobjects.GameObjects;
import gameframe.gameobjects.MoveableObject;
import java.util.ArrayList;
import org.dyn4j.geometry.Vector2;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathFinder;

/**
 *
 * @author David
 */
public class PathControl {
    
    private GameObjects gameObjects;
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    private NodeConnector nodeConnector;
    private PathFinder pathFinder;
    private double maxConnectionDistanceBetweenObstacles;
    
    public PathControl(GameObjects gameObjects){
        this.gameObjects = gameObjects;
        
        stationaryObstacles = gameObjects.getStationaryObstacles();
        
        nodeConnector  = new NodeConnector();
        
        maxConnectionDistanceBetweenObstacles = 1000;
        
        // complete the nodes for straightedge
        for (int k = 0; k < stationaryObstacles.size(); k++){
            nodeConnector.addObstacle(stationaryObstacles.get(k), stationaryObstacles, maxConnectionDistanceBetweenObstacles);
        }
        pathFinder = new PathFinder();
        
        
    }
    
    public ArrayList<KPoint> getPathPoints(KPoint startPoint, KPoint endPoint){
        
        // Calculate the shortest path
        double maxConnectionDistanceFromStartAndEndPointsToObstacles = maxConnectionDistanceBetweenObstacles;
        PathData pathData= pathFinder.calc(startPoint, endPoint, maxConnectionDistanceFromStartAndEndPointsToObstacles, nodeConnector, stationaryObstacles);
        
        // moove the kpoints the clip clearance away from the object
        for (KPoint point : pathData.points) {
            KPoint adjustedPoint = avoidClippingCorners(point);
            point.setCoords(adjustedPoint);
        }
        
        // now need to check that these points are not within the clip clearance of another object
        // but would that check that the lines leading to those points didn't clip another object?
        
        return pathData.points;
    }
    
    public KPoint avoidClippingCorners(KPoint nextPoint){
        KPoint unClipped = nextPoint;
        //System.out.println(nextPoint);
        for (int x=-1; x<2; x++){
            for (int y=-1; y<2; y++){
                KPoint testPoint = new KPoint();
                testPoint.x = nextPoint.x+x*10;
                testPoint.y = nextPoint.y+y*10;
                // find out in next point +xy*10 is in stationary obstacles
                for (int i=0; i< stationaryObstacles.size(); i++){
                    KPolygon innerPolygon = ((PathBlockingObstacle)stationaryObstacles.get(i)).getInnerPolygon();
                    if (innerPolygon.contains(testPoint)){
                        unClipped.x = nextPoint.x-x*CLIP_CLEARANCE;
                        unClipped.y = nextPoint.y-y*CLIP_CLEARANCE;// use double negatives to invert where blockage found
                    }
                }
            }
        }
        return unClipped;
    }
    
    public void moveObjects(){
        //repeat for each moveable object
        for (int i=0; i<gameObjects.getMoveableObjectsList().size(); i++){
            
            MoveableObject movingOb = gameObjects.getMoveableObjectsList().get(i);
            
            if (movingOb.getPathDestination()!=null){
                
                KPoint start = movingOb.getPathLocation();
                KPoint destination = movingOb.getPathDestination();
            
                ArrayList<KPoint> pathPoints = getPathPoints(start, destination);
            
                // find the angle the moveable object needs to rotate to by comparing start with the next path point
                KPoint nextPoint=pathPoints.get(1);
                double targetAngle = Math.atan2(nextPoint.y-start.y, nextPoint.x-start.x );
                double angleRemaining = movingOb.getTransform().getRotation() - targetAngle;
            
                // fix for beyond pi range
                if (angleRemaining>Math.PI)angleRemaining -=2*Math.PI;
                if (angleRemaining<-Math.PI)angleRemaining +=2*Math.PI;
            
                if (angleRemaining > 0.05){                    
                    movingOb.rotateAboutCenter(-ROTATION_SPEED*movingOb.getSpeed());
                } else if (angleRemaining <-0.05){
                    movingOb.rotateAboutCenter(ROTATION_SPEED*movingOb.getSpeed());

                } else {
                    double angle = movingOb.getTransform().getRotation();
                    int xAdjust = (int)Math.ceil(Math.cos(angle)*FORCE_AMOUNT*movingOb.getSpeed());
                    int yAdjust = (int)Math.ceil(Math.sin(angle)*FORCE_AMOUNT*movingOb.getSpeed());
                    movingOb.applyForce(new Vector2(xAdjust,yAdjust));
                }
            }
            
        }
    }
    
   
}
