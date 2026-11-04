package vn.edu.ptit.oop.nhom9.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class LoanCsvExportService {
    public void export(List<LoanRecord> records, Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write('\uFEFF');
            writer.write("Mã tài liệu,Tên tài liệu,Loại tài liệu,Người mượn,Số điện thoại,MSSV,Lớp,Ngày mượn,Hạn trả,Ngày trả,Số ngày quá hạn,Tiền phạt (VND),Trạng thái");
            writer.newLine();
            for (LoanRecord record : records) {
                long overdueDays = record.getOverdueDays();
                String status;
                if (record.isReturned() && overdueDays > 0) {
                    status = "Đã trả trễ";
                } else if (record.isReturned()) {
                    status = "Đã trả";
                } else if (overdueDays > 0) {
                    status = "Quá hạn";
                } else {
                    status = "Đang mượn";
                }
                String[] values = {
                    record.getItem().getId(),
                    record.getItem().getTitle(),
                    record.getItem().getTypeName(),
                    record.getBorrowerName(),
                    record.getBorrowerPhone(),
                    record.getStudentId(),
                    record.getClassName(),
                    record.getBorrowDate().toString(),
                    record.getDueDate().toString(),
                    record.getReturnDate() == null ? "" : record.getReturnDate().toString(),
                    Long.toString(overdueDays),
                    Long.toString(record.getFineAmount()),
                    status
                };
                for (int index = 0; index < values.length; index++) {
                    if (index > 0) {
                        writer.write(',');
                    }
                    writer.write(csv(values[index]));
                }
                writer.newLine();
            }
        }
    }

    private String csv(String value) {
        String safeValue = value == null ? "" : value;
        if (!safeValue.isEmpty() && "=+-@".indexOf(safeValue.charAt(0)) >= 0) {
            safeValue = "'" + safeValue;
        }
        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }
}