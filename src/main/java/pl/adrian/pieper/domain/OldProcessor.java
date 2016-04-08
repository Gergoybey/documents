/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;


/**
 *
 * @author Adi
 */


public class OldProcessor {
    private static WordprocessingMLPackage  wordMLPackage;
    private static ObjectFactory factory;
 
    public static void main (String[] args) throws Docx4JException {
       
        new OldProcessor().replaceDate();
    }
 
    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
    
      WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
    
      return template;
    
     }
    
    
    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {

        File f = new File(target);
        template.save(f);
    }

    
    private static <T> List<T> getAllElementFromObject(Object obj, Class<T> toSearch) {
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

    private void replace() {
        
        try {
            WordprocessingMLPackage template = getTemplate("template.docx");
            replacePlaceholder(template, "Fajne funkcje", "{TITLE}");
            replacePlaceholder(template, "Adi", "{NAME}");
            replaceParagraph("{PAR}", "HEHESZKI\nBardzo", template.getMainDocumentPart());
            replateTable(template);
            writeDocxToStream(template, "out.docx");
        } catch (Docx4JException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void replaceParagraph(String placeholder, String textToAdd,ContentAccessor addTo) {
 // 1. get the paragraph
        List<P> paragraphs = getAllElementFromObject(addTo, P.class);

        P toReplace = null;
        for (P p : paragraphs) {
            List<Text> texts = getAllElementFromObject(p, Text.class);
            for (Text content : texts) {
                
                if (content.getValue().equals(placeholder)) {
                    toReplace = p;
                    break;
                }
            }
        }

 // we now have the paragraph that contains our placeholder: toReplace
        // 2. split into seperate lines
        String as[] = StringUtils.splitPreserveAllTokens(textToAdd, '\n');

        for (int i = 0; i < as.length; i++) {
            String ptext = as[i];

            // 3. copy the found paragraph to keep styling correct
            P copy = (P) XmlUtils.deepCopy(toReplace);

            // replace the text elements from the copy
            List texts = getAllElementFromObject(copy, Text.class);
            if (texts.size() > 0) {
                Text textToReplace = (Text) texts.get(0);
                textToReplace.setValue(ptext);
            }

            // add the paragraph to the document
            addTo.getContent().add(copy);
        }

        // 4. remove the original one
        ((ContentAccessor) toReplace.getParent()).getContent().remove(toReplace);

    }
    
    public void replateTable(WordprocessingMLPackage template) throws Docx4JException, JAXBException{
        Map<String, String> repl1 = new HashMap<String, String>();
            repl1.put("SJ_FUNCTION", "f(x) = ax + b");
            repl1.put("SJ_DESC", "liniowa");
            repl1.put("SJ_PERIOD", "NIE");

        Map<String, String> repl2 = new HashMap<String, String>();
            repl2.put("SJ_FUNCTION", "f(x) = ax^2 + bx + c");
            repl2.put("SJ_DESC", "kwadratowa");
            repl2.put("SJ_PERIOD", "NIE");

        Map<String, String> repl3 = new HashMap<String, String>();
            repl3.put("SJ_FUNCTION", "f(x) = a * sin(bx + c)");
            repl3.put("SJ_DESC", "sinusoidalna");
            repl3.put("SJ_PERIOD", "TAK");

        replaceTable(new String[]{"SJ_FUNCTION", "SJ_DESC", "SJ_PERIOD"}, Arrays.asList(repl1, repl2, repl3), template);
    }
    
    private void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd,WordprocessingMLPackage template) 
            throws Docx4JException, JAXBException {
        List<Tbl> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

        // 1. find the table
        Tbl tempTable = getTemplateTable(tables, placeholders[0]);
        List<Tr> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = rows.get(1);

            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }
    
    private Tbl getTemplateTable(List<Tbl> tables, String templateKey) throws Docx4JException, JAXBException {
    for (Iterator<Tbl> iterator = tables.iterator(); iterator.hasNext();) {
            Tbl tbl = iterator.next();
            List<Text> textElements = getAllElementFromObject(tbl, Text.class);
            for (Text textElement : textElements) {
                
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey)) {
                    return tbl;
                }
            }
        }
        return null;
    }
    
    private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr newRow = XmlUtils.deepCopy(templateRow);
        List<Text> textElements = getAllElementFromObject(newRow, Text.class);
        
        for (Text text : textElements) {
            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue != null) {
                text.setValue(replacementValue);
            }
        }

        reviewtable.getContent().add(newRow);
    }

    private void replaceDate() {
        
        
        try {
            WordprocessingMLPackage template = getTemplate("szablony/1.Pomiary okresowe strona tytułowa - 2.docx");
            replacePlaceholder(template, "Fajne funkcje", "{DATE}");
            writeDocxToStream(template, "out.docx");
        } catch (Docx4JException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public void replace(String templateFileName, String outdocx, Map<String, String> values) {
        
        try {
            WordprocessingMLPackage template = getTemplate(templateFileName);
            for (Map.Entry<String, String> entry : values.entrySet()) {
                replacePlaceholder(template, entry.getValue(), entry.getKey());
            }
            writeDocxToStream(template, outdocx);
        } catch (Docx4JException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OldProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private void replacePlaceholder(WordprocessingMLPackage template, String value, String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
