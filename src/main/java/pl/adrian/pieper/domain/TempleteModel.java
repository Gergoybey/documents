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
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.SwingUtilities;
import pl.adrian.pieper.domain.ProcessorModule.Processor;

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
        analize(new File(dir,"config.xml"));
    }

    private void analize(File file) {
        System.out.println(file.getName());
        this.modules.addAll(Processors.readConfig(file));
    }
    
    public List<ProcessorModule> getModules() {
        return modules;
    }

    public interface ProgressGUI{
        public void showProgress(int i,int N,String processingFile);
        public void done();
    }
    
    public void process(String outDirName,final ProgressGUI gui) {
        new ThreadImpl(outDirName, gui).start();
        
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

    private class ThreadImpl extends Thread {

        private final String outDirName;
        private final ProgressGUI gui;

        public ThreadImpl(String outDirName, ProgressGUI gui) {
            this.outDirName = outDirName;
            this.gui = gui;
        }

        @Override
        public void run() {
            final File outdir = new File("out/" + outDirName + "/");
            
            
            outdir.mkdirs();
            List<File> files = new ArrayList<>(Arrays.asList(tempDir.listFiles()));
            files.removeIf(file -> !file.getName().endsWith(".docx") || file.getName().startsWith("~"));
            int N = files.size();
            int i = 0;
            
            List<Processor> processors = new ArrayList<>(modules.size());
            modules.forEach((ProcessorModule t) -> {
                processors.add(t.getProcessor());
            });
            
            for (final File file : files) {
                final int fileId = i;
                SwingUtilities.invokeLater(() -> gui.showProgress(fileId, N, file.getName())                       );
                i++;
                ProcessFile processFile = new ProcessFile(file);
                
                for (Processor processor : processors) {
                    processor.process(processFile);
                }
                
                processFile.save(outdir.getPath() + "/" + file.getName());
                
            }
            
            SwingUtilities.invokeLater(()->gui.done());
            
        }
    }
}
