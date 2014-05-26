/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class MasterClass {
    
    private Renderer renderer;
    
    public MasterClass (JFrame gameFrame, BufferStrategy bs){
        
        renderer = new Renderer(bs);
        gameFrame.add(renderer);
        renderer.rendererStart();
        
        
    }
    
}
