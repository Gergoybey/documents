/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

/**
 *
 * @author Adi
 */
public abstract class ProcessorModule {

    public abstract void process();
    
    public abstract void attach(Gui gui);
    
    public interface Gui{
        void attach(PlaceHolderProcessor holderProcessor);
    }
}
