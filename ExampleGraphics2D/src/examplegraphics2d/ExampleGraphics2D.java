/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package examplegraphics2d;
/*
 * Copyright (c) 2010-2013 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

/**
 * Class used to show a simple example of using the dyn4j project using
 * Java2D for rendering.
 * <p>
 * This class can be used as a starting point for projects.
 * @author William Bittle
 * @version 3.1.5
 * @since 3.0.0
 */
public class ExampleGraphics2D extends JFrame {
        /** The serial version id */
        private static final long serialVersionUID = 5663760293144882635L;
        
        /** The scale 45 pixels per meter */
        public static final double SCALE = 1.0;
        
        /** The conversion factor from nano to base */
        public static final double NANO_TO_BASE = 1.0e9;
        
        private GameObject capsule, capsule2;
        /**
         * Custom Body class to add drawing functionality.
         * @author William Bittle
         * @version 3.0.2
         * @since 3.0.0
         */
        public static class GameObject extends Body {
                /** The color of the object */
                protected Color color;
                
                /**
                 * Default constructor.
                 */
                public GameObject() {
                        // randomly generate the color
                        this.color = new Color(
                                        (float)Math.random() * 0.5f + 0.5f,
                                        (float)Math.random() * 0.5f + 0.5f,
                                        (float)Math.random() * 0.5f + 0.5f);
                }
                
                /**
                 * Draws the body.
                 * <p>
                 * Only coded for polygons and circles.
                 * @param g the graphics object to render to
                 */
                public void render(Graphics2D g) {
                        // save the original transform
                        AffineTransform ot = g.getTransform();
                        
                        // transform the coordinate system from world coordinates to local coordinates
                        AffineTransform lt = new AffineTransform();
                        lt.translate(this.transform.getTranslationX() * SCALE, this.transform.getTranslationY() * SCALE);
                        lt.rotate(this.transform.getRotation());
                        
                        // apply the transform
                        g.transform(lt);
                        
                        // loop over all the body fixtures for this body
                        for (BodyFixture fixture : this.fixtures) {
                                // get the shape on the fixture
                                Convex convex = fixture.getShape();
                                Graphics2DRenderer.render(g, convex, SCALE, color);
                        }
                        
                        // set the original transform
                        g.setTransform(ot);
                }
        }
        
        /** The canvas to draw to */
        protected Canvas canvas;
        
        /** The dynamics engine */
        protected World world;
        
        /** Wether the example is stopped or not */
        protected boolean stopped;
        
        /** The time stamp for the last iteration */
        protected long last;
        
        /**
         * Default constructor for the window
         */
        public ExampleGraphics2D() {
                super("Graphics2D Example");
                // setup the JFrame
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                // add a window listener
                this.addWindowListener(new WindowAdapter() {
                        /* (non-Javadoc)
                         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
                         */
                        @Override
                        public void windowClosing(WindowEvent e) {
                                // before we stop the JVM stop the example
                                stop();
                                super.windowClosing(e);
                        }
                });
                
                // create the size of the window
                Dimension size = new Dimension(800, 600);
                
                // create a canvas to paint to 
                this.canvas = new Canvas();
                this.canvas.setPreferredSize(size);
                this.canvas.setMinimumSize(size);
                this.canvas.setMaximumSize(size);
                
                // add the canvas to the JFrame
                this.add(this.canvas);
                
                // make the JFrame not resizable
                // (this way I dont have to worry about resize events)
                this.setResizable(false);
                
                // size everything
                this.pack();
                
                // make sure we are not stopped
                this.stopped = false;
                
                // setup the world
                this.initializeWorld();
        }
        
        /**
         * Creates game objects and adds them to the world.
         * <p>
         * Basically the same shapes from the Shapes test in
         * the TestBed.
         */
        protected void initializeWorld() {
                // create the world
                this.world = new World();
                world.setGravity(new Vector2(0,0));
                
                // create all your bodies/joints
                
                // create the floor
                Rectangle floorRect = new Rectangle(200.0, 10.0);
                GameObject floor = new GameObject();
                floor.addFixture(new BodyFixture(floorRect));
                floor.setMass(Mass.Type.INFINITE);
                // move the floor down a bit
                floor.translate(0.0, -200.0);
                this.world.addBody(floor);
                
                
                
                // try a compound object
                Circle c1 = new Circle(50);
                BodyFixture c1Fixture = new BodyFixture(c1);
                c1Fixture.setDensity(0.5);
                Rectangle rm = new Rectangle(100.0, 30.0);
                // translate the circles in local coordinates
                rm.translate(0.0, -50.0);
                capsule = new GameObject();
                capsule.addFixture(c1Fixture);
                capsule.addFixture(rm);
                capsule.setMass();
                capsule.translate(-100.0, 0.0);
                this.world.addBody(capsule);
                
                capsule2 = new GameObject();
                capsule2.addFixture(c1Fixture);
                capsule2.setMass();
                capsule2.translate(100.0, 0.0);
                this.world.addBody(capsule2);
                
                
        }
        
        /**
         * Start active rendering the example.
         * <p>
         * This should be called after the JFrame has been shown.
         */
        public void start() {
                // initialize the last update time
                this.last = System.nanoTime();
                // don't allow AWT to paint the canvas since we are
                this.canvas.setIgnoreRepaint(true);
                // enable double buffering (the JFrame has to be
                // visible before this can be done)
                this.canvas.createBufferStrategy(2);
                // run a separate thread to do active rendering
                // because we don't want to do it on the EDT
                Thread thread = new Thread() {
                        public void run() {
                                // perform an infinite loop stopped
                                // render as fast as possible
                                while (!isStopped()) {
                                        gameLoop();
                                        // you could add a Thread.yield(); or
                                        // Thread.sleep(long) here to give the
                                        // CPU some breathing room
                                }
                        }
                };
                // set the game loop thread to a daemon thread so that
                // it cannot stop the JVM from exiting
                thread.setDaemon(true);
                // start the game loop
                thread.start();
        }
        
        /**
         * The method calling the necessary methods to update
         * the game, graphics, and poll for input.
         */
        protected void gameLoop() {
                // get the graphics object to render to
                Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();
                
                // before we render everything im going to flip the y axis and move the
                // origin to the center (instead of it being in the top left corner)
                AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
                AffineTransform move = AffineTransform.getTranslateInstance(400, -300);
                g.transform(yFlip);
                g.transform(move);
                
                // now (0, 0) is in the center of the screen with the positive x axis
                // pointing right and the positive y axis pointing up
                
                // render anything about the Example (will render the World objects)
                this.render(g);
                
                // dispose of the graphics object
                g.dispose();
                
                // blit/flip the buffer
                BufferStrategy strategy = this.canvas.getBufferStrategy();
                if (!strategy.contentsLost()) {
                        strategy.show();
                }
                
                // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
        
        // update the World
        capsule.applyImpulse(new Vector2(1000, 1));
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = (double)diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.world.update(elapsedTime);
        }

        /**
         * Renders the example.
         * @param g the graphics object to render to
         */
        protected void render(Graphics2D g) {
                // lets draw over everything with a white background
                g.setColor(Color.WHITE);
                g.fillRect(-400, -300, 800, 600);
                
                // lets move the view up some
                g.translate(0.0, -1.0 * SCALE);
                
                // draw all the objects in the world
                for (int i = 0; i < this.world.getBodyCount(); i++) {
                        // get the object
                        GameObject go = (GameObject) this.world.getBody(i);
                        // draw the object
                        go.render(g);
                }
        }
        
        /**
         * Stops the example.
         */
        public synchronized void stop() {
                this.stopped = true;
        }
        
        /**
         * Returns true if the example is stopped.
         * @return boolean true if stopped
         */
        public synchronized boolean isStopped() {
                return this.stopped;
        }
        
        /**
         * Entry point for the example application.
         * @param args command line arguments
         */
        public static void main(String[] args) {
                // set the look and feel to the system look and feel
                try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                } catch (InstantiationException e) {
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                }
                
                // create the example JFrame
                ExampleGraphics2D window = new ExampleGraphics2D();
                
                // show it
                window.setVisible(true);
                
                // start it
                window.start();
        }
}
