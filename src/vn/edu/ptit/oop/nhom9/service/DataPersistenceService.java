package vn.edu.ptit.oop.nhom9.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DataPersistenceService {
    private final Path filePath;

    public DataPersistenceService(Path filePath) {
        this.filePath = filePath;
    }

    public void save(AppData data) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            output.writeObject(data);
        }
    }

    public Optional<AppData> load() throws IOException {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(filePath))) {
            Object value = input.readObject();
            if (!(value instanceof AppData data)) {
                throw new IOException("Tệp dữ liệu không đúng định dạng của chương trình.");
            }
            return Optional.of(data);
        } catch (ClassNotFoundException exception) {
            throw new IOException("Không thể đọc lớp dữ liệu đã lưu.", exception);
        }
    }
}