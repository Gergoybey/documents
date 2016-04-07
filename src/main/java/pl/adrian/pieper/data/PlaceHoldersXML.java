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
@XStreamAlias("pola")
public class PlaceHoldersXML {
    @XStreamImplicit
    public PlaceHolder[] placeHolders;
    
    
    public static XStream getXSteam(){
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(PlaceHoldersXML.class);
        xstream.processAnnotations(PlaceHolder.class);
        return xstream;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        PlaceHoldersXML holdersXML = new PlaceHoldersXML();
        holdersXML.placeHolders = new PlaceHolder[]{new PlaceHolder("Ulica", "{STREET}", "DEF VALUE"),new PlaceHolder("Ulica", "{STREET}", "DEF VALUE")};
        getXSteam().toXML(holdersXML, new FileOutputStream("out.xml"));
    }
    
    public static PlaceHoldersXML load(File file){
        final PlaceHoldersXML xml = (PlaceHoldersXML) getXSteam().fromXML(file);
        for (PlaceHolder placeHolder : xml.placeHolders) {
            placeHolder.setDefault();
        }
        return xml;
    }
}
