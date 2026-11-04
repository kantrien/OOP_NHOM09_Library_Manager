package vn.edu.ptit.oop.nhom9;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import vn.edu.ptit.oop.nhom9.common.ValidationUtil;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.exception.OverdueException;
import vn.edu.ptit.oop.nhom9.model.Book;
import vn.edu.ptit.oop.nhom9.model.Magazine;
import vn.edu.ptit.oop.nhom9.model.Thesis;
import vn.edu.ptit.oop.nhom9.repository.InMemoryLibraryRepository;
import vn.edu.ptit.oop.nhom9.repository.MockDataGenerator;
import vn.edu.ptit.oop.nhom9.service.LoanService;

class LibraryManagerTest {
    @Test
    void bookHasFourteenLoanDays() {
        assertEquals(14, createBook().getLoanDays());
    }

    @Test
    void magazineHasSevenLoanDays() {
        assertEquals(7, createMagazine().getLoanDays());
    }

    @Test
    void thesisHasThreeLoanDays() {
        assertEquals(3, createThesis().getLoanDays());
    }

    @Test
    void acceptsTenDigitIsbn() {
        assertEquals("1234567890", ValidationUtil.validateISBN("1234567890"));
    }

    @Test
    void acceptsThirteenDigitIsbn() {
        assertEquals("9786041234567", ValidationUtil.validateISBN("9786041234567"));
    }

    @Test
    void rejectsInvalidIsbn() {
        assertThrows(InvalidDataException.class, () -> ValidationUtil.validateISBN("12345"));
    }

    @Test
    void rejectsFuturePublicationYear() {
        assertThrows(InvalidDataException.class,
                () -> ValidationUtil.validatePublicationYear(LocalDate.now().getYear() + 1));
    }

    @Test
    void generatorCreatesAtLeastFifteenItems() {
        assertTrue(MockDataGenerator.generateItems().size() >= 15);
    }

    @Test
    void repositoryStartsWithAtLeastFifteenItems() {
        assertTrue(new InMemoryLibraryRepository().findAll().size() >= 15);
    }

    @Test
    void borrowCalculatesDueDateFromItemLoanDays() {
        LoanService service = new LoanService();
        Book book = createBook();
        LocalDate borrowDate = LocalDate.of(2026, 1, 1);
        var record = service.borrow(book, "Nguyen Van A", "0912345678", "B12345", "D21CQCN01", borrowDate);
        assertEquals(borrowDate.plusDays(14), record.getDueDate());
    }

    @Test
    void borrowedItemBecomesUnavailable() {
        LoanService service = new LoanService();
        Book book = createBook();
        service.borrow(book, "Nguyen Van A", "0912345678", "B12345", "D21CQCN01", LocalDate.of(2026, 1, 1));
        assertFalse(book.isAvailable());
    }

    @Test
    void cannotBorrowUnavailableItemTwice() {
        LoanService service = new LoanService();
        Book book = createBook();
        LocalDate date = LocalDate.of(2026, 1, 1);
        service.borrow(book, "Nguyen Van A", "0912345678", "B12345", "D21CQCN01", date);
        assertThrows(InvalidDataException.class,
                () -> service.borrow(book, "Tran Thi B", "0912345679", "B12346", "D21CQCN01", date));
    }

    @Test
    void bookFineIsCalculatedForThreeOverdueDays() {
        assertEquals(6000, createBook().calculateFine(3));
    }

    @Test
    void magazineFineIsCalculatedForThreeOverdueDays() {
        assertEquals(3000, createMagazine().calculateFine(3));
    }

    @Test
    void negativeOverdueDaysNeverCreateAFine() {
        assertEquals(0, createBook().calculateFine(-2));
    }

    @Test
    void overdueLoanRaisesOverdueExceptionWhenValidated() {
        LoanService service = new LoanService();
        Book book = createBook();
        LocalDate borrowDate = LocalDate.of(2026, 1, 1);
        service.borrow(book, "Nguyen Van A", "0912345678", "B12345", "D21CQCN01", borrowDate);
        assertThrows(OverdueException.class,
                () -> service.validateNotOverdue(book.getId(), borrowDate.plusDays(15)));
    }

    private Book createBook() {
        return new Book("BTEST", "Test Book", "Test Author", 2024, true, "1234567890", 100);
    }

    private Magazine createMagazine() {
        return new Magazine("MTEST", "Test Magazine", "Test Author", 2024, true, "01/2024");
    }

    private Thesis createThesis() {
        return new Thesis("TTEST", "Test Thesis", "Test Author", 2024, true, "Supervisor", "University");
    }
}