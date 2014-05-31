/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;


/**
 *
 * @author David
 */
public final class GameObjects {
    private World world;
    private MoveableObject testObject[];
    private ArrayList<MoveableObject> gameObjectsList;
    
    
    
    public GameObjects (World world){
        this.world = world;
        testObject = new MoveableObject[20];
        gameObjectsList = new ArrayList();
        initTestObjects();
        
        
    }
    
    private void initTestObjects(){
        
        // add walls
        Rectangle rh1 = new Rectangle(2,500);
        //Rectangle rh2 = new Rectangle(500,2);
        //Rectangle rv1 = new Rectangle(2,500);
        //Rectangle rv2 = new Rectangle(2,500);
        BodyFixture rh1Fixture = new BodyFixture (rh1);
        //BodyFixture rh2Fixture = new BodyFixture (rh2);
        //BodyFixture rv1Fixture = new BodyFixture (rv1);
        //BodyFixture rv2Fixture = new BodyFixture (rv2);
        //rh1.translate(250, 0);
        //rh2.translate(250, 490);
        //rv1.translate(0, 250);
        //rv2.translate(490, 250);
        Body wall = new Body();
        wall.addFixture(rh1Fixture);
        //wall.addFixture(rh2Fixture);
        //wall.addFixture(rv1Fixture);
        //wall.addFixture(rv2Fixture);
        //wall.setMass(Mass.Type.INFINITE);
        wall.translate(0, 0);
        world.addBody(wall);
        
        

        //Circle hitCircle = new Circle (20);
        //Rectangle hitRect = new Rectangle (80, 80);
        
        
        Ellipse2D.Double hitCircle = new Ellipse2D.Double(-50,-50, 100, 100);
        testObject[0] = new MoveableObject(hitCircle, 100, 100);
        testObject[0].setMass();
        
        Rectangle2D.Double hitRect = new Rectangle2D.Double(-100, -15, 200, 30);
        testObject[0].addFixture(hitRect, 0, -50);
        
        //add to world and game object list
        this.world.addBody(testObject[0]);
        
        gameObjectsList.add(testObject[0]);
        //set test speed
        testObject[0].getLinearVelocity().set(9000.0, 0);
        
        testObject[1] = new MoveableObject(hitCircle, 400, 100);
        testObject[1].setMass();
        //add to world and game object list
        this.world.addBody(testObject[1]);
        gameObjectsList.add(testObject[1]);
        //set test speed
        testObject[1].getLinearVelocity().set(-9000.0, -1000);
        
        
    }
    
    public ArrayList<MoveableObject> getGameObjects(){
        return gameObjectsList;
    }
    
    
    

    

    
   
    
}
