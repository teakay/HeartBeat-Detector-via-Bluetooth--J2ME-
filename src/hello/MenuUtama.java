/*
 * Menu Utama
 * Untuk memilih koneksi, bantuan dan tentang
 */
package hello;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/*-----------------------Kelas Midlet----------------------------------*/
public class MenuUtama extends MIDlet{
    
    private Display display;
    SplashScreen splash;

    public MenuUtama(){
         display = Display.getDisplay(this);
    }
    
    public void startApp() {
      splash = new SplashScreen(this,display);  
      display.setCurrent(splash);
   
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }

    public void show(){
       Menu menu = new Menu(this,display);
        display.setCurrent(menu);
        
    }
    
}
/*-------------------------------Tampilan pilihan menu koneksi, tentang, bantuan-------------------*/
class Menu extends Canvas implements CommandListener{

    private Image bluetooth, help, about;
    private int counter=0;
    MenuUtama midlet;
    Display display;
    private Command exit,pilih;
/*-----------------Konstruktor-Inisialisasi objek yang akan digunakan-------------*/
    public Menu(MenuUtama midlet, Display display){
        this.midlet = midlet;
        this.display = display;
        exit = new Command("Keluar",Command.EXIT,0);
        pilih = new Command("Pilih",Command.SCREEN,0);
        try {

            bluetooth = Image.createImage("/images/bluetooth.PNG");
            help = Image.createImage("/images/help.PNG");
            about = Image.createImage("/images/about.PNG");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.addCommand(exit);
        this.addCommand(pilih);
        this.setCommandListener(this);
    }
    
    /*-----------Menggambar icon dan membuat tulisan koneksi,bantuan, tentang------------------------------*/
    protected void paint(Graphics g) {
        //setting warna background layar
        g.setColor(128,128,255);
        g.fillRect(0, 0, getWidth(), getHeight());
        //set warna untuk pilihan
        g.setColor(255,255,255);
         for(int i=0;i<3;i++){
            if(i == counter)
            g.fillRect(20, 10+(30*i), 80, 20);
        }
        //set gambar dan warna tulisan untuk menu
        g.drawImage(bluetooth, 10, 10, Graphics.TOP | Graphics.LEFT);
        //g.setColor(255, 255, 255);
        g.setColor(0,0,0);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
        g.drawString("Koneksi", 40, 12, Graphics.TOP | Graphics.LEFT);
        g.drawImage(help, 10, 40, Graphics.TOP | Graphics.LEFT);
        g.setColor(0,0,0);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
        g.drawString("Bantuan", 40, 42, Graphics.TOP | Graphics.LEFT);
        g.drawImage(about, 10, 70, Graphics.TOP | Graphics.LEFT);
        g.setColor(0,0,0);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
        g.drawString("Tentang", 40, 72, Graphics.TOP | Graphics.LEFT);
       
    }
    //action key,untuk merubah posisi pilihan
    public void keyPressed(int keyCode){
        int gameaction = getGameAction(keyCode);
        switch(gameaction){
            case Canvas.DOWN:
                if(counter >= 2)
                 counter = 0;   
                else
                    counter++;
                    
                System.out.println("right,counter"+counter);
                break;
            case Canvas.UP:
                if (counter<=0)
                counter = 2;
                else
                counter--;
                System.out.println("left,counter"+counter);
                break;

        }
        repaint();
    }
    
    //mengarahkan ke display selanjutnya sesuai menu yang dipilih
    public void selectedMenu(int counter){
        switch(counter){
            case 0:
                 SearchDevice search = new SearchDevice(midlet,display);
            display.setCurrent(search);
                break;
            case 1:
                //bantuan
                break;
            case 2:
                //tentang
                break;
                
        }
    }

//tombol pilihan dan keluar
    public void commandAction(Command c, Displayable d) {
        if (c == exit){
            midlet.destroyApp(true);
        }
        if(c == pilih){
            selectedMenu(counter);
        }
    }
    
}