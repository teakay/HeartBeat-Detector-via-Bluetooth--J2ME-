package hello;

import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;



public class EchoServer extends MIDlet implements CommandListener{
    private static final UUID uuid = new UUID("F0E0D0C0B0A000908070605040302010",false);
    // private static final UUID uuid = new UUID("AABBCCDDEEFF",false);
    public final String name = "Echo Server";
//    public final String url = "btspp://localhost:"+uuid+";name="+name+";authenticate=false;encrypt=false;";
    StringBuffer url =new StringBuffer("btspp://");
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;
    Display display;
    Form formServer;
    TextField tf,tfClient;
    
    public EchoServer() throws IOException{
      display = Display.getDisplay(this);
       formServer = new Form("Server");
       tf = new TextField("Any","",50,TextField.ANY);
       formServer.append(tf);
       formServer.setCommandListener(this);
    }
   
  

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void pauseApp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void startApp(){
       
       display.setCurrent(formServer);
        try {
            connect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void connect() throws IOException{
         // try {
           
            
            System.out.println("Setting device to be discoverable...");
            //definisi diri sendiri
            local = LocalDevice.getLocalDevice();
            //set supaya bisa ditemukan device lain /klien
            local.setDiscoverable(DiscoveryAgent.GIAC);
            
            System.out.println("Start advertising service...");
            //set url untuk server btspp://localhost:oewrirej9765;name=Echo Server;authorize=false;
            url.append("localhost").append(':');
            url.append(uuid.toString());
            url.append(";name="+name);
            url.append(";authorize=false");
            //membuat service record yang mewakili layanan server
            server = (StreamConnectionNotifier)Connector.open(url.toString());
            System.out.println("Waiting for incoming connection...");
            //service siap untuk menerima koneksi klien
            conn = server.acceptAndOpen();
            
            System.out.println("Client Connected");
            //mempersiapkan data masuk dari klien
            DataInputStream din = new DataInputStream(conn.openInputStream());
            DataOutputStream dout = new DataOutputStream(conn.openOutputStream());
            
//            dout.writeChars("123\n");
//            dout.writeChars("lowhigh\n");
//            dout.writeChars("125\n");
//            dout.writeChars("lowhigh\n");
//            dout.writeChars("125\n");
//            dout.writeChars("lowhigh\n");
//            dout.writeChars("123\n");
//            dout.writeChars("124\n");
//            dout.writeChars("lowhigh\n");
//            dout.writeChars("lowhigh\n");
//            dout.writeChars("123\n");
//            dout.writeChars("124\n");
            
            //dout.writeChars("123\r\n124\r\nlowhigh\r\n125\r\nlowhigh\r\n123\r\n124\r\nlowhigh\r\n");
              dout.writeInt(123);
              dout.writeChars("\r\n");
              dout.writeInt(-124);
               dout.writeChars("\r\n");
              dout.writeInt(-123);
               dout.writeChars("\r\n");
              dout.writeInt(125);
               dout.writeChars("\n");
              dout.writeInt(-123);
               dout.writeChars("\n");
              dout.writeInt(126);
               dout.writeChars("\n");
              dout.writeInt(-123);
               dout.writeChars("\n");
              dout.writeInt(-123);
               dout.writeChars("\n");
              dout.writeInt(124);
              
           //   dout.writeChars("lowhigh");
            dout.flush();
//            while(true){
//                
//                String cmd = "";
//                char c;
//                
//                while(((c = din.readChar())>0) && (c != '\n')){
//                    cmd = cmd + c;
//                }
                //System.out.println("Received "+cmd);
//                tf.setString(cmd);
                
 //           }
//        } catch (BluetoothStateException ex) {
//            ex.printStackTrace();
//        }
        
    }
}
