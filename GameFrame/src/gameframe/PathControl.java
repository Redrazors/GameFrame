/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import static gameframe.StaticFields.CLIP_CLEARANCE;
import static gameframe.StaticFields.FORCE_AMOUNT;
import static gameframe.StaticFields.ROTATION_SPEED;
import gameframe.gameobjects.GameObjects;
import gameframe.gameobjects.MoveableObject;
import java.util.ArrayList;
import org.dyn4j.geometry.Vector2;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import straightedge.geom.PolygonBufferer;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;
import straightedge.geom.path.PathFinder;

/**
 *
 * @author David
 */
public class PathControl {
    
    private GameObjects gameObjects;
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    private ArrayList<PathBlockingObstacle> bufferedStationaryObstacles;
    
    private NodeConnector nodeConnector;
    private NodeConnector bufferedNodeConnector;
    private PathFinder pathFinder;
    private double maxConnectionDistanceBetweenObstacles;
    private PolygonBufferer bufferer;
    
    public PathControl(GameObjects gameObjects){
        this.gameObjects = gameObjects;
        
        stationaryObstacles = gameObjects.getStationaryObstacles();
        bufferedStationaryObstacles = new ArrayList();
 
        
        nodeConnector  = new NodeConnector();
        bufferedNodeConnector = new NodeConnector();
        
        maxConnectionDistanceBetweenObstacles = 1000;
        
        // complete the nodes for straightedge
        for (int k = 0; k < stationaryObstacles.size(); k++){
            nodeConnector.addObstacle(stationaryObstacles.get(k), stationaryObstacles, maxConnectionDistanceBetweenObstacles);
        }
        pathFinder = new PathFinder();
        bufferer = new PolygonBufferer ();
        
        bufferObstacles();      
        
       
    }
    
    private void bufferObstacles(){
        for (PathBlockingObstacle stationaryObstacle : stationaryObstacles) {
            KPolygon buffered = bufferer.buffer(stationaryObstacle.getPolygon(), CLIP_CLEARANCE, PathBlockingObstacleImpl.NUM_POINTS_IN_A_QUADRANT);
            bufferedStationaryObstacles.add(PathBlockingObstacleImpl.createObstacleFromInnerPolygon(buffered));
        }
        for (PathBlockingObstacle bufferedStationaryObstacle : bufferedStationaryObstacles) {
            bufferedNodeConnector.addObstacle(bufferedStationaryObstacle, bufferedStationaryObstacles, maxConnectionDistanceBetweenObstacles);
        }
    }
    
    public ArrayList<KPoint> getPathPoints(KPoint startPoint, KPoint endPoint){
        double maxConnectionDistanceFromStartAndEndPointsToObstacles = maxConnectionDistanceBetweenObstacles;
        
     
        return pathFinder.calc(startPoint, endPoint, maxConnectionDistanceFromStartAndEndPointsToObstacles, 
                bufferedNodeConnector, bufferedStationaryObstacles).points;
    }
    
   
    
    
    public void moveObjectsAlongPath(){
        //repeat for each moveable object
        for (int i=0; i<gameObjects.getMoveableObjectsList().size(); i++){
            
            MoveableObject movingOb = gameObjects.getMoveableObjectsList().get(i);
            
            if (movingOb.getPathDestination()!=null ){
                
                KPoint start = movingOb.getPathLocation();
                KPoint destination = movingOb.getPathDestination();
            
                ArrayList<KPoint> pathPoints = getPathPoints(start, destination);
                movingOb.setCurrentPathTest(pathPoints);
                //System.out.println("got path " + pathPoints.size() + " * " + start + " * " + destination );
         
            }
            
            if(movingOb.getCurrentPath().size()>0){
                //System.out.println("got path");
                // find the angle the moveable object needs to rotate to by comparing start with the next path point
                KPoint start = movingOb.getPathLocation();
                KPoint nextPoint=movingOb.getCurrentPath().get(1);
                double targetAngle = Math.atan2(nextPoint.y-start.y, nextPoint.x-start.x );
                double angleRemaining = movingOb.getTransform().getRotation() - targetAngle;
            
                // fix for beyond pi range
                if (angleRemaining>Math.PI)angleRemaining -=2*Math.PI;
                if (angleRemaining<-Math.PI)angleRemaining +=2*Math.PI;
            
                if (angleRemaining > 0.1){ 
                    //movingOb.rotateAboutCenter(-angleRemaining);
                    movingOb.rotateAboutCenter(-ROTATION_SPEED*movingOb.getSpeed());
                } else if (angleRemaining <-0.1){
                    //movingOb.rotateAboutCenter(-ROTATION_SPEED*movingOb.getSpeed());
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
    
    public void moveObjectForward(MoveableObject movingOb, int forceAmount){
        double angle = movingOb.getTransform().getRotation();
        int xAdjust = (int)Math.ceil(Math.cos(angle)*forceAmount*movingOb.getSpeed());
        int yAdjust = (int)Math.ceil(Math.sin(angle)*forceAmount*movingOb.getSpeed());
        //movingOb.applyForce(new Vector2(xAdjust,yAdjust));
        movingOb.setLinearVelocity(new Vector2(xAdjust, yAdjust));
        
    }
    
   
}
