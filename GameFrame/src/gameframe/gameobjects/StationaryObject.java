/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.util.ArrayList;
import org.dyn4j.dynamics.Body;

/**
 *
 * @author David
 */
public class StationaryObject extends Body{
    
    private ArrayList<BaseShape> shapeList;
    
    public StationaryObject(BaseShape baseShape){
        shapeList = new ArrayList();
        shapeList.add(baseShape);
        
        //convert to dyn4j shape and add to fixture
        
        
        // convert to kpolygon and add to straightedge collision
        
    }
    
    public void addCompoundShape(BaseShape baseShape){
        shapeList.add(baseShape);
        
    }
    
    public ArrayList<BaseShape> getStationaryObjectArray(){
        return shapeList;
    }
    
}
