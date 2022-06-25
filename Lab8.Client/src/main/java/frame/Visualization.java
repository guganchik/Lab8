/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.TreeSet;

import collections.Vehicle;


/**
 *
 * @author fetus
 */
public class Visualization extends Canvas {  

    private float minX;
    private float maxX;
    
    private float minY;
    private float maxY;
    
    
    public void paint(Graphics g) {  
        g.drawString("Hello",40,40);  
        setBackground(Color.WHITE);  
        g.fillRect(130, 30,100, 80);  
        g.drawOval(30,130,50, 60);  
        setForeground(Color.RED);  
        g.fillOval(130,130,50, 60);  
        g.drawArc(30, 200, 40,50,90,60);  
        g.fillArc(30, 130, 40,50,180,40);  
          
    }  
    
    public void setCollection(TreeSet<Vehicle> collection) {
        boolean first = true;
        
        /**
        int maxLength = Collections.max(WindowManager.movies, Comparator.comparingInt(Movie::getLength)).getLength();
        int minLength = Collections.min(WindowManager.movies, Comparator.comparingInt(Movie::getLength)).getLength();

        double maxX = Collections.max(WindowManager.movies, (first, second) -> (int) (first.getCoordinates().getX() - second.getCoordinates().getX())).getCoordinates().getX();
        double minX = Collections.min(WindowManager.movies, (first, second) -> (int) (first.getCoordinates().getX() - second.getCoordinates().getX())).getCoordinates().getX();

        int maxY = Collections.max(WindowManager.movies, Comparator.comparingInt(movie -> movie.getCoordinates().getY())).getCoordinates().getY();
        int minY = Collections.min(WindowManager.movies, Comparator.comparingInt(movie -> movie.getCoordinates().getY())).getCoordinates().getY();        
        */
        
        for(Vehicle v: collection) {
            if (first) {
                minX = v.getCoordinates().getX();
                maxX = v.getCoordinates().getX();
                minY = v.getCoordinates().getY();
                maxY = v.getCoordinates().getY();
                first = false;
            } else {
                if (v.getCoordinates().getX() < minX) {
                    minX = v.getCoordinates().getX();
                }
                if (v.getCoordinates().getX() > maxX) {
                    maxX = v.getCoordinates().getX();
                }
                if (v.getCoordinates().getY() < minY) {
                    minY = v.getCoordinates().getY();
                }
                if (v.getCoordinates().getY() > maxY) {
                    maxY = v.getCoordinates().getY();
                }
            }
        }
        
        
    }
}  