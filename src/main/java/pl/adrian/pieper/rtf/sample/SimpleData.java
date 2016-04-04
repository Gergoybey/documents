/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.rtf.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adi
 */
public class SimpleData {
    private String name;
    private String placeholder;
    private String value;

    public SimpleData(String name, String placeholder, String value) {
        this.name = name;
        this.placeholder = placeholder;
        this.value = value;
    }

    public SimpleData(String name, String placeholder) {
        this(name, placeholder, name);
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    

    public static SimpleData parse(String string){
        String[] scValues = string.split(";");
        if (scValues.length == 3)
            return new SimpleData(scValues[0], scValues[1], scValues[2]);
        else
            return new SimpleData(scValues[0], scValues[1]);
    }
    
    public static List<SimpleData> parseFile(String filename){
        Scanner scanner = null;
        List<SimpleData> data = new ArrayList<>();
        
        try {
            scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                if (!line.isEmpty())
                data.add(parse(line));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return data;
    }
    
    
    
    
    
    
}
