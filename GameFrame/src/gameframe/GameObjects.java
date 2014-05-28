/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.Shape;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

/**
 *
 * @author David
 */
public class GameObjects {
    private World world;
    private MoveableObject testObject[];
    private ArrayList<MoveableObject> gameObjectsList;
    
    public GameObjects (World world){
        this.world = world;
        testObject = new MoveableObject[8];
        gameObjectsList = new ArrayList();
        initTestObjects();
    }
    
    private void initTestObjects(){
        
        // add walls
        Rectangle rh1 = new Rectangle(500,2);
        Rectangle rh2 = new Rectangle(500,2);
        Rectangle rv1 = new Rectangle(2,500);
        Rectangle rv2 = new Rectangle(2,500);
        BodyFixture rh1Fixture = new BodyFixture (rh1);
        BodyFixture rh2Fixture = new BodyFixture (rh2);
        BodyFixture rv1Fixture = new BodyFixture (rv1);
        BodyFixture rv2Fixture = new BodyFixture (rv2);
        rh1.translate(250, 0);
        rh2.translate(250, 490);
        rv1.translate(0, 250);
        rv2.translate(490, 250);
        Body wall = new Body();
        wall.addFixture(rh1Fixture);
        wall.addFixture(rh2Fixture);
        wall.addFixture(rv1Fixture);
        wall.addFixture(rv2Fixture);
        wall.setMass(Mass.Type.INFINITE);
        wall.translate(0, 0);
        world.addBody(wall);
        
        

        Circle hitCircle = new Circle (40);
        Rectangle hitRect = new Rectangle (80, 80);
        
        for (int i =0; i<8; i++){
            testObject[i] = new MoveableObject(hitCircle);
            //testObject[i].addFixture(hitCircle);
            testObject[i].setMass();
            world.addBody(testObject[i]);
            
            int xPos = (int)(Math.random()*500);
            int yPos = (int)(Math.random()*500);
            int directionX = (int)(Math.round(Math.random())*2-1);
            int directionY = (int)(Math.round(Math.random())*2-1);
            
            testObject[i].translate(xPos, yPos); // these are on screen coords 
            testObject[i].getLinearVelocity().set(900.0*directionX, 900.0*directionY);
            
            
            gameObjectsList.add(testObject[i]);
        }
        
    }
    
    public ArrayList<MoveableObject> getGameObjects(){
        return gameObjectsList;
    }
    
   
    
}
