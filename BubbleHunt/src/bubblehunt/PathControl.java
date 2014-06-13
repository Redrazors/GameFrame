/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt;

import static bubblehunt.StaticFields.FORCE_AMOUNT;
import static bubblehunt.StaticFields.ROTATION_SPEED;
import static bubblehunt.StaticFields.TILESIZE;
import static bubblehunt.StaticFields.PITCHSIZE;

import bubblehunt.gameobjects.BubbleTile;
import bubblehunt.gameobjects.GameObjects;
import bubblehunt.gameobjects.MoveableObject;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap nodeMap, obstaclesMap;   
    private PathFinder pathFinder;
    private double maxConnectionDistanceBetweenObstacles;
    private PolygonBufferer bufferer;
    
    public PathControl(GameObjects gameObjects){
        this.gameObjects = gameObjects;  
        stationaryObstacles = gameObjects.getStationaryObstacles(); 
        nodeConnector  = new NodeConnector();   
        nodeMap = new HashMap();
        obstaclesMap = new HashMap(); 
        maxConnectionDistanceBetweenObstacles = 1000;
        pathFinder = new PathFinder();
        bufferer = new PolygonBufferer ();     
        
       setNodeConnectors();
    }
    

    
    private void setNodeConnectors(){
        // go through each moveable object
        for (MoveableObject moveableObject : gameObjects.getMoveableObjectsList()) {
            // if the hashmap doesn't contain that radius then make the nodes
             if (!nodeMap.containsKey(moveableObject.getObjectRadius())){
                 bufferedStationaryObstacles = new ArrayList();
                 bufferedNodeConnector = new NodeConnector();
                // go through each obstace and buffer it by the object radius -0.5
                for (PathBlockingObstacle stationaryObstacle : stationaryObstacles) {
                    KPolygon buffered1 = bufferer.buffer(stationaryObstacle.getPolygon(), moveableObject.getObjectRadius()-0.5, PathBlockingObstacleImpl.NUM_POINTS_IN_A_QUADRANT);
                    bufferedStationaryObstacles.add(PathBlockingObstacleImpl.createObstacleFromInnerPolygon(buffered1));
                }
                // add these obstacles to a buffered node list
                for (PathBlockingObstacle bufferedStationaryObstacle : bufferedStationaryObstacles) {
                    bufferedNodeConnector.addObstacle(bufferedStationaryObstacle, bufferedStationaryObstacles, maxConnectionDistanceBetweenObstacles);
                }
                // add this node list to the hashmap with the radius as key
                nodeMap.put(moveableObject.getObjectRadius(), bufferedNodeConnector);
                obstaclesMap.put(moveableObject.getObjectRadius(), bufferedStationaryObstacles);
             }
            
            
        }
        
    }
    
    public ArrayList<KPoint> getPathPoints(KPoint startPoint, KPoint endPoint, int objectRadius){
        // go through the hashmap and get the appropriate nodeConnector
        double maxConnectionDistanceFromStartAndEndPointsToObstacles = maxConnectionDistanceBetweenObstacles;
        NodeConnector node = (NodeConnector) nodeMap.get(objectRadius);
        ArrayList<PathBlockingObstacle> obstacles = (ArrayList)obstaclesMap.get(objectRadius);
        
        return  pathFinder.calc(startPoint, endPoint, maxConnectionDistanceFromStartAndEndPointsToObstacles, 
                node, obstacles).points;
    }
    
   
    
    
    public void moveObjectsAlongPath(){
        //repeat for each moveable object
        for (int i=0; i<gameObjects.getMoveableObjectsList().size(); i++){
            
            MoveableObject movingOb = gameObjects.getMoveableObjectsList().get(i);
            
            if (movingOb.getPathDestination()!=null ){
                
                KPoint start = movingOb.getPathLocation();
                KPoint destination = movingOb.getPathDestination();
                
                ArrayList<KPoint> pathPoints = getPathPoints(start, destination, movingOb.getObjectRadius());
                movingOb.setCurrentPathTest(pathPoints);
         
            }
            
            if(movingOb.getCurrentPath().size()>0){
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
                    double distance = start.distance(nextPoint);
                    double distMult = 1;
                    if (distance<=100){
                        distMult = distance/100;
                    }
                    double angle = movingOb.getTransform().getRotation();
                    int xAdjust = (int)Math.ceil(Math.cos(angle)*FORCE_AMOUNT*movingOb.getSpeed()*distMult);
                    int yAdjust = (int)Math.ceil(Math.sin(angle)*FORCE_AMOUNT*movingOb.getSpeed()*distMult);
                    movingOb.setLinearVelocity(new Vector2(xAdjust,yAdjust));
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
    
    //test for drawing the obstacles
    public ArrayList<PathBlockingObstacle> getStationaryObstacles(){
        ArrayList<PathBlockingObstacle> obstacles = (ArrayList)obstaclesMap.get(gameObjects.getMoveableObjectsList().get(0).getObjectRadius());
        return obstacles;
    }
    
    public void setActiveTiles(){
        BubbleTile[][] bubbleTile = gameObjects.getBubbleTile();
        
        for (MoveableObject movingOb: gameObjects.getMoveableObjectsList()){
            // go through each moveable object and set its tile as active
            int x = (int)movingOb.getTransform().getTranslationX();
            int y = (int)movingOb.getTransform().getTranslationY();
            
            int tileX = (int)Math.floor((PITCHSIZE/2+x)/TILESIZE);
            int tileY = (int)Math.floor((PITCHSIZE/2+y)/TILESIZE);
            
            // set the block of 9 tiles active
            for (int i=-1; i<2; i++){
                for (int j=-1; j<2; j++){
                    bubbleTile[tileX+i][tileY+j].setActive(true);
                }
            }
            
            KPoint currentTile = new KPoint (tileX, tileY);
            // if the stored current tile isn't the same as the tile it is actually on
            // then ???
            if (movingOb.getCurrentTile()!=currentTile){
                // need to work out the direction of travel then deselect based on that
                
            }
            
        }
    }
   
}
