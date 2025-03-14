import util.Connect;
import util.ContactData;
import util.SelectResponse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhoneBook extends JFrame{
    private JPanel panel;
    private JTable phoneTable;
    private JButton addButton;
    private JButton deleteButton;

    private final DefaultTableModel tableModel;
    private Object[] columnIdentifiers;

    public PhoneBook(){
        setSize(1000, 1000);
        setVisible(true);
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();

        refreshTable();

        deleteButton.addActionListener(e -> {
            int row = phoneTable.getSelectedRow();

            if (row < 0) return;

            int index = (phoneTable.getColumnModel().getColumnIndex(columnIdentifiers[0]));
            int contactId = Integer.parseInt(phoneTable.getValueAt(row, index).toString());
            Connect.removeContact(contactId);
            refreshTable();
        });

        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Contact Name");
            String phone = JOptionPane.showInputDialog("Enter Contact Phone Number");
            String email = JOptionPane.showInputDialog("Enter Contact Email Address");
            String address = JOptionPane.showInputDialog("Enter Contact Address");

            if(ContactData.isValid(name, phone, email, address)){
                Connect.addContact(name, phone, email, address);
                refreshTable();
            } else{
                JOptionPane.showMessageDialog(null, "Invalid Contact Info");
            }
        });

        phoneTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }

            @Override
            public boolean stopCellEditing() {
                String newValue = getCellEditorValue().toString();

                int row = phoneTable.getEditingRow();

                int column = phoneTable.getEditingColumn();

                String columnName = phoneTable.getColumnName(column);

                int index = (phoneTable.getColumnModel().getColumnIndex(columnIdentifiers[0]));
                String contactId = (phoneTable.getValueAt(row, index).toString());

                if(columnName.equals("id")) return super.stopCellEditing();

                Connect.updateContact(contactId, columnName, newValue);
                refreshTable();

                return super.stopCellEditing();
            }
        });
    }

    private void refreshTable(){
        String query = "select * from sakila.contact";
        SelectResponse sr = Connect.executeSelectQuery(query);

        columnIdentifiers = sr.columns.toArray(new String[0]);

        tableModel.setColumnIdentifiers(columnIdentifiers);
        phoneTable.setModel(tableModel);

        tableModel.setRowCount(0);
        for(String[] contact : sr.rows){
            tableModel.addRow(contact);
        }
    }
}
