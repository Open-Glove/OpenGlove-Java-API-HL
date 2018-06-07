/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proof.concept;
import java.util.ArrayList;
import java.util.List;
import org.datacontract.schemas._2004._07.openglovewcf.Glove;
/**
 *
 * @author RaySis
 */
public class GloveRules {
    
    String GloveName;
    List<Parameters> parameters;

    public GloveRules() {
        GloveName = "";
        this.parameters = new ArrayList<Parameters>();
    }
    
    public String getGloveName() {
        return GloveName;
    }

    public void setGloveName(String GloveName) {
        this.GloveName = GloveName;
    }
    
    private Glove getGloveFromRules(List<Glove> gloves){
        for(Glove g : gloves){
            if(g.getBluetoothAddress().getValue().equals(this.GloveName)){
                return g;
            }
        }
        return null;
    }
    
}
