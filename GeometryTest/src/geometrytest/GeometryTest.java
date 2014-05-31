/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geometrytest;

import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class GeometryTest extends JFrame{
    
    public GeometryTest(){
        setTitle("Geometry Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setResizable(false);
        setVisible(true);
        Board board = new Board();
        add(board);
        board.boardInit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GeometryTest geometryTest = new GeometryTest();
    }
    
}
