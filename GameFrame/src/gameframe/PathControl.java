/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import gameframe.gameobjects.GameObjects;
import java.util.ArrayList;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathFinder;

import static gameframe.StaticFields.CLIP_CLEARANCE;

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
        
        // change all points to avoid clipping corners
        for(int i =0; i<pathData.points.size(); i++){
            KPoint adjustedPoint = avoidClippingCorners(pathData.points.get(i));
            pathData.points.get(i).setCoords(adjustedPoint);
        }
        
        return pathData.points;
    }
    
    public KPoint avoidClippingCorners(KPoint nextPoint){
        KPoint unClipped = nextPoint;
        //System.out.println(nextPoint);
        // nextPoint is a map Coord
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
}
