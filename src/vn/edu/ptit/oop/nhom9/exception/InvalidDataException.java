package vn.edu.ptit.oop.nhom9.exception;

public class InvalidDataException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}