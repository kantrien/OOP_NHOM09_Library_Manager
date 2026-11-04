package vn.edu.ptit.oop.nhom9.exception;

public class OverdueException extends InvalidDataException {
    private static final long serialVersionUID = 1L;

    public OverdueException(String message) {
        super(message);
    }
}