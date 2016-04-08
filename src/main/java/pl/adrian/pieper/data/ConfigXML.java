/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.data;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import pl.adrian.pieper.domain.PlaceHolder;

/**
 *
 * @author Adi
 */
@XStreamAlias("config")
public class ConfigXML {
    @XStreamImplicit
    public PlaceHolder[] placeHolders;
    @XStreamAlias("tablica")
    public boolean hasTable;
    
    public static XStream getXSteam(){
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(ConfigXML.class);
        xstream.processAnnotations(PlaceHolder.class);
        return xstream;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        ConfigXML holdersXML = new ConfigXML();
        holdersXML.placeHolders = new PlaceHolder[]{new PlaceHolder("Ulica", "{STREET}", "DEF VALUE"),new PlaceHolder("Ulica", "{STREET}", "DEF VALUE")};
        getXSteam().toXML(holdersXML, new FileOutputStream("out.xml"));
    }
    
    public static ConfigXML load(File file){
        final ConfigXML xml = (ConfigXML) getXSteam().fromXML(file);
        for (PlaceHolder placeHolder : xml.placeHolders) {
            placeHolder.setDefault();
        }
        return xml;
    }
}
