/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.levels;

import java.util.ArrayList;
import rocketbubble.MasterClass;
import straightedge.geom.KPoint;

/**
 *
 * @author david
 */
public class LevelControl {
    private MasterClass masterClass;
    
    private ArrayList<GameLevel> levelList;
    private int currentLevel=0;
    
    public LevelControl(MasterClass masterClass){
        this.masterClass = masterClass;
        levelList = new ArrayList();
        
        addLevels();
    }
    
    private void addLevels(){
        GameLevel testLevel = new GameLevel(new KPoint(0, 280));
        levelList.add(testLevel);
    }
    
    public ArrayList<GameLevel> getGameLevels(){
        return levelList;
    }
    public int getCurrentLevel(){
        return currentLevel;
    }
    
}
