/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import pl.adrian.pieper.data.ConfigXML;

/**
 *
 * @author Adi
 */
public class Processors {

    public static Collection<ProcessorModule> readConfig(File file) {
        ConfigXML configXML = ConfigXML.load(file);
        ArrayList<ProcessorModule> modules = new ArrayList<>();
        modules.add(new PlaceHolderModule(Arrays.asList(configXML.placeHolders)));
        if (configXML.hasTable)
            modules.add(new TableModule());
        return modules;
    }
    
}
