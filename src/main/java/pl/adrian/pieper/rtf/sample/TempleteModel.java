/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adi
 */
public class TempleteModel {
    
    private String name;
    private final List<ProcessorModule> modules = new ArrayList<>();
    private File dir;
    
    public TempleteModel(String name) {
        this.name = name;
    }

    public TempleteModel(File dir) {
        this.name = dir.getName();
        this.dir = dir;
    }

    public void addModule(ProcessorModule module) {
        modules.add(module);
    }

    public List<ProcessorModule> getModules() {
        return modules;
    }

    public void process() {
        for (ProcessorModule module : modules) {
            module.process();
        }
    }

    @Override
    public String toString() {
        return name + " dir : " + dir.getAbsolutePath();
    }
    
    
}
