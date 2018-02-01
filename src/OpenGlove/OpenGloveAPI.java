/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpenGlove;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfstringstring;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfstringstring.KeyValueOfstringstring;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datacontract.schemas._2004._07.openglovewcf.Glove;
import org.tempuri.IOGService;
import org.tempuri.OGService;
import websocketOG.DataReceiver;

/**
 *
 * @author RaySis
 */
public class OpenGloveAPI {
    private static OpenGloveAPI instance;
    private final IOGService serviceClient;
    private List<Glove> devices;
    private List<DataReceiver> DataReceivers;
    
    OpenGloveAPI(){
        serviceClient = new OGService().getBasicHttpBindingIOGService();
        DataReceivers = new ArrayList<DataReceiver>();
    }
    
    public static OpenGloveAPI getInstance(){
        if(instance == null)
        {
            instance = new OpenGloveAPI();
        }
        return instance;
    }
    
    public DataReceiver getDataReceiver(Glove selectedGlove){
       for(int i = 0; i < DataReceivers.size();i++){
          if(DataReceivers.get(i).SerialPort.equals(selectedGlove.getPort().getValue()) ){
              return DataReceivers.get(i);
          }
       }
       return null;
    }
    
    public void startCaptureData(Glove selectedGlove){
        if(DataReceivers !=null){
            for(int i = 0; i < DataReceivers.size();i++){
                if(DataReceivers.get(i).SerialPort.equals(selectedGlove.getPort().getValue()) ){
                    return; //already exist
                }
             }
        }
        DataReceiver data = new DataReceiver(selectedGlove.getWebSocketPort().getValue(),selectedGlove.getPort().getValue());
        DataReceivers.add(data);
    }
    
    public void stopCaptureData (Glove selectedGlove){
        if (DataReceivers != null){
            for(int i = 0; i < DataReceivers.size();i++){
                if(DataReceivers.get(i).SerialPort.equals(selectedGlove.getPort().getValue()) ){
                    DataReceivers.get(i).WebSocketActive = false;
                    try {
                        Thread.sleep(100);
                        DataReceivers.remove(DataReceivers.get(i));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OpenGloveAPI.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                    return;
                }
             }
        }
    }
    
    public List<Glove> getDevices() {
        devices = serviceClient.getGloves().getGlove();
        return devices;
    }
    
    public int addFlexor(Glove selectedGlove, int flexor, int mapping)
    {
        return serviceClient.addFlexor(selectedGlove.getBluetoothAddress().getValue(), flexor, mapping);
    }
    
    public int removeFlexor(Glove selectedGlove, int mapping)
    {
        return this.serviceClient.removeFlexor(selectedGlove.getBluetoothAddress().getValue(), mapping);
    }

    public void calibrateFlexors(Glove selectedGlove)
    {
       this.serviceClient.calibrateFlexors(selectedGlove.getBluetoothAddress().getValue());
    }

    public void confirmCalibration(Glove selectedGlove)
    {
        this.serviceClient.confirmCalibration(selectedGlove.getBluetoothAddress().getValue());
    }

    public void setThreshold(Glove selectedGlove, int value)
    {
        this.serviceClient.setThreshold(selectedGlove.getBluetoothAddress().getValue(),value);
    }

    public void resetFlexors(Glove selectedGlove)
    {
        this.serviceClient.resetFlexors(selectedGlove.getBluetoothAddress().getValue());
    }

    public void startIMU(Glove selectedGlove)
    {
        this.serviceClient.startIMU(selectedGlove.getBluetoothAddress().getValue());
    }
    
    public void setIMUStatus(Glove selectedGlove, boolean value)
    {
        if (value == true)
        {
            this.serviceClient.setIMUStatus(selectedGlove.getBluetoothAddress().getValue(), 1);
        }else
        {
            this.serviceClient.setIMUStatus(selectedGlove.getBluetoothAddress().getValue(), 0);
        }
    }
    
    public void setRawData(Glove selectedGlove, boolean value)
    {
        if (value == true)
        {
            this.serviceClient.setRawData(selectedGlove.getBluetoothAddress().getValue(), 1);
        }
        else
        {
            this.serviceClient.setRawData(selectedGlove.getBluetoothAddress().getValue(), 0);
        }
    }
    
    
    public int Connect(Glove selectedGlove)
    {
        try
        {
            return this.serviceClient.connect(selectedGlove.getBluetoothAddress().getValue());
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    
    public void saveGlove(Glove selectedGlove)
    {
        this.serviceClient.saveGlove(selectedGlove);
    }

    /// <summary>
    /// Closes a connection with a glove
    /// </summary>
    /// <param name="selectedGlove">A Glove object to be connected</param>
    /// <returns>Result code</returns>
    public int Disconnect(Glove selectedGlove)
    {
        try
        {
            return this.serviceClient.disconnect(selectedGlove.getBluetoothAddress().getValue());
        }
        catch (Exception e)
        {

            return -1;
        }
    }
    
    public void Activate(Glove selectedGlove, Integer region, Integer intensity) {
        int actuator = -1;
        List<KeyValueOfstringstring> mappings = selectedGlove.getGloveConfiguration().getValue().getGloveProfile().getValue().getMappings().getValue().getKeyValueOfstringstring();
        for (KeyValueOfstringstring item : mappings) {
            if (item.getKey().equals(region.toString())) {
                actuator = Integer.parseInt(item.getValue());
                break;
            }
        }
        
        if (actuator == -1) {
            return;
        }
        
        this.serviceClient.activate(selectedGlove.getBluetoothAddress().getValue(), actuator, intensity);
    }
    
    public void Activate(Glove selectedGlove, List<Integer> regions, List<Integer> intensityList) {
        ArrayOfint actuators = new ArrayOfint();
        
        for (Integer region : regions) {
            int actuator = -1;
            List<KeyValueOfstringstring> mappings = selectedGlove.getGloveConfiguration().getValue().getGloveProfile().getValue().getMappings().getValue().getKeyValueOfstringstring();
            for (KeyValueOfstringstring item : mappings) {
                if (item.getKey().equals(region.toString())) {
                    actuator = Integer.parseInt(item.getValue());
                    break;
                }
            }
            if (actuator == -1) {
                return;
            }
            actuators.getInt().add(actuator);
        }
        
        ArrayOfint intensityListAOI = new ArrayOfint();
        intensityList.stream().forEach((intensity) -> {
            intensityListAOI.getInt().add(intensity);
        });
        
        this.serviceClient.activateMany(selectedGlove.getBluetoothAddress().getValue(), actuators, intensityListAOI);
    }
    
     public enum HandRegion {
        PalmarFingerSmallDistal(0),
        PalmarFingerRingDistal(1),
        PalmarFingerMiddleDistal(2),
        PalmarFingerIndexDistal(3),
        PalmarFingerSmallMiddle(4),
        PalmarFingerRingMiddle(5),
        PalmarFingerMiddleMiddle(6),
        PalmarFingerIndexMiddle(7),
        PalmarFingerSmallProximal(8),
        PalmarFingerRingProximal(9),
        PalmarFingerMiddleProximal(10),
        PalmarFingerIndexProximal(11),
        PalmarPalmSmallDistal(12),
        PalmarPalmRingDistal(13),
        PalmarPalmMiddleDistal(14),
        PalmarPalmIndexDistal(15),
        PalmarPalmSmallProximal(16),
        PalmarPalmRingProximal(17),
        PalmarPalmMiddleProximal(18),
        PalmarPalmIndexProximal(19),
        PalmarHypoThenarSmall(20),
        PalmarHypoThenarRing(21),
        PalmarThenarMiddle(22),
        PalmarThenarIndex(23),
        PalmarFingerThumbProximal(24),
        PalmarFingerThumbDistal(25),
        PalmarHypoThenarDistal(26),
        PalmarThenar(27),
        PalmarHypoThenarProximal(28),
        DorsalFingerSmallDistal(29),
        DorsalFingerRingDistal(30),
        DorsalFingerMiddleDistal(31),
        DorsalFingerIndexDistal(32),
        DorsalFingerSmallMiddle(33),
        DorsalFingerRingMiddle(34),
        DorsalFingerMiddleMiddle(35),
        DorsalFingerIndexMiddle(36),
        DorsalFingerSmallProximal(37),
        DorsalFingerRingProximal(38),
        DorsalFingerMiddleProximal(39),
        DorsalFingerIndexProximal(40),
        DorsalPalmSmallDistal(41),
        DorsalPalmRingDistal(42),
        DorsalPalmMiddleDistal(43),
        DorsalPalmIndexDistal(44),
        DorsalPalmSmallProximal(45),
        DorsalPalmRingProximal(46),
        DorsalPalmMiddleProximal(47),
        DorsalPalmIndexProximal(48),
        DorsalHypoThenarSmall(49),
        DorsalHypoThenarRing(50),
        DorsalThenarMiddle(51),
        DorsalThenarIndex(52),
        DorsalFingerThumbProximal(53),
        DorsalFingerThumbDistal(54),
        DorsalHypoThenarDistal(55),
        DorsalThenar(56),
        DorsalHypoThenarProximal(57);
        
        private final int  _value;
        
        public int getValue() {
            return _value;
        }
        
        HandRegion(int Value) {
        this._value = Value;
        }
    }
     
    public enum FlexorsRegion {
        ThumbInterphalangealJoint(0),
        IndexInterphalangealJoint(1),
        MiddleInterphalangealJoint(2),
        RingInterphalangealJoint(3),
        SmallInterphalangealJoint(4),

        ThumbMetacarpophalangealJoint(5),
        IndexMetacarpophalangealJoint(6),
        MiddleMetacarpophalangealJoint(7),
        RingMetacarpophalangealJoint(8),
        SmallMetacarpophalangealJoint(9);
        
        private final int  _value;
        
        public int getValue() {
            return _value;
        }
        
        FlexorsRegion(int Value) {
        this._value = Value;
        }
    }
    
}
