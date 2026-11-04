package vn.edu.ptit.oop.nhom9.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;

public class LoanRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LibraryItem item;
    private final String borrowerName;
    private final String borrowerPhone;
    private final String studentId;
    private final String className;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public LoanRecord(LibraryItem item, String borrowerName, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this(item, borrowerName, "Chưa cập nhật", "Chưa cập nhật", "Chưa cập nhật", borrowDate, dueDate, returnDate);
    }

    public LoanRecord(LibraryItem item, String borrowerName, String borrowerPhone, String studentId,
            String className, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.item = Objects.requireNonNull(item, "Tài liệu không được để trống.");
        if (borrowerName == null || borrowerName.isBlank()) {
            throw new InvalidDataException("Tên người mượn không được để trống.");
        }
        this.borrowerName = borrowerName.trim();
        this.borrowerPhone = validatePhone(borrowerPhone);
        this.studentId = validateRequired(studentId, "MSSV");
        this.className = validateRequired(className, "Lớp");
        this.borrowDate = Objects.requireNonNull(borrowDate, "Ngày mượn không được để trống.");
        this.dueDate = Objects.requireNonNull(dueDate, "Ngày phải trả không được để trống.");
        if (dueDate.isBefore(borrowDate)) {
            throw new InvalidDataException("Ngày phải trả không được trước ngày mượn.");
        }
        if (returnDate != null && returnDate.isBefore(borrowDate)) {
            throw new InvalidDataException("Ngày trả không được trước ngày mượn.");
        }
        this.returnDate = returnDate;
    }

    private static String validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

private static String validatePhone(String value) {
    if (value == null || value.isBlank()) {
        throw new InvalidDataException("Số điện thoại không được để trống.");
    }
    if ("Chưa cập nhật".equals(value.trim())) {
        return value.trim();
    }
    String normalized = value.replaceAll("[\\s.-]", "");
    if (!normalized.startsWith("0") && !normalized.startsWith("+84")) {
        throw new InvalidDataException("Số điện thoại phải bắt đầu bằng 0 hoặc +84.");
    }
    String digits = normalized.startsWith("+84") ? "0" + normalized.substring(3) : normalized;

if (!digits.matches("\\d+")) {
    throw new InvalidDataException("Số điện thoại chỉ được nhập chữ số.");
}
if (digits.length() > 10) {
    throw new InvalidDataException("Bạn đã nhập quá 10 số. Số điện thoại chỉ gồm đúng 10 số.");
}
if (digits.length() < 10) {
    throw new InvalidDataException("Số điện thoại chưa đủ 10 số.");
}
return normalized;
}
    public LibraryItem getItem() {
        return item;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBorrowerPhone() {
        return borrowerPhone == null || borrowerPhone.isBlank() ? "Chưa cập nhật" : borrowerPhone;
    }

    public String getStudentId() {
        return studentId == null || studentId.isBlank() ? "Chưa cập nhật" : studentId;
    }

    public String getClassName() {
        return className == null || className.isBlank() ? "Chưa cập nhật" : className;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        if (returnDate != null && returnDate.isBefore(borrowDate)) {
            throw new InvalidDataException("Ngày trả không được trước ngày mượn.");
        }
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate date) {
        return !isReturned() && date.isAfter(dueDate);
    }

    public long getOverdueDays() {
        LocalDate effectiveDate = returnDate == null ? LocalDate.now() : returnDate;
        if (!effectiveDate.isAfter(dueDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, effectiveDate);
    }

    public long getFineAmount() {
        return item.calculateFine(getOverdueDays());
    }

    public boolean wasReturnedLate() {
        return isReturned() && getOverdueDays() > 0;
    }
}