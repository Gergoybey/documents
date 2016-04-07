/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adi
 */
public class PlaceHolderModule extends ProcessorModule{
    private final Map<String,PlaceHolder> placeHolders = new HashMap<>();

    public PlaceHolderModule(Collection<PlaceHolder> data) {
        for (PlaceHolder simpleData : data) {
            placeHolders.put(simpleData.getPlaceholder(), simpleData);
        }
    }
    
    private void addData(String name,String placeholder){
        
        placeHolders.put(placeholder, new PlaceHolder(name,placeholder));
    }

    public Map<String, PlaceHolder> getPlaceHolders() {
        return placeHolders;
    }
    
    @Override
    public void attach(Gui gui) {
        gui.attach(this);
    }

    @Override
    public void process(ProcessFile processFile) {
        Map<String,String> values = new HashMap<>();
        
        for (PlaceHolder value : placeHolders.values()) {
            values.put(value.getPlaceholder(), value.getValue());
        }
        
        
        processFile.replacePlaceholders(values);
    }
}
