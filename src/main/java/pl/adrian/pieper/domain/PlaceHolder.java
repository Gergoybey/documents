/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adi
 */
@XStreamAlias("pole")
public class PlaceHolder {
    @XStreamAlias("nazwa")
    private String name;
    @XStreamAsAttribute
    @XStreamAlias("etykieta")
    private String placeholder;
    @XStreamAlias("wartosc")
    private String value;

    public PlaceHolder(String name, String placeholder, String value) {
        this.name = name;
        this.placeholder = placeholder;
        this.value = value;
    }

    public PlaceHolder(String name, String placeholder) {
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

    public static PlaceHolder parse(String string){
        String[] scValues = string.split(";");
        if (scValues.length == 3)
            return new PlaceHolder(scValues[0], scValues[1], scValues[2]);
        else
            return new PlaceHolder(scValues[0], scValues[1]);
    }
    
    public static List<PlaceHolder> parseFile(String filename){
        Scanner scanner = null;
        List<PlaceHolder> data = new ArrayList<>();
        
        try {
            scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                if (!line.isEmpty())
                data.add(parse(line));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlaceHolder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return data;
    }
    
    
    
    public static void main(String[] args) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(PlaceHolder.class);
        xstream.toXML(new PlaceHolder("Ulica", "{STREET}", "DEF VALUE"), System.out);
    }

    public void setDefault() {
        if (name == null)
            name = placeholder;
        if (value == null)
            value = name;
    }
    
    
}
