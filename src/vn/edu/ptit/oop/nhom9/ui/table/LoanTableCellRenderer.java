package vn.edu.ptit.oop.nhom09.ui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import vn.edu.ptit.oop.nhom09.model.LoanRecord;

public class LoanTableCellRenderer extends DefaultTableCellRenderer {
    private final LoanTableModel tableModel;

    public LoanTableCellRenderer(LoanTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int modelRow = table.convertRowIndexToModel(row);
        LoanRecord record = tableModel.getRecordAt(modelRow);
        boolean late = record.getOverdueDays() > 0;
        if (isSelected) {
            component.setBackground(late ? new Color(239, 154, 154) : table.getSelectionBackground());
            component.setForeground(late ? new Color(100, 0, 0) : table.getSelectionForeground());
        } else if (late) {
            component.setBackground(new Color(255, 205, 210));
            component.setForeground(new Color(183, 28, 28));
        } else {
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
        }
        return component;
    }
}
