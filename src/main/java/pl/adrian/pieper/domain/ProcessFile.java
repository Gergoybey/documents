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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Br;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 *
 * @author Adi
 */
public class ProcessFile {
    private WordprocessingMLPackage  wordMLPackage;
    private ObjectFactory factory = Context.getWmlObjectFactory();

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
    
    public static <T>  List<T> getAllElementFromObject(Object obj, Class<T> toSearch) {
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
    
    public void replacePlaceholders(Map<Pattern,String> placeholders) {
        int i = 0;
        for (Text textElement : getAllElement(Text.class)) {
            System.out.println(i++ + " " + textElement.getValue());
            
            String textValue = textElement.getValue();
            for (Map.Entry<Pattern, String> entrySet : placeholders.entrySet()) {
                Pattern compile = entrySet.getKey(); 
                String value = entrySet.getValue();
                textValue = compile.matcher(textValue).replaceAll(value);
                
            }
            
            textElement.setValue(textValue);
            
        }
    }
    
    public static void replacePlaceholderIn(Object object, String placeholder,String value){
        Pattern pattern = Pattern.compile(placeholder);
        for (Text textElement : getAllElementFromObject(object,Text.class)) {
            final String oldValue = textElement.getValue();
            final String newValue = pattern.matcher(oldValue).replaceAll(value);
            textElement.setValue(newValue);
        }
    }
    
    public void replacePlaceholder(String name, String placeholder ) {
        replacePlaceholderIn(wordMLPackage.getMainDocumentPart(),name,placeholder);
    }
    
    
    
    private void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd,WordprocessingMLPackage template) 
            throws Docx4JException, JAXBException {
        // 1. find the table
        Tbl tempTable = getTemplateTable(placeholders[0]);
        List<Tr> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = rows.get(1);

            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements,1);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }
    
    public Tbl getTemplateTable(String templateKey) {
    
    for (Tbl tbl : getAllElement(Tbl.class)) {
            List<Text> textElements = getAllElementFromObject(tbl, Text.class);
            for (Text textElement : textElements) {
                
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey)) {
                    return tbl;
                }
            }
        }
        return null;
    }
    
    public static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements,int row) {
        Tr newRow = XmlUtils.deepCopy(templateRow);
        List<Text> textElements = getAllElementFromObject(newRow, Text.class);
        
        for (Text text : textElements) {
            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue != null) {
                text.setValue(replacementValue);
            }
        }

        reviewtable.getContent().add(row,newRow);
    }
    
    public void add(Object object){
        wordMLPackage.getMainDocumentPart().getContent().add(object);
    }
    
    public void save(String filename){
        
        try {
            final File file = new File(filename);
            wordMLPackage.save(file);
        } catch (Docx4JException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nextPage() {
        Br objBr = new Br();
        objBr.setType(STBrType.PAGE);
        P para = factory.createP();
        para.getContent().add(objBr);
        add(para);
    }
}
