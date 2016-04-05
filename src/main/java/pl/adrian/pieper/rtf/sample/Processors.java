/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

import java.io.File;

/**
 *
 * @author Adi
 */
public class Processors {

    public static ProcessorModule createfor(File file) {
        if (file.getName().equals("holders.xml")){
            return new PlaceHolderProcessor(file.getPath());
        }
        throw new RuntimeException();
    }
    
}
