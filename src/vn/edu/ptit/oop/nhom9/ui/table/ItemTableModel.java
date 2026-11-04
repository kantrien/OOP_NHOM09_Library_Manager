package vn.edu.ptit.oop.nhom9.ui.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;

public class ItemTableModel extends AbstractTableModel {
    private final String[] columns = {"Mã", "Loại", "Tên tài liệu", "Tác giả", "Năm xuất bản", "Tình trạng"};
    private List<LibraryItem> items = new ArrayList<>();

    public void setItems(List<LibraryItem> items) {
        this.items = new ArrayList<>(items);
        fireTableDataChanged();
    }

    public LibraryItem getItemAt(int row) {
        return items.get(row);
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LibraryItem item = items.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> item.getId();
            case 1 -> item.getTypeName();
            case 2 -> item.getTitle();
            case 3 -> item.getAuthor();
            case 4 -> item.getYear();
            case 5 -> item.isAvailable() ? "Có sẵn" : "Đang mượn";
            default -> "";
        };
    }
}