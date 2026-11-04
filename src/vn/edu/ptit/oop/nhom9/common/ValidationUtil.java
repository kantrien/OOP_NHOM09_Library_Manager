package vn.edu.ptit.oop.nhom9.common;

import java.time.Year;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static String validateISBN(String isbn) {
        if (isbn == null || !isbn.matches("(?:[0-9]{10}|[0-9]{13})")) {
            throw new InvalidDataException("ISBN phải gồm đúng 10 hoặc 13 chữ số.");
        }
        return isbn;
    }

    public static int validatePublicationYear(int year) {
        if (year < 1900 || year > Year.now().getValue()) {
            throw new InvalidDataException("Năm xuất bản phải từ 1900 đến " + Year.now().getValue() + ".");
        }
        return year;
    }
}