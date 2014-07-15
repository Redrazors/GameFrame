/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble.gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.dyn4j.dynamics.World;
import rocketbubble.MasterClass;
import static rocketbubble.StaticFields.ANGULAR_DAMPING;
import static rocketbubble.StaticFields.LINEAR_DAMPING;
import static rocketbubble.StaticFields.PITCHSIZE;
import static rocketbubble.StaticFields.TILESIZE;
import straightedge.geom.KPoint;
import straightedge.geom.path.PathBlockingObstacle;


/**
 *
 * @author David
 */
public final class GameObjects {
    private World world;
    private MoveableObject testObject[];
    private ArrayList<MoveableObject> moveableObjectsList;
    private ArrayList<StationaryObject> stationaryObjectsList;//list of my stationary object 
    
    private ArrayList<Bubble> bubbleList;
    
    
    private ArrayList<PathBlockingObstacle> stationaryObstacles;
    private Dimension screenSize;
    private BufferedImage bubbleImages[][];
    
    private BubbleTile bubbleTile[][];
    private int tileCount;
    
    private MoveableObject heroRocket;
    private MasterClass masterClass;
    
    public GameObjects (World world, Dimension screenSize, MasterClass masterClass){
        this.world = world;
        this.screenSize = screenSize;
        this.masterClass = masterClass;
        testObject = new MoveableObject[20];
        
        
        moveableObjectsList = new ArrayList<>();
        stationaryObjectsList = new ArrayList<>();
        stationaryObstacles = new ArrayList<>();
        
        bubbleList = new ArrayList<>();
        tileCount = PITCHSIZE/TILESIZE+1;
        bubbleTile = new BubbleTile[tileCount][tileCount];
        // create bubbles
        initBubbles();
        createBubbleTiles();

        
        // init hero
        initHero();
        
        // init map, or platform until then
        initMap();

   
    }
    
    private void initBubbles(){
        
        int bubbleCount = 0;
        
        
        for (int i=0; i< bubbleCount; i++){
            int radius = 10+(int)(Math.random()*5);
            // coords for -1000,-1000 to 1000,1000
            KPoint bubblePoint = new KPoint((int)Math.round(Math.random()*PITCHSIZE-PITCHSIZE/2), 
                    (int)Math.round(Math.random()*PITCHSIZE-PITCHSIZE/2));

            boolean generateNew = true;
            
            for (Bubble bubbleList1 : bubbleList) {
                double distance = bubblePoint.distance(bubbleList1.getBubblePoint());
                //
                //System.out.println(bubbleList.size() + " i= " + i);
                // if the combined radius is less than the distance
                if (radius + bubbleList1.getRadius()+3 > distance) {
                    generateNew = false;
                    break;
                } 
            }
       
            
            if (generateNew){
                // init bubble and add to list
                Bubble bubble = new Bubble(bubblePoint, radius);
                bubbleList.add(bubble);
            } else {
                // inrease the bubblecount by 1 so a new coord is created
                bubbleCount+=1;
                //System.out.println(bubbleCount);
            }
            
        }  
        
    }
    
    private void createBubbleTiles(){
        BubbleRenderer bubbleRenderer = new BubbleRenderer();
        
        // init bubble tiles
        for (int i=0; i<tileCount; i++){
            for (int j=0; j<tileCount; j++){
                // 15 buffer on each side for radius on drawn circle
                KPoint topLeft = new KPoint(-PITCHSIZE/2+i*TILESIZE, -PITCHSIZE/2+j*TILESIZE);
                bubbleTile[i][j] = new BubbleTile(topLeft, bubbleRenderer);
            }
        }
        
        // add the bubble to the correct tile
        for (Bubble bubble: bubbleList){
            int x = (int)bubble.getBubblePoint().x;
            int y= (int)bubble.getBubblePoint().y;
            int bubbleTileX = (int)Math.floor((x+PITCHSIZE/2)/TILESIZE);
            int bubbleTileY = (int)Math.floor((y+PITCHSIZE/2)/TILESIZE);
            
            bubbleTile[bubbleTileX][bubbleTileY].addBubble(bubble);
        }
        
        //init all the bubble tile images
        for (int i=0; i<tileCount; i++){
            for (int j=0; j<tileCount; j++){
                bubbleTile[i][j].initTileImage();
            }
        }
    }
    
    public BubbleTile[][] getBubbleTile(){
        return bubbleTile;
    }
    
    public int getTileCount(){
        return tileCount;
    }
    
    private void initHero(){
        Ellipse2D.Double heroCirc = new Ellipse2D.Double(-10,-10, 20, 20);
        KPoint start= masterClass.getLevelControl().getGameLevels().get(masterClass.getLevelControl().getCurrentLevel()).getStartPoint();
        heroRocket = new MoveableObject((int)start.x, (int)start.y, Color.red, 1, 10); 
        heroRocket.addFixture(heroCirc, 0, 0);
        Rectangle2D.Double hitRect = new Rectangle2D.Double(-10, -3, 20, 6);
        heroRocket.addFixture(hitRect, 15, 0);
        heroRocket.initObject();
        
        //add to world and game object list
        this.world.addBody(heroRocket);
        heroRocket.setAngularDamping(1);
        heroRocket.setLinearDamping(2);
        moveableObjectsList.add(heroRocket);
        
        // rotate to point up
        heroRocket.rotateAboutCenter(-Math.PI/2);
 
    }
    
    public MoveableObject getHero(){
        return heroRocket;
    }
    
    private void initMap(){
        // add walls
        //Rectangle2D.Double rh1 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        //Rectangle2D.Double rh2 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        //Rectangle2D.Double rv1 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        //Rectangle2D.Double rv2 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        
        //StationaryObject boundingWalls = new StationaryObject(0, 0, Color.GREEN, 1, stationaryObstacles);
        //boundingWalls.addFixture(rh1, 0, -screenSize.height/2);
        //boundingWalls.addFixture(rh2, 0, screenSize.height/2);
        //boundingWalls.addFixture(rv1, -screenSize.width/2, 0);
        //boundingWalls.addFixture(rv2, screenSize.width/2, 0);
        //boundingWalls.initObject();
        //this.world.addBody(boundingWalls);
        //stationaryObjectsList.add(boundingWalls);
        
        // home obstacle
        Rectangle2D.Double homeRec = new Rectangle2D.Double(-50,-5, 100, 10);
        StationaryObject home = new StationaryObject(0, 300, Color.gray, 1, stationaryObstacles);
        home.addFixture(homeRec, 0, 0);
        home.initObject();
        this.world.addBody(home);
        stationaryObjectsList.add(home);
        
        
    }
    
    
    public ArrayList<MoveableObject> getMoveableObjectsList(){
        return moveableObjectsList;
    }
    
    public ArrayList<StationaryObject> getStationaryObjectsList(){
        return stationaryObjectsList;
    }
    
    public ArrayList<PathBlockingObstacle> getStationaryObstacles(){
        return stationaryObstacles;
    }
    
    public ArrayList<Bubble> getBubbleList(){
        return bubbleList;
    }
    
    
    

    

    
   
    
}
