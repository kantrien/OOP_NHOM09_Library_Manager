package vn.edu.ptit.oop.nhom9.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final HashMap<String, LibraryItem> items;
    private final ArrayList<LoanRecord> loans;

    public AppData(Map<String, LibraryItem> items, List<LoanRecord> loans) {
        this.items = new HashMap<>(items);
        this.loans = new ArrayList<>(loans);
    }

    public Map<String, LibraryItem> getItems() {
        return new HashMap<>(items);
    }

    public List<LoanRecord> getLoans() {
        return new ArrayList<>(loans);
    }
}