/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.util.ArrayList;
import straightedge.geom.path.PathBlockingObstacle;

/**
 *
 * @author david
 */
public class PathingObstacles {
    
    private ArrayList<PathBlockingObstacle> pathingObstacles;
    
    public PathingObstacles(){
        
        pathingObstacles = new ArrayList();
        
    }
    
    public ArrayList<PathBlockingObstacle> getPathingObstacles(){
        return pathingObstacles;
    }
    
}
