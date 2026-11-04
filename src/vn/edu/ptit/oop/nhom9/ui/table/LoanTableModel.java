package vn.edu.ptit.oop.nhom9.ui.table;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class LoanTableModel extends AbstractTableModel {
    private final String[] columns = {
        "Mã tài liệu", "Tên tài liệu", "Người mượn", "Số điện thoại", "MSSV", "Lớp",
        "Ngày mượn", "Hạn trả", "Ngày trả", "Quá hạn (ngày)", "Tiền phạt", "Trạng thái"
    };
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));
    private List<LoanRecord> records = new ArrayList<>();

    public void setRecords(List<LoanRecord> records) {
        this.records = new ArrayList<>(records);
        fireTableDataChanged();
    }

    public LoanRecord getRecordAt(int row) {
        return records.get(row);
    }

    @Override
    public int getRowCount() {
        return records.size();
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
        LoanRecord record = records.get(rowIndex);
        LocalDate returnDate = record.getReturnDate();
        long overdueDays = record.getOverdueDays();
        String status;
        if (record.isReturned() && overdueDays > 0) {
            status = "Đã trả trễ";
        } else if (record.isReturned()) {
            status = "Đã trả";
        } else if (record.isOverdue(LocalDate.now())) {
            status = "Quá hạn";
        } else {
            status = "Đang mượn";
        }
        return switch (columnIndex) {
            case 0 -> record.getItem().getId();
            case 1 -> record.getItem().getTitle();
            case 2 -> record.getBorrowerName();
            case 3 -> record.getBorrowerPhone();
            case 4 -> record.getStudentId();
            case 5 -> record.getClassName();
            case 6 -> formatter.format(record.getBorrowDate());
            case 7 -> formatter.format(record.getDueDate());
            case 8 -> returnDate == null ? "—" : formatter.format(returnDate);
            case 9 -> overdueDays;
            case 10 -> currencyFormatter.format(record.getFineAmount()) + " đ";
            case 11 -> status;
            default -> "";
        };
    }
}