/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gameobjects;

import java.awt.Shape;

/**
 *
 * @author David
 */
public class BaseShape {
    
    private Shape shape;
    private int translationX, translationY;
    
    public BaseShape(Shape shape, int translationX, int translationY){
        this.shape = shape;
        this.translationX = translationX;
        this.translationY = translationY;
    }
    
    public int getTranslationX(){
        return translationX;
    }
    
    public int getTranslationY(){
        return translationY;
    }
    
    public Shape getBaseShape(){
        return shape;
    }
    
}
