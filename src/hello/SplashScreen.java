/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

class SplashScreen extends Canvas implements Runnable{

    private Image image;
    private boolean run = true;
    private int counter=0;
    Thread t;
    MenuUtama menu ;
    Display display;
   
    public SplashScreen(MenuUtama midlet, Display display){
        this.menu = midlet;
        this.display = display;
       
        t = new Thread(this);
        t.start();
        try {
           //image = Image.createImage("/images/logodetakjantung.png");
           image = Image.createImage("/images/logo2.PNG");
             } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    protected void paint(Graphics g) {
        g.setColor(128,128,255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image,20,10, Graphics.TOP|Graphics.LEFT);
        
        g.setColor(255,255,255);
        g.fillRect(20, 90, 5+counter, 10);
    }

    public void run() {
        while(run){
//            if(counter <= 180){
            if(counter <= 80){
                counter += 5;
                try {
                    Thread.sleep(80);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                repaint();
            }
            else{
                this.run = false;
                menu.show();
            }
        }
    }

   

       
}
