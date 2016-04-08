/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/**
 *
 * @author Adi
 */
public abstract class ProcessorModule {

    public abstract Processor getProcessor();
    
    public abstract void attach(Gui gui);
    
    public interface Gui{
        void attach(PlaceHolderModule holderProcessor);
        void attach(TableModule tableModule);
    }
    
    public interface Processor{
        void process(ProcessFile processFile);
    }
}
