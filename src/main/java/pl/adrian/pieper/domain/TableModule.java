/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.docx4j.XmlUtils;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;
import static pl.adrian.pieper.domain.ProcessFile.getAllElementFromObject;
import static pl.adrian.pieper.domain.ProcessFile.replacePlaceholderIn;

/**
 *
 * @author Adi
 */
public class TableModule extends ProcessorModule{

    private int rows = 0;
    private List<DataRow> data = new ArrayList<>();

    @Override
    public Processor getProcessor() {
        return new TableProc();
    }

    @Override
    public void attach(Gui gui) {
        gui.attach(this);
    }

    public int getSize() {
        return rows;
    }

    public void setRowsNumber(int i){
        rows = i;
        while (data.size() < rows){
            data.add(new DataRow(data.size()));
        }
    }
    
    public DataRow get(int rowIndex) {
        return data.get(rowIndex);
    }
    
    
    public static class DataRow{

        private String name;
        private int number;
        private int ln;
        private int la;
        private double zs;
        private double zdop;
        
        public DataRow(int number) {
            this("Gniazdo",number,16,80,0.45,2.87);
        }
        
        public DataRow(String name, int number, int ln,int la, double zs, double zdop) {
            this.name = name;
            this.number = number;
            this.ln = ln;
            this.la = la;
            this.zs = zs;
            this.zdop = zdop;
        }
        
        private static final String NAMES[] = {"Nazwa", "Numer", "Ln", "Zs", "Zdop"};
        
        public static String getFieldName(int column) {
            return NAMES[column];
        }

        
        public static int getFieldsCount() {
            return NAMES.length;
        }

        public Object get(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return name;
                case 1:
                    return number;
                case 2:
                    return ln;
                case 3:
                    return la;
                case 4:
                    return zs;
                case 5:
                    return zdop;
            }
            return "?";
        }

        public void set(int columnIndex, Object value) {
            try{
                switch (columnIndex) {
                    case 0:
                        name = value.toString();
                        break;
                    case 1:
                        number = Integer.parseInt(value.toString());
                        break;
                    case 2:
                        ln = Integer.parseInt(value.toString());
                        break;
                    case 3:
                        la = Integer.parseInt(value.toString());
                        break;
                    case 4:
                        zs = Double.parseDouble(value.toString());
                        break;
                    case 5:
                        zdop = Double.parseDouble(value.toString());
                        break;
                }
            }catch(Exception e){
                
            }
        }

        
    }
    
    public class TableProc implements Processor {

        private final int rowsOnFirstPage = 18;
        private final int rowsPerPage = 36;
        DecimalFormat df = new DecimalFormat("#.##");
        Map<String, String> map = new HashMap<>();
            
        @Override
        public void process(ProcessFile processFile) {
            Tbl workingTable = processFile.getTemplateTable("_MNAME_");
            if (workingTable != null) {
                Tbl tableCopy = null;
                List<Tr> tableRows = getAllElementFromObject(workingTable, Tr.class);
                Tr tempRow = tableRows.get(3);
                workingTable.getContent().remove(tempRow);
                if (rows > rowsOnFirstPage){
                    tableCopy = XmlUtils.deepCopy(workingTable);
                }
                
                int end = Math.min(rowsOnFirstPage, rows);
                int offset = 0;
                int tableLP = 1;
                while(true){
                    replacePlaceholderIn(workingTable,"_TN_",Integer.toString(tableLP++));
                    writeTo(workingTable, tempRow, offset, end);
                    offset = end;
                    end = Math.min(offset + rowsPerPage, rows);
                    if (offset != end){
                        workingTable = XmlUtils.deepCopy(tableCopy);
                        processFile.nextPage();
                        processFile.add(workingTable);
                    }else{
                        break;
                    }
                }
            }
        }
        
        private void writeTo(Tbl workingTable,Tr tempRow,int offset,int end){
            
            int lp = 1;
            for (int i = offset; i < end; i++) {
                DataRow row = data.get(i);
                map.put("_LP_", Integer.toString(lp));
                map.put("_MNAME_", row.name);
                map.put("_NUM_", Integer.toString(row.number));
                map.put("_LA_", Integer.toString(row.la));
                map.put("_LN_", Integer.toString(row.ln));
                map.put("_ZS_", df.format(row.zs));
                map.put("_ZDOP_", df.format(row.zdop));
                ProcessFile.addRowToTable(workingTable, tempRow, map, 2 + lp);
                lp++;
            }
        }
    }
}
