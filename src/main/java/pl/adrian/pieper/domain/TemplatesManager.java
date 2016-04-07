/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adi
 */
public class TemplatesManager {
    
    private final List<TempleteModel.Info> templates = new ArrayList<>();

    public TemplatesManager() {
    }

    public void load(String templatesDirName) {
        System.out.println("scan : " + templatesDirName);
        File temps = new File(templatesDirName);
        for(File dir : temps.listFiles()){
            System.out.println("scan : " + dir.toString());
            if (dir.isDirectory()){
                TempleteModel.Info templeteModel = new TempleteModel.Info(dir);
                templates.add(templeteModel);
            }
        }    
    }


    public List<TempleteModel.Info> getTemplates() {
        return templates;
    }
    
    public static void main(String[] args) {
        List<TempleteModel.Info> templates = new TemplatesManager().getTemplates();
    
        for (TempleteModel.Info template : templates) {
            System.out.println(template.toString());
            
        }
    }

}
