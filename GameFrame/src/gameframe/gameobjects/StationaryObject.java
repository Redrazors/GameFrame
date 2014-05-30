/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class StationaryObject {
    private ArrayList<BaseShape> shapeList;
    
    public StationaryObject(BaseShape baseShape){
        shapeList = new ArrayList();
        shapeList.add(baseShape);
        
    }
    
    public void addCompoundShape(BaseShape baseShape){
        shapeList.add(baseShape);
        
    }
    
    public ArrayList<BaseShape> getStationaryObjectArray(){
        return shapeList;
    }
    
}
