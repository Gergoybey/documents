/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

/**
 *
 * @author Adi
 */
public class ProcessFile {
    private WordprocessingMLPackage  wordMLPackage;

    public ProcessFile(File file) {
        try {
            wordMLPackage = WordprocessingMLPackage.load(new FileInputStream(file));
        } catch (FileNotFoundException | Docx4JException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public <T> List<T> getAllElement(Class<T> toSearch){
        return getAllElementFromObject(wordMLPackage.getMainDocumentPart(),toSearch);
    }
    
    public <T> List<T> getAllElementFromObject(Object obj, Class<T> toSearch) {
        List<T> result = new ArrayList<>();

        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch)){

            result.add((T) obj);
        }
        else if (obj instanceof ContentAccessor) {

        List children = ((ContentAccessor) obj).getContent();

        for (Object child : children) {

                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }

        return result;

    }
    
    public void replacePlaceholders(Map<String,String> placeholders) {

        for (Text textElement : getAllElement(Text.class)) {
            
            String ph = placeholders.get(textElement.getValue());
            if (ph != null){
                textElement.setValue(ph);
            }
        }
    }
    
    public void replacePlaceholder(String name, String placeholder ) {

        for (Text textElement : getAllElement(Text.class)) {
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }
    
    public void save(String filename){
        
        try {
            final File file = new File(filename);
            wordMLPackage.save(file);
        } catch (Docx4JException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
