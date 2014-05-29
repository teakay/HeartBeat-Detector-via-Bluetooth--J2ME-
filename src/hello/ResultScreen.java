/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import javax.microedition.lcdui.*;
import java.lang.String;
/**
 *
 * @author user
 */
public class ResultScreen extends Canvas implements CommandListener{

    private MenuUtama midlet;
    private Display display;
    private String result,nama,gender,status;
    private Command exit,main;
    private int usia,bpm;
    private int data[] ;

    public ResultScreen(MenuUtama midlet, Display display, String nama, String umur, int gender,String result){
        this.midlet = midlet;
        this.display = display;
        data = new int[50];
        main = new Command("Menu",Command.SCREEN,0);
        exit = new Command("Keluar",Command.EXIT,0);
        addCommand(main);
        addCommand(exit);
        setCommandListener(this);
        this.nama = nama;
        this.usia = Integer.parseInt(umur);
        this.gender = (gender == 0)?"Pria":"Wanita";
        this.result = result;
        //hitung detak melalui fungsi count
        bpm = count(result);
        //cek status berdasarkan jumlah detak melalui fungsi status_check
        status = status_check(bpm,usia,gender);
//        data = dataGrafik(result);
    }
    protected void paint(Graphics g) {
//        g.setColor(0,0,255);
        g.setColor(128,128,255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(255, 255, 255);
        g.drawString("Nama : "+nama, 20, 20, Graphics.TOP|Graphics.LEFT);
        g.drawString("Umur : "+usia, 20, 40, Graphics.TOP|Graphics.LEFT);
        g.drawString("Jenis kelamin : "+gender, 20, 60, Graphics.TOP|Graphics.LEFT);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString(bpm+" bpm", 20, 80, Graphics.TOP|Graphics.LEFT);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
        g.drawString("Status : "+status, 20, 100, Graphics.TOP|Graphics.LEFT);
//        for(int i=0;i<1;i++){
//            g.drawLine(20, 120 + (130 - data[i]), 50, 120 +( 130 -data[i+1]));
//        }
    }

    public void commandAction(Command c, Displayable d) {
        if(c == exit){
           
            midlet.destroyApp(true);
        }
        if(c == main){
            
            midlet.show();
        }    
    }
    
    //Fungsi dan algoritma untuk menghitung detak 
     public int count(String result){
       //System.out.println("counting result ... ");
        int i = result.indexOf("/");
        int n = 0,x=0,y=0,j=0;
        int count=0,copy;
        String sub;
        while(i >= 0){
           //System.out.println("loop");
              sub = result.substring(n, i);
            
            if(sub.startsWith("/-")){
               // System.out.println("ada -");
                 if(!(result.substring(x,y)).startsWith("/-")){
                     System.out.println("seblmnya positif");
                     count++;
                  
                }
                 else{
                     
                 }
            }
                x = n;
                y = i;      
                n = i;
                i = result.indexOf("/",i+1);    
//                 copy= Integer.parseInt(result.substring(x,y));
//                data[j] = copy;
//                j++;
                //data += result.substring(x,y);
                     
        }
         count *= 4;
         return count;
    }
    public String status_check(int count, int umur, int gender){
        String stat="";
       // System.out.println("count"+count+"umur"+umur+"gender"+gender);
        //Laki-laki
        if(gender == 0){
            if((umur >= 20) && (umur <=29)){
                if(count < 60)
                    stat =  "Sangat Baik";
                else if((count>=60)&&(count<=69))
                    stat = "Baik";
                else if((count >= 70)&&(count<=75))
                    stat = "Cukup";
                else if(count > 85)
                    stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
            else if((umur >= 30) && (umur <=39)){
                if(count < 64)
                    stat = "Sangat Baik";
                else if((count>=65)&&(count<=71))
                    stat = "Baik";
                else if((count >= 72)&&(count<=87))
                    stat = "Cukup";
                else if(count > 87)
                    stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
            else if((umur >= 40) && (umur <=49)){
                if(count < 66)
                    stat = "Sangat Baik";
                else if((count>=66)&&(count<=73))
                    stat = "Baik";
                else if((count >= 74)&&(count<=89))
                    stat = "Cukup";
                else if(count > 89)
                     stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
            else if(umur > 50){
                if(count < 68)
                    stat = "Sangat Baik";
                else if((count>=68)&&(count<=75))
                    stat = "Baik";
                else if((count >= 79)&&(count<=91))
                    stat = "Cukup";
                else if(count > 91)
                    stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
        }
        //Perempuan
        else if(gender == 1){
             if((umur >= 20) && (umur <=29)){
                if(count < 70)
                    stat = "Sangat Baik";
                else if((count>=70)&&(count<=77))
                    stat = "Baik";
                else if((count >= 78)&&(count<=94))
                    stat = "Cukup";
                else if(count > 94)
                    stat ="Kurang";
                else
                    stat="Data tidak ada";
            }
            else if((umur >= 30) && (umur <=39)){
                if(count < 72)
                    stat = "Sangat Baik";
                else if((count>=72)&&(count<=79))
                    stat = "Baik";
                else if((count >= 80)&&(count<=96))
                    stat = "Cukup";
                else if(count > 96)
                    stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
            else if((umur >= 40) && (umur <=49)){
                if(count < 74)
                    stat = "Sangat Baik";
                else if((count>=74)&&(count<=81))
                    stat = "Baik";
                else if((count >= 82)&&(count<=98))
                    stat = "Cukup";
                else if(count > 98)
                     stat = "Kurang";
                else
                    stat="Data tidak ada";
                
            }
            else if(umur > 50){
                if(count < 76)
                    stat = "Sangat Baik";
                else if((count>=76)&&(count<=83))
                    stat = "Baik";
                else if((count >= 84)&&(count<=100))
                    stat = "Cukup";
                else if(count > 100)
                    stat = "Kurang";
                else
                    stat="Data tidak ada";
            }
        }
        else {
        stat = "Data tidak ada";
        }
        return stat;
    }
    public int[] dataGrafik(String result){
       
        //String str = "123\n124\n125\n126\nlowhigh\nlowhigh\n123\n124\n125\n";
        String str = result;
        int i = str.indexOf("lowhigh\n");
        int n = 0,j=0;
        int data[] = new int[20];
        
        while(i >= 0){
            
            //System.out.println(i);
            String sub = str.substring(n, i);
//            System.out.println("sub"+sub);
            if(sub.equals("lowhigh\n")){
                //System.out.println("----");
               j--;
            }
            else{
                data[j] = Integer.parseInt(sub);
            }
                
                j++;
         
            n = i+1;
            
            i = str.indexOf("lowhigh\n",i+1);
            }
        return data;
    }
}
