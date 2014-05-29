/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.util.Timer;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.bluetooth.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

class SearchDevice extends Canvas implements Runnable,CommandListener,DiscoveryListener{

    private Display display;
    private MenuUtama midlet;
    private Form form,formInput;
    private Command select,exit,back,measure,result;
    private TextField nama,umur;
    private List btDevice;
    RemoteDevice devices[] = new RemoteDevice[255];
    private LocalDevice local;
    private DiscoveryAgent agent;
    private DataOutputStream dout;
    private ChoiceGroup gender;
    private StreamConnectionNotifier server = null;
    private StreamConnection conn = null;
    private String msg,URL,dataresult;
    private Timer timer;
    private SearchDevice.TestTimerTask2 task;
    int currentDevice = 0;
    private static int count=0;
    private static boolean time = true;
    private boolean run = true;
    private int counter = 0;
    Thread t;
    private static int SERVICE_NAME_ATTRID = 0x1101;
    private int num_discovered;
  
    /*---------------Konstruktor-Inisialisasi objek yang akan digunakan----------*/
    public SearchDevice(MenuUtama midlet, Display display){
        this.display = display;
        this.midlet = midlet;
        
        t = new Thread(this);
        t.start();
        
        timer = new Timer();
        task = new SearchDevice.TestTimerTask2();
        
        select = new Command("Pilih",Command.OK,0);
        back = new Command("Kembali",Command.BACK,0);
        exit = new Command("Exit",Command.EXIT,0);
        result = new Command("Hasil",Command.SCREEN,0);
        formInput = new Form("Form Input");
        nama = new TextField("Nama","",50,TextField.ANY);
        umur = new TextField("Umur","",50,TextField.ANY);
        measure = new Command("Ukur",Command.SCREEN,0);
        gender = new ChoiceGroup("Jenis Kelamin",Choice.EXCLUSIVE);
        btDevice = new List("Pilih Device",Choice.IMPLICIT);
               
        addCommand(select);     
        addCommand(exit);
        setCommandListener(this);
        
        findDevice();
    }
      /*----------------------------- Running Searching Screen ----------------------------------------------*/
    public void run() {
        while(run){
            
           this.counter++;
           if(counter > 4){
               this.counter = 1;
           }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("interrupt"+ex.getMessage());
            }
             repaint();
        }
    }

/*----------------------------Aksi ketika tombol dipilih----------------------------------*/
    public void commandAction(Command c, Displayable d) {
        if(c == select){
            this.run = false;
            //find service
            findService(btDevice.getSelectedIndex());
            displayInputForm();
        }
        if(c == back){
            
                time = false;
                this.run = false;
                midlet.show();
           
        }
        if(c == exit){
            try {
                time = false;
                conn.close();
                this.run = false;
                midlet.destroyApp(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(c == measure){
           System.out.println("measure command");
             timer.schedule(task, 15000);
            try{
            int usia = Integer.parseInt(umur.getString());
            getData();
           
            }catch(NumberFormatException ex){
                Alert alert = new Alert("Error");
                alert.setString("Masukkan umur dengan angka");
                alert.setTimeout(4000);
                display.setCurrent(alert);
            }
           
        }
        if( c == result){
            ResultScreen result = new ResultScreen(midlet,display,nama.getString(),umur.getString(),gender.getSelectedIndex(),dataresult);
            display.setCurrent(result);
        }
    }
     /*-----------------------------cari device bluetooth yang tersedia---------------------------------*/
    public void findDevice(){
        try {
            local = LocalDevice.getLocalDevice();
            agent = local.getDiscoveryAgent();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            agent.startInquiry(DiscoveryAgent.GIAC, this);
        } catch (BluetoothStateException ex) {
            System.out.println("find device"+ex.getMessage());
        }
    }
    /*--------------------------------cari service------------------------------------------*/
     private void findService(int devicenumber){
        UUID uuids[] = new UUID[1];
        //uuid - serial port
        uuids[0] = new UUID(0x1101);
        int attridset[] = new int[1];
        attridset[0]=SERVICE_NAME_ATTRID;
        RemoteDevice rd = this.devices[devicenumber];
        try {
            agent.searchServices(attridset, uuids, rd, this);
        } catch (BluetoothStateException ex) {
                //ex.printStackTrace();
            System.out.println("Service search"+ex.getMessage());
        }
        synchronized(this){
            try{
                this.wait();
            }catch(Exception e){}
        }
       
    }
     /*-------------Discovery Listener -----------------------------------------------------*/
    public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
        String name="";
        String addr = rd.getBluetoothAddress();
        try {
            name = rd.getFriendlyName(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.devices[this.num_discovered] = rd;
        this.num_discovered++;
        btDevice.append(name, null);
    }

    public void inquiryCompleted(int param) {
        switch(param){
            case DiscoveryListener.INQUIRY_COMPLETED:
                if(devices.length > 0){
                displayDeviceList();
                }
                else{
                   do_alert("No device found in range",4000);
                }
                break;
            case DiscoveryListener.INQUIRY_ERROR:
                do_alert("Inquiry error",4000);
                break;
            case DiscoveryListener.INQUIRY_TERMINATED:
                do_alert("Inquiry canceled",4000);
                break;
        }
    }
    public void servicesDiscovered(int transID, ServiceRecord[] rec) {
        for(int i =0; i<rec.length;i++){
            DataElement d = rec[i].getAttributeValue(SERVICE_NAME_ATTRID);
            this.URL = rec[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            System.out.println(URL);
        }
    }
    public void serviceSearchCompleted(int i, int i1) {
        synchronized(this){
            try{
                this.notifyAll();
            }catch(Exception e){}
        }
    }

   /*----------------ambil data dari bluetooth-------------*/
    private void getData(){
        System.out.println("getting data.....");
        String cmd="";
        String str="";
        int input=0;
        try {
            //Koneksi ke bluetooth
            conn = (StreamConnection)Connector.open(URL);
            //menyiapkan variabel untuk data yang masuk
            DataInputStream in = conn.openDataInputStream();
                int i=0;
                char c1;
                   while(time){
                       //membaca tipe data yang masuk
                        input = in.readInt();
                        cmd = cmd + input +"/";
                        System.out.print("input"+cmd);
                        formInput.deleteAll();
                        formInput.append(cmd);
                      }
                    
                if(time == false){
                    System.out.println("time up");
                        in.close();
                        conn.close();
                        dataresult = cmd;
                        formInput.addCommand(result);
                        display.setCurrent(formInput);
                    }
            } catch (IOException ex) {
                ex.printStackTrace();
        }
        
    }

/*----------------Gambar screen Mencari device---------------------------------*/
    protected void paint(Graphics g) {
        
            g.setColor(128,128,255);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(255,255,255);
            g.drawString("Mencari Device...", 10, 10, Graphics.TOP|Graphics.LEFT);
       
        if(this.counter == 1){
            g.setColor(255,115,200);
            g.fillRect(10, 80, 10, 10);
        }
        if(this.counter == 2){
            g.setColor(255,115,200);
            g.fillRect(10, 80, 10, 10);
            g.setColor(100,255,255);
            g.fillRect(25, 70, 10, 20);
           
        }
        if(this.counter == 3){
            g.setColor(255,115,200);
            g.fillRect(10, 80, 10, 10);
            g.setColor(100,255,255);
            g.fillRect(25, 70, 10, 20);
            g.setColor(255,115,200);
            g.fillRect(40, 60, 10, 30);
        }
        if(this.counter == 4){
            g.setColor(255,115,200);
            g.fillRect(10, 80, 10, 10);
            g.setColor(100,255,255);
            g.fillRect(25, 70, 10, 20);
            g.setColor(255,115,200);
            g.fillRect(40, 60, 10, 30);
            g.setColor(100,255,255);
            g.fillRect(55, 50, 10, 40);
        }
    }
     /*--------------------Display Device List------------------------------*/
    private void displayDeviceList(){
        
            btDevice.addCommand(select);
            btDevice.addCommand(back);
            btDevice.setCommandListener(this);
            display.setCurrent(btDevice);
    }
      /*------------------ Display Input Form---------------------------------*/       
    private void displayInputForm(){
            formInput.addCommand(back);
            formInput.addCommand(measure);
            gender.append("Pria", null);
            gender.append("Wanita", null);
            formInput.append(nama);
            formInput.append(umur);
            formInput.append(gender);
            formInput.setCommandListener(this);
            display.setCurrent(formInput);
    }
private void do_alert(String msg, int time_out){
        if(display.getCurrent() instanceof Alert){
            ((Alert)display.getCurrent()).setString(msg);
            ((Alert)display.getCurrent()).setTimeout(time_out);
        }else{
            Alert alert = new Alert("Bluetooth");
            alert.setString(msg);
            alert.setTimeout(time_out);
            display.setCurrent(alert);
        }
        }

    //timer task fungsinya ketika telah mencapai waktu yg dijadwalkan putus koneksi
    private static class TestTimerTask2 extends TimerTask{

        public TestTimerTask2() {
        }
        public void run() {
            time = false;
        }
    }   
}
