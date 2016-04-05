/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adi
 */
public class PlaceHolderProcessor extends ProcessorModule{
    private final Map<String,SimpleData> placeHolders = new HashMap<>();

    public PlaceHolderProcessor(String fileName) {
        
        List<SimpleData> data = SimpleData.parseFile(fileName);
        for (SimpleData simpleData : data) {
            placeHolders.put(simpleData.getPlaceholder(), simpleData);
        }
    }

    private void addData(String name,String placeholder){
        
        placeHolders.put(placeholder, new SimpleData(name,placeholder));
    }

    public Map<String, SimpleData> getPlaceHolders() {
        return placeHolders;
    }
    
    @Override
    public void process(){
        Processor processor = new Processor();
        
        Map<String,String> values = new HashMap<>();
        
        for (SimpleData value : placeHolders.values()) {
            values.put(value.getPlaceholder(), value.getValue());
        }
        
        processor.replace("szablony/1.Pomiary-temp.docx","out.docx",values);
    }

    @Override
    public void attach(Gui gui) {
        gui.attach(this);
    }
}
