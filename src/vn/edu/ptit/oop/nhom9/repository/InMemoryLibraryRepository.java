package vn.edu.ptit.oop.nhom9.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;

public class InMemoryLibraryRepository implements LibraryRepository, Serializable {
    private static final long serialVersionUID = 1L;

    private final HashMap<String, LibraryItem> items = new HashMap<>();

    public InMemoryLibraryRepository() {
        for (LibraryItem item : MockDataGenerator.generateItems()) {
            add(item);
        }
    }

    @Override
    public synchronized void add(LibraryItem item) {
        if (item == null) {
            throw new InvalidDataException("Tài liệu không được để trống.");
        }
        if (items.containsKey(item.getId())) {
            throw new InvalidDataException("Mã tài liệu đã tồn tại.");
        }
        items.put(item.getId(), item);
    }

    @Override
    public synchronized boolean remove(String id) {
        return items.remove(id) != null;
    }

    @Override
    public synchronized Optional<LibraryItem> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public synchronized List<LibraryItem> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public synchronized List<LibraryItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String normalized = keyword.trim().toLowerCase(Locale.ROOT);
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.getTitle().toLowerCase(Locale.ROOT).contains(normalized)
                    || item.getAuthor().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(item);
            }
        }
        return result;
    }

    public synchronized Map<String, LibraryItem> exportItems() {
        return new HashMap<>(items);
    }

    public synchronized void replaceAll(Map<String, LibraryItem> source) {
        items.clear();
        if (source != null) {
            for (Map.Entry<String, LibraryItem> entry : source.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    items.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }


}