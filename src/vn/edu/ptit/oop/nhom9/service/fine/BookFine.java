package vn.edu.ptit.oop.nhom09.service.fine;

public class BookFine implements FineCalculator {
    private static final long FINE_PER_DAY = 2000L;

    @Override
    public long calculateFine(long overdueDays) {
        return Math.max(0, overdueDays) * FINE_PER_DAY;
    }
}
