package vn.edu.ptit.oop.nhom9.exception;

public class ItemNotFoundException extends InvalidDataException {
    private static final long serialVersionUID = 1L;

    public ItemNotFoundException(String message) {
        super(message);
    }
}