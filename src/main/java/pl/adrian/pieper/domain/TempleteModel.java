/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 *
 * @author Adi
 */
public class TempleteModel {
    
    private final List<ProcessorModule> modules = new ArrayList<>();
    private final File tempDir;
    
    private TempleteModel(File dir) {
        this.tempDir = dir;
        System.out.println("is dir");
        for (File file : dir.listFiles()) {
            analize(file);
        }
    }

    private void analize(File file) {
        System.out.println(file.getName());
        if (file.getName().endsWith(".xml")){
            System.out.println(file.getName());
            ProcessorModule module = Processors.createfor(file);
            addModule(module);
        }
    }
    
    private void addModule(ProcessorModule module) {
        modules.add(module);
    }

    public List<ProcessorModule> getModules() {
        return modules;
    }

    public interface ProgressGUI{
        public void showProgress(int i,int N,String processingFile);
        public void done();
    }
    
    public void process(String outDirName,final ProgressGUI gui) {
        new Thread(){

                @Override
                public void run() {
                    final File outdir = new File("out/" + outDirName + "/");
                    
                    
                    outdir.mkdirs();
                    List<File> files = new ArrayList<>(Arrays.asList(tempDir.listFiles()));
                    files.removeIf(file -> !file.getName().endsWith(".docx"));
                    int N = files.size();
                    int i = 0;
                    for (final File file : files) {
                        final int fileId = i;
                        SwingUtilities.invokeLater(() -> gui.showProgress(fileId, N, file.getName())                       );
                        i++;
                        if (file.getName().contains(".docx")) {
                            ProcessFile processFile = new ProcessFile(file);

                            for (ProcessorModule module : modules) {
                                module.process(processFile);
                            }

                            processFile.save(outdir.getPath() + "/" + file.getName());
                        }
                    } 
                    
                    SwingUtilities.invokeLater(()->gui.done());
                    
                }
                
            }.start();
        
    }

    @Override
    public String toString() {
        return " dir : " + tempDir.getAbsolutePath();
    }
    
    public static class Info{
        
        private File dir;

        public Info(File dir) {
            this.dir = dir;
        }

        @Override
        public String toString() {
            return dir.getName();
        }
        
        public TempleteModel create(){
            return new TempleteModel(dir);
        }
    }
}
