/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Adi
 */
public class PlaceHolderModule extends ProcessorModule{
    private final List<PlaceHolder> placeHolders = new ArrayList<>();

    public PlaceHolderModule(Collection<PlaceHolder> data) {
        placeHolders.addAll(data);
    }

    public List<PlaceHolder> getPlaceHolders() {
        return placeHolders;
    }
    
    @Override
    public void attach(Gui gui) {
        gui.attach(this);
    }

    @Override
    public Processor getProcessor() {
        return new HoldersProc();
    }
    
    class HoldersProc implements Processor{
        Map<Pattern,String> values = new HashMap<>();

        public HoldersProc() {
        
            for (PlaceHolder value : placeHolders) {
                values.put(Pattern.compile(value.getPlaceholder()), value.getValue());
            }
        }
        
        @Override
        public void process(ProcessFile processFile) {
            processFile.replacePlaceholders(values);
        }
    }
}
