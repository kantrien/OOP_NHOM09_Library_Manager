package vn.edu.ptit.oop.nhom9.model;

import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.service.fine.FineCalculator;
import vn.edu.ptit.oop.nhom9.service.fine.MagazineFine;

public class Magazine extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private static final FineCalculator FINE_CALCULATOR = new MagazineFine();

    private String issueNumber;

    public Magazine(String id, String title, String author, int year, boolean available, String issueNumber) {
        super(id, title, author, year, available);
        setIssueNumber(issueNumber);
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public final void setIssueNumber(String issueNumber) {
        if (issueNumber == null || issueNumber.isBlank()) {
            throw new InvalidDataException("Số phát hành không được để trống.");
        }
        this.issueNumber = issueNumber.trim();
    }

    @Override
    public int getLoanDays() {
        return 7;
    }

    @Override
    public String getTypeName() {
        return "Tạp chí";
    }

    @Override
    public long calculateFine(long overdueDays) {
        return FINE_CALCULATOR.calculateFine(overdueDays);
    }

    @Override
    public String toString() {
        return super.toString() + "\nSố phát hành: " + issueNumber;
    }
}