package vn.edu.ptit.oop.nhom09.service.fine;

public class ThesisFine implements FineCalculator {
    private static final long FINE_PER_DAY = 3000L;

    @Override
    public long calculateFine(long overdueDays) {
        return Math.max(0, overdueDays) * FINE_PER_DAY;
    }
}
