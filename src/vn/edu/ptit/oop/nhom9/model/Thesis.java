package vn.edu.ptit.oop.nhom9.model;

import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.service.fine.FineCalculator;
import vn.edu.ptit.oop.nhom9.service.fine.ThesisFine;

public class Thesis extends LibraryItem {
    private static final long serialVersionUID = 1L;
    private static final FineCalculator FINE_CALCULATOR = new ThesisFine();

    private String supervisor;
    private String university;

    public Thesis(String id, String title, String author, int year, boolean available, String supervisor, String university) {
        super(id, title, author, year, available);
        setSupervisor(supervisor);
        setUniversity(university);
    }

    public String getSupervisor() {
        return supervisor;
    }

    public final void setSupervisor(String supervisor) {
        if (supervisor == null || supervisor.isBlank()) {
            throw new InvalidDataException("Tên người hướng dẫn không được để trống.");
        }
        this.supervisor = supervisor.trim();
    }

    public String getUniversity() {
        return university;
    }

    public final void setUniversity(String university) {
        if (university == null || university.isBlank()) {
            throw new InvalidDataException("Tên trường không được để trống.");
        }
        this.university = university.trim();
    }

    @Override
    public int getLoanDays() {
        return 3;
    }

    @Override
    public String getTypeName() {
        return "Luận văn";
    }

    @Override
    public long calculateFine(long overdueDays) {
        return FINE_CALCULATOR.calculateFine(overdueDays);
    }

    @Override
    public String toString() {
        return super.toString() + "\nNgười hướng dẫn: " + supervisor + "\nTrường: " + university;
    }
}