/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt;

import static bubblehunt.StaticFields.FORCE_AMOUNT;
import static bubblehunt.StaticFields.PITCHSIZE;
import static bubblehunt.StaticFields.ROTATION_SPEED;
import static bubblehunt.StaticFields.TILESIZE;
import bubblehunt.gameobjects.Bubble;
import bubblehunt.gameobjects.BubbleTile;
import bubblehunt.gameobjects.GameObjects;
import bubblehunt.gameobjects.MoveableObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private ArrayList<BubbleTile> activeTiles;
    
    private CopyOnWriteArrayList<BubbleTile> tilesToRedraw;
    
    
    
    public PathControl(GameObjects gameObjects){
        this.gameObjects = gameObjects;
        
        stationaryObstacles = gameObjects.getStationaryObstacles(); 
        nodeConnector  = new NodeConnector();   
        nodeMap = new HashMap();
        obstaclesMap = new HashMap(); 
        maxConnectionDistanceBetweenObstacles = 1000;
        pathFinder = new PathFinder();
        bufferer = new PolygonBufferer ();     
        
        activeTiles = new ArrayList();
       setNodeConnectors();
       
       tilesToRedraw = new CopyOnWriteArrayList();
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
                //System.out.println(angleRemaining);
                if (angleRemaining > 0.1){ 
                    //movingOb.rotateAboutCenter(-angleRemaining);
                    movingOb.rotateAboutCenter(-ROTATION_SPEED*movingOb.getSpeed());
                } else if (angleRemaining <-0.1){
                    //movingOb.rotateAboutCenter(-ROTATION_SPEED*movingOb.getSpeed());
                    movingOb.rotateAboutCenter(ROTATION_SPEED*movingOb.getSpeed());

                } else {
                    double distance = start.distance(nextPoint);
                    double distMult = 1;
                    // distmulti code not working properly
                    //if (distance<=100){
                        //distMult = distance/100;
                    //}
                    double angle = movingOb.getTransform().getRotation();
                    //System.out.println(angle);
                    int xAdjust = (int)Math.ceil(Math.cos(angle)*FORCE_AMOUNT*movingOb.getSpeed()*distMult);
                    int yAdjust = (int)Math.ceil(Math.sin(angle)*FORCE_AMOUNT*movingOb.getSpeed()*distMult);
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
    
    //test for drawing the obstacles
    public ArrayList<PathBlockingObstacle> getStationaryObstacles(){
        ArrayList<PathBlockingObstacle> obstacles = (ArrayList)obstaclesMap.get(gameObjects.getMoveableObjectsList().get(0).getObjectRadius());
        return obstacles;
    }
    
    public void bubbleCollision(){
        BubbleTile[][] bubbleTile = gameObjects.getBubbleTile();
        
        for (MoveableObject movingOb: gameObjects.getMoveableObjectsList()){
            // set the moving ob's active tile
            int x = (int)movingOb.getTransform().getTranslationX();
            int y = (int)movingOb.getTransform().getTranslationY();
            
            int tileX = (int)Math.floor((PITCHSIZE/2+x)/TILESIZE);
            int tileY = (int)Math.floor((PITCHSIZE/2+y)/TILESIZE);
            
            movingOb.setCurrentTile(new KPoint(tileX, tileY));
        }
        
 
        
        // do collision work for moving ob on its current tiles
        
        for (MoveableObject movingOb: gameObjects.getMoveableObjectsList()){
            int tileObX = (int)movingOb.getCurrentTile().x;
            int tileObY = (int)movingOb.getCurrentTile().y;
            
            // only on current tile for now 
            for (int x=-1; x<2; x++){
                for (int y=-1; y<2; y++){
                    int tileX = tileObX+x;
                    int tileY = tileObY+y;
                    if (tileX>=0 && tileX<gameObjects.getTileCount() && tileY>=0 && tileY<gameObjects.getTileCount()){
                        for (Bubble bubble: bubbleTile[tileX][tileY].getBubble()){
                            int totalRadius = bubble.getRadius()+movingOb.getObjectRadius();
                            int distance = (int)bubble.getBubblePoint().distance(movingOb.getPathLocation());
                            if (totalRadius>=distance){
                                //bubbleTile[tileX][tileY].getBubble().remove(bubble);
                                //add this tile to tile to redraw if it doesn't already exist
                                if (!tilesToRedraw.contains(bubbleTile[tileX][tileY])){
                                    tilesToRedraw.add(bubbleTile[tileX][tileY]);
                                }
                                // add this bubble to list of bubbles to redraw, if it has not already been done by another movingob
                                if (!bubbleTile[tileX][tileY].getBubblesToRemove().contains(bubble)){
                                    //System.out.println("added bubbe at " + bubbleTile[x][y] + " bubble " + bubble);
                                    
                                    bubbleTile[tileX][tileY].addBubbleToRemove(bubble);
                                    bubbleTile[tileX][tileY].getBubble().remove(bubble);
                                }
                                
                                
                                
                            }
                        } 
                    }
                      
                    
                    
                }//end y
            }// end x
            
        } // end tile collision for
        
        
    }
   
    public void addTileToRedraw(BubbleTile bubbleTile){
        tilesToRedraw.add(bubbleTile);
    }
    
    public CopyOnWriteArrayList<BubbleTile> getTilesToRedraw(){
        return tilesToRedraw;
    }
}
