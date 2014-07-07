/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocketbubble;

import rocketbubble.gameobjects.Bubble;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 *
 * @author David
 */
public class SoundControl {
    
    private boolean playingPop = false;
    private Sound pop[];
    private Music popM[];
    private Music song1;
    
    public SoundControl(){
        pop = new Sound[2];
        popM = new Music[2];
        song1 = TinySound.loadMusic("music/music1.wav");
        pop[0] = TinySound.loadSound("music/pop1.wav");
        pop[1] = TinySound.loadSound("music/pop2.wav");
        popM[0] = TinySound.loadMusic("music/pop1.wav");
        popM[1] = TinySound.loadMusic("music/pop2.wav");
    }
    
    public void playPop(Bubble bubble){  
        int ran = (int)Math.round(Math.random());
        pop[ran].play();
            
    }
}
