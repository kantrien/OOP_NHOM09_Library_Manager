package vn.edu.ptit.oop.nhom9.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class FileService {
    private final DataPersistenceService persistenceService;

    public FileService(Path filePath) {
        persistenceService = new DataPersistenceService(filePath);
    }

    public void save(AppData data) throws IOException {
        persistenceService.save(data);
    }

    public Optional<AppData> load() throws IOException {
        return persistenceService.load();
    }
}