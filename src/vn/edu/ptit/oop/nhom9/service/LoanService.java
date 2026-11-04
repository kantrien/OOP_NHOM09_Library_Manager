package vn.edu.ptit.oop.nhom9.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.exception.ItemNotFoundException;
import vn.edu.ptit.oop.nhom9.exception.OverdueException;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class LoanService {
    private final List<LoanRecord> loanRecords = new ArrayList<>();

    public synchronized LoanRecord borrow(LibraryItem item, String borrowerName, LocalDate borrowDate) {
        return borrow(item, borrowerName, "Chưa cập nhật", "Chưa cập nhật", "Chưa cập nhật", borrowDate);
    }

    public synchronized LoanRecord borrow(LibraryItem item, String borrowerName, String borrowerPhone,
            String studentId, String className, LocalDate borrowDate) {
        Objects.requireNonNull(item, "Tài liệu không được để trống.");
        Objects.requireNonNull(borrowDate, "Ngày mượn không được để trống.");
        if (!item.isAvailable()) {
            throw new InvalidDataException("Tài liệu hiện không có sẵn để mượn.");
        }
        LocalDate dueDate = borrowDate.plusDays(item.getLoanDays());
        LoanRecord record = new LoanRecord(item, borrowerName, borrowerPhone, studentId,
                className, borrowDate, dueDate, null);
        item.setAvailable(false);
        loanRecords.add(record);
        return record;
    }

    public synchronized void returnItem(String itemId, LocalDate returnDate) {
        Objects.requireNonNull(returnDate, "Ngày trả không được để trống.");
        LoanRecord record = findActiveLoan(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy lượt mượn đang hoạt động của tài liệu."));
        if (returnDate.isBefore(record.getBorrowDate())) {
            throw new InvalidDataException("Ngày trả không được trước ngày mượn.");
        }
        record.setReturnDate(returnDate);
        record.getItem().setAvailable(true);
    }

    public synchronized Optional<LoanRecord> findActiveLoan(String itemId) {
        return loanRecords.stream()
                .filter(record -> record.getItem().getId().equals(itemId) && !record.isReturned())
                .findFirst();
    }

    public synchronized boolean isOverdue(String itemId, LocalDate date) {
        return findActiveLoan(itemId).map(record -> record.isOverdue(date)).orElse(false);
    }


    public synchronized void validateNotOverdue(String itemId, LocalDate date) {
        Objects.requireNonNull(date, "Ngày kiểm tra không được để trống.");
        LoanRecord record = findActiveLoan(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy lượt mượn đang hoạt động của tài liệu."));
        if (record.isOverdue(date)) {
            throw new OverdueException("Tài liệu đã quá hạn trả.");
        }
    }

    public synchronized List<LoanRecord> getLoanRecords() {
        return new ArrayList<>(loanRecords);
    }

    public synchronized void replaceAll(List<LoanRecord> records) {
        loanRecords.clear();
        if (records != null) {
            loanRecords.addAll(records);
        }
    }
}