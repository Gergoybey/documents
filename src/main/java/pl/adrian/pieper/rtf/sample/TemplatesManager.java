/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adi
 */
public class TemplatesManager {
    
    private final List<TempleteModel> templates = new ArrayList<>();

    public TemplatesManager() {
    }

    public void load(String templatesDirName) {
        System.out.println("scan : " + templatesDirName);
        File temps = new File(templatesDirName);
        for(File dir : temps.listFiles()){
            System.out.println("scan : " + dir.toString());
            if (dir.isDirectory()){
                TempleteModel templeteModel = new TempleteModel(dir);
                templates.add(templeteModel);
                System.out.println("is dir");
                for (File file : dir.listFiles()) {
                    analize(file,templeteModel);
                }
            }
        }    
    }

    private void analize(File file, TempleteModel templeteModel) {
        System.out.println(file.getName());
        if (file.getName().endsWith(".xml")){
            System.out.println(file.getName());
            ProcessorModule module = Processors.createfor(file);
            templeteModel.addModule(module);
        }
    }

    public List<TempleteModel> getTemplates() {
        return templates;
    }
    
    public static void main(String[] args) {
        List<TempleteModel> templates = new TemplatesManager().getTemplates();
    
        for (TempleteModel template : templates) {
            System.out.println(template.toString());
            
        }
    }

}
