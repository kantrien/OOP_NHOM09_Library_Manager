package vn.edu.ptit.oop.nhom9.repository;

import java.util.List;
import java.util.Optional;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;

public interface LibraryRepository {
    void add(LibraryItem item);

    boolean remove(String id);

    Optional<LibraryItem> findById(String id);

    List<LibraryItem> findAll();

    List<LibraryItem> search(String keyword);
}