package vn.edu.ptit.oop.nhom9.service;

import java.util.List;
import java.util.Objects;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.exception.ItemNotFoundException;
import vn.edu.ptit.oop.nhom9.model.Book;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.Magazine;
import vn.edu.ptit.oop.nhom9.model.Thesis;
import vn.edu.ptit.oop.nhom9.repository.LibraryRepository;

public class LibraryService {
    private final LibraryRepository repository;

    public LibraryService(LibraryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public void addItem(LibraryItem item) {
        repository.add(item);
    }

    public void updateItem(String id, LibraryItem updatedItem) {
        LibraryItem current = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy tài liệu cần cập nhật."));
        if (!id.equals(updatedItem.getId())) {
            throw new InvalidDataException("Không được thay đổi mã tài liệu khi cập nhật.");
        }
        if (current.getClass() != updatedItem.getClass()) {
            throw new InvalidDataException("Không được thay đổi loại tài liệu khi cập nhật.");
        }
        current.setTitle(updatedItem.getTitle());
        current.setAuthor(updatedItem.getAuthor());
        current.setYear(updatedItem.getYear());
        if (current instanceof Book currentBook && updatedItem instanceof Book updatedBook) {
            currentBook.setIsbn(updatedBook.getIsbn());
            currentBook.setPages(updatedBook.getPages());
        } else if (current instanceof Magazine currentMagazine && updatedItem instanceof Magazine updatedMagazine) {
            currentMagazine.setIssueNumber(updatedMagazine.getIssueNumber());
        } else if (current instanceof Thesis currentThesis && updatedItem instanceof Thesis updatedThesis) {
            currentThesis.setSupervisor(updatedThesis.getSupervisor());
            currentThesis.setUniversity(updatedThesis.getUniversity());
        }
    }

    public void removeItem(String id) {
        if (!repository.remove(id)) {
            throw new ItemNotFoundException("Không tìm thấy tài liệu cần xóa.");
        }
    }

    public LibraryItem findItem(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy tài liệu."));
    }

    public List<LibraryItem> getAllItems() {
        return repository.findAll();
    }

    public List<LibraryItem> searchItems(String keyword) {
        return repository.search(keyword);
    }
}