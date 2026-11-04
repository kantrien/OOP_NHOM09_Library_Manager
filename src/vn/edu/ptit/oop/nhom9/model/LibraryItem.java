package vn.edu.ptit.oop.nhom9.model;

import java.io.Serializable;
import java.util.Objects;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.common.ValidationUtil;

public abstract class LibraryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String author;
    private int year;
    private boolean available;

    protected LibraryItem(String id, String title, String author, int year, boolean available) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setYear(year);
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Mã tài liệu không được để trống.");
        }
        this.id = id.trim();
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidDataException("Tên tài liệu không được để trống.");
        }
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public final void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new InvalidDataException("Tác giả không được để trống.");
        }
        this.author = author.trim();
    }

    public int getYear() {
        return year;
    }

    public final void setYear(int year) {
        this.year = ValidationUtil.validatePublicationYear(year);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract int getLoanDays();

    public abstract String getTypeName();

    public abstract long calculateFine(long overdueDays);

    @Override
    public String toString() {
        return "Mã: " + id + "\nLoại: " + getTypeName() + "\nTên: " + title + "\nTác giả: " + author + "\nNăm xuất bản: " + year + "\nTình trạng: " + (available ? "Có sẵn" : "Đang được mượn");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LibraryItem that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}