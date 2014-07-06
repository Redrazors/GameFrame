/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubblehunt.gameobjects;

import bubblehunt.Renderer;
import static bubblehunt.StaticFields.PITCHSIZE;
import static bubblehunt.StaticFields.TILESIZE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import org.dyn4j.dynamics.World;
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
    
    public GameObjects (World world, Dimension screenSize){
        this.world = world;
        this.screenSize = screenSize;
        testObject = new MoveableObject[20];
        moveableObjectsList = new ArrayList();
        stationaryObjectsList = new ArrayList();
        stationaryObstacles = new ArrayList();
        
        bubbleList = new ArrayList();
        tileCount = PITCHSIZE/TILESIZE+1;
        bubbleTile = new BubbleTile[tileCount][tileCount];
        // create bubbles
        initBubbles();
        createBubbleTiles();
        //createBubbleImages();
        
        // init all the objects here
        initTestObjects();
   
    }
    
    private void initBubbles(){
        
        int bubbleCount = 1000;
        
        
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
    
    
    
    private void initTestObjects(){
        
        // add walls
        Rectangle2D.Double rh1 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        Rectangle2D.Double rh2 = new Rectangle2D.Double(-screenSize.width/2,-10, screenSize.width, 20);
        Rectangle2D.Double rv1 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        Rectangle2D.Double rv2 = new Rectangle2D.Double(-10,-screenSize.height/2, 20, screenSize.height);
        
        StationaryObject boundingWalls = new StationaryObject(0, 0, Color.GREEN, 1, stationaryObstacles);
        boundingWalls.addFixture(rh1, 0, -screenSize.height/2);
        boundingWalls.addFixture(rh2, 0, screenSize.height/2);
        boundingWalls.addFixture(rv1, -screenSize.width/2, 0);
        boundingWalls.addFixture(rv2, screenSize.width/2, 0);
        boundingWalls.initObject();
        this.world.addBody(boundingWalls);
        stationaryObjectsList.add(boundingWalls);

        // home obstacle
        Rectangle2D.Double homeRec = new Rectangle2D.Double(-50,-30, 100, 60);
        StationaryObject home = new StationaryObject(0, -50, Color.gray, 1, stationaryObstacles);
        home.addFixture(homeRec, 0, 0);
        home.initObject();
        this.world.addBody(home);
        stationaryObjectsList.add(home);
  
        
        Ellipse2D.Double testBigCirc = new Ellipse2D.Double(-10,-10, 20, 20);
        testObject[0] = new MoveableObject(-200, 0, Color.red, 1, 10); 
        testObject[0].addFixture(testBigCirc, 0, 0);
        Rectangle2D.Double hitRect = new Rectangle2D.Double(-5, -3, 10, 6);
        testObject[0].addFixture(hitRect, 0, 0);
        testObject[0].initObject();
        //add to world and game object list
        this.world.addBody(testObject[0]);
        moveableObjectsList.add(testObject[0]);
        //set test destination
        testObject[0].setPathDestination(new KPoint(210, 270));
        //testObject[0].getLinearVelocity().set(9000.0, 1000.0);
        
        //Ellipse2D.Double testSmallCirc = new Ellipse2D.Double(-15,-15, 30, 30);
        //testObject[1] = new MoveableObject(-200, 0, Color.BLUE, 1,15);
        //testObject[1].addFixture(testSmallCirc, 0, 0);
        //testObject[1].initObject();
        //add to world and game object list
       // this.world.addBody(testObject[1]);
        //moveableObjectsList.add(testObject[1]);
        //testObject[1].setPathDestination(new KPoint(200, -300));
        //set test speed
        //testObject[1].getLinearVelocity().set(-9000.0, 0);
              
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
