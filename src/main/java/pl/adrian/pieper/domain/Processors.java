/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.util.Arrays;
import pl.adrian.pieper.data.PlaceHoldersXML;

/**
 *
 * @author Adi
 */
public class Processors {
    public static final String HOLDERS = "holders.xml";

    public static ProcessorModule createfor(File file) {
        if (file.getName().equals(HOLDERS)){
            PlaceHoldersXML placeHoldersXML = PlaceHoldersXML.load(file);
            
            return new PlaceHolderModule(Arrays.asList(placeHoldersXML.placeHolders));
        }
        throw new RuntimeException();
    }
    
}
