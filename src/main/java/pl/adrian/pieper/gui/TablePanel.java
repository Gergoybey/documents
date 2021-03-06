/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import pl.adrian.pieper.domain.TableModule;

/**
 *
 * @author Adi
 */
public class TablePanel extends javax.swing.JPanel {
    private TableModule tableModule;

    /**
     * Creates new form TablePanel
     */
    public TablePanel() {
        initComponents();
        
        rowsTextField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                tableModule.setRowsNumber(Integer.parseInt(rowsTextField.getText()));
                myTableModel.fireTableDataChanged();
            }
        
            
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rowsLabel = new javax.swing.JLabel();
        rowsTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        rowsLabel.setText("Ilosc Danych");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(rowsLabel, gridBagConstraints);

        rowsTextField.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(rowsTextField, gridBagConstraints);

        table.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setToolTipText("");
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel rowsLabel;
    private javax.swing.JTextField rowsTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    public void bind(TableModule tableModule) {
        this.tableModule = tableModule;
        myTableModel = new MyTableModel();
        myTableModel.addTableModelListener(table);
        table.setModel(myTableModel);
    }
    private MyTableModel myTableModel;
    
    class MyTableModel extends AbstractTableModel{

        @Override
        public String getColumnName(int column) {
            return TableModule.DataRow.getFieldName(column);
        }
        
        @Override
        public int getRowCount() {
            return tableModule.getSize();
        }

        @Override
        public int getColumnCount() {
            return TableModule.DataRow.getFieldsCount();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return tableModule.get(rowIndex).get(columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            
            tableModule.get(rowIndex).set(columnIndex,value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        
        
        
    }
}
