/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketOG;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author RaySis
 */
public class Client extends WebSocketClient{
    
    String[] words;
  //  Event moverdedo = new Event();

    public Client(URI serverUri) {
        super(serverUri);
    }
    
    private FingerMovement fingersListener = null;
    
    public void setFingerMovement(FingerMovement listener) {
        this.fingersListener = listener;
    }
    
    @FunctionalInterface
    public interface FingerMovement {
        public void run(int region, int value);
    }
    
    ////////////////////////////////////////////////////////
    private accelerometerValues accelerometerListener = null;
    
    public void setAccelerometerValues(accelerometerValues listener) {
        this.accelerometerListener = listener;
    }
     
   @FunctionalInterface
    public interface accelerometerValues {
        public void run(float ax, float ay, float az);
    }
    
    ////////////////////////////////////////////////////////
    private gyroscopeValues gyroscopeListener = null;
    
    public void setGyroscopeValues(gyroscopeValues listener) {
        this.gyroscopeListener = listener;
    }
    
    @FunctionalInterface
    public interface gyroscopeValues {
        public void run(float gx, float gy, float gz);
    }
    
    ////////////////////////////////////////////////////////
    private magnometerValues magnometerListener = null;
    
    public void setMagnometerValues(magnometerValues listener) {
        this.magnometerListener = listener;
    }
    
    @FunctionalInterface
    public interface magnometerValues {
        public void run(float mx, float my, float mz);
    }
    
    ////////////////////////////////////////////////////////
    private allIMUValues allIMUListener = null;
    
    public void setAllIMUValues(allIMUValues listener) {
        this.allIMUListener = listener;
    }
    
    @FunctionalInterface
    public interface allIMUValues {
        public void run(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz);
    }

    @Override
    public void onOpen(ServerHandshake sh) {
        System.out.println("Client <"+this.getURI()+"> connected");
    }
    
    @Override
    public void onMessage(String string) {
       if(string != null)
       {
           words = string.trim().split(",");
           try{
                switch(words[0])
                {
                    case "f":
                        if(fingersListener!=null)
                        {
                            fingersListener.run(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                        }
                        break;
                    case "a":
                        if(accelerometerListener!=null){
                            accelerometerListener.run(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[1]));
                        }
                        break;
                    case "g":
                        if(gyroscopeListener!=null){
                            gyroscopeListener.run(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[1]));
                        }
                        break;
                    case "m":
                        if(magnometerListener!=null){
                            magnometerListener.run(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[1]));
                        }
                        break;
                    case "z":
                        if(allIMUListener!=null){
                            allIMUListener.run(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]), 
                                    Float.parseFloat(words[4]), Float.parseFloat(words[5]), Float.parseFloat(words[6]), 
                                    Float.parseFloat(words[7]), Float.parseFloat(words[8]), Float.parseFloat(words[9]));
                        }
                        break;
                    default:
                        break;
                }
           }catch(Exception e){}
       }
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        System.out.println("Client <"+this.getURI()+"> closed");
       
    }

    @Override
    public void onError(Exception excptn) {
        System.out.println("Error check the glove connection");
    }
    
    
}
