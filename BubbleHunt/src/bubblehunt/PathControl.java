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
                    
                    // add to list of active tiles
                    activeTiles.add(bubbleTile[tileX+i][tileY+j]);
                    // add self to tile if not already active
                    if (!bubbleTile[tileX+i][tileY+j].getObjectsActivatingThis().contains(movingOb)){
                        bubbleTile[tileX+i][tileY+j].getObjectsActivatingThis().add(movingOb);
                    }
                    
                }
            }
            
            
            KPoint currentTile = new KPoint (tileX, tileY);
            
            // if the stored current tile isn't the same as the tile it is actually on
            // then ???
            if (movingOb.getCurrentTile()!=currentTile){
                // need to work out the direction of travel then deselect based on that
                int diffX = (int)(tileX - movingOb.getCurrentTile().x);
                int diffY = (int)(tileY - movingOb.getCurrentTile().y);
                
                // eg at 4,1 and move to 3,1 = -1x
                // invert the x and double it to get 2 squares away in the previosuly moved direction
                int invertDiffX = 0-diffX;
                int invertDiffY = 0-diffY;
                
                // x first - don't do it if it is 0 as it hasn't moved along x
                if (invertDiffX!=0){
                    
                    int removeX = invertDiffX-diffX+tileX;
                    int removeY = invertDiffY+tileY;
                    //System.out.println(removeX + " , " + removeY);
                    for (int i=-1; i<2; i++){
                        // check that the tile exists
                        if (removeY+i>=0 && removeY+i<gameObjects.getTileCount() && removeX>=0 && removeX<gameObjects.getTileCount() ){
                            if (bubbleTile[removeX][removeY+i].getObjectsActivatingThis().contains(movingOb)){
                                bubbleTile[removeX][removeY+i].getObjectsActivatingThis().remove(movingOb);
                            }
                        }
                        
                    }
                }
                
                if (invertDiffY!=0){
                    int removeX = invertDiffX + tileX;
                    int removeY = invertDiffY-diffY +tileY;
                    for (int i=-1; i<2; i++){
                        if (removeX+i>0 && removeX+i<gameObjects.getTileCount() && removeY>=0 && removeY<gameObjects.getTileCount() ){
                            if (bubbleTile[removeX+i][removeY].getObjectsActivatingThis().contains(movingOb)){
                                bubbleTile[removeX+i][removeY].getObjectsActivatingThis().remove(movingOb);
                            }
                        }   
                        
                    }
                }
                // set the current tile
                movingOb.setCurrentTile(new KPoint(tileX, tileY));
                // 
                // 1) Find out which tiles it no longer needs and remove itself from tile array list
                // 2) Go through tile array list after gameobject loop and deactivate any tiles that no have an arraylist length of 0
                // 3) activate any tiles with an arraylength >0 bubbleTile[tileX+i][tileY+j].setActive(true);
                
            }
            
            
            
        }
        // set the tiles active according to whether objects are listed as activating them
        // 1) Tile only deactivated in renderer once image creation is complete
        // 2) image is only refreshed if bubble count has changed
        ArrayList<BubbleTile> tilesToRemove = new ArrayList(); 
        for (BubbleTile activeTile: activeTiles){
            if (activeTile.getObjectsActivatingThis().isEmpty()){
                // only refresh if bubble count is changed
                activeTile.setRefreshImage();
                //activeTile.initTileImage();
                tilesToRemove.add(activeTile);
            } else {
                activeTile.setActive(true);
            }
        }
        
        // bubbles removed here
        // call animation and sound
        for (BubbleTile removeBubble: tilesToRemove){
            activeTiles.remove(removeBubble);
        }
        
        // do collision work for moving ob on its current tiles
        
        for (MoveableObject movingOb: gameObjects.getMoveableObjectsList()){
            int tileX = (int)movingOb.getCurrentTile().x;
            int tileY = (int)movingOb.getCurrentTile().y;
            
            // only on current tile for now 
            for (int x=-1; x<2; x++){
                for (int y=-1; y<2; y++){
                    for (Bubble bubble: bubbleTile[tileX+x][tileY+y].getBubble()){
                        int totalRadius = bubble.getRadius()+movingOb.getObjectRadius();
                        int distance = (int)bubble.getBubblePoint().distance(movingOb.getPathLocation());
                        if (totalRadius>=distance){
                            bubbleTile[tileX][tileY].getBubble().remove(bubble);
                        }
                    }   
                    
                    
                }//end y
            }// end x
            
        } // end tile collision for
        
        
    }
   
}
