package vn.edu.ptit.oop.nhom9.model;

import vn.edu.ptit.oop.nhom9.common.ValidationUtil;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.service.fine.BookFine;
import vn.edu.ptit.oop.nhom9.service.fine.FineCalculator;

public class Book extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private static final FineCalculator FINE_CALCULATOR = new BookFine();

    private String isbn;
    private int pages;

    public Book(String id, String title, String author, int year, boolean available, String isbn, int pages) {
        super(id, title, author, year, available);
        setIsbn(isbn);
        setPages(pages);
    }

    public String getIsbn() {
        return isbn;
    }

    public final void setIsbn(String isbn) {
        this.isbn = ValidationUtil.validateISBN(isbn);
    }

    public int getPages() {
        return pages;
    }

    public final void setPages(int pages) {
        if (pages <= 0) {
            throw new InvalidDataException("Số trang phải lớn hơn 0.");
        }
        this.pages = pages;
    }

    @Override
    public int getLoanDays() {
        return 14;
    }

    @Override
    public String getTypeName() {
        return "Sách";
    }

    @Override
    public long calculateFine(long overdueDays) {
        return FINE_CALCULATOR.calculateFine(overdueDays);
    }

    @Override
    public String toString() {
        return super.toString() + "\nISBN: " + isbn + "\nSố trang: " + pages;
    }
}