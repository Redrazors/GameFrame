/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocketbubble;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author Dave
 */
public class ActionControl {

    
    public ActionControl (JComponent drawPanel){
        
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UpPressed");// false means ! when key released
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UpReleased");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownPressed");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftPressed");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightPressed");
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");
        
        drawPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "SystemExit");
        
        Action upPressed = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                
                                   }
                           }; 
                        Action upReleased = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                 
                                   }
                           };
                        Action downPressed = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                
                                   }
                           }; 
                        Action downReleased = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                
                                   }
                           };
                        
                        Action leftPressed = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                  
                                   }
                           }; 
                        Action leftReleased = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                  
                                   }
                           };
                        
                        Action rightPressed = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                                  
                                   }
                           };
                        Action rightReleased = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                                               
                                   }
                           };
                        
                        Action systemExit = new AbstractAction()
                           {
                                        @Override
                                  public void actionPerformed(ActionEvent e)
                                   {
                                         System.exit(0);
                                   }
                           };
       
                        
        drawPanel.getActionMap().put("UpPressed", upPressed);
        drawPanel.getActionMap().put("UpReleased", upReleased);
        drawPanel.getActionMap().put("DownPressed", downPressed);
        drawPanel.getActionMap().put("DownReleased", downReleased);
        drawPanel.getActionMap().put("LeftPressed", leftPressed);
        drawPanel.getActionMap().put("LeftReleased", leftReleased);
        drawPanel.getActionMap().put("RightPressed", rightPressed);
        drawPanel.getActionMap().put("RightReleased", rightReleased);
        
        drawPanel.getActionMap().put("SystemExit", systemExit);
    }
}
