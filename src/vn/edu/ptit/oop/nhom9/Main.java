package vn.edu.ptit.oop.nhom9;

import java.io.IOException;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vn.edu.ptit.oop.nhom9.repository.InMemoryLibraryRepository;
import vn.edu.ptit.oop.nhom9.service.AppData;
import vn.edu.ptit.oop.nhom9.service.FileService;
import vn.edu.ptit.oop.nhom9.service.LibraryService;
import vn.edu.ptit.oop.nhom9.service.LoanService;
import vn.edu.ptit.oop.nhom9.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
            }
            InMemoryLibraryRepository repository = new InMemoryLibraryRepository();
            LoanService loanService = new LoanService();
            FileService persistenceService = new FileService(Path.of("data", "library-manager.dat"));
            try {
                var savedData = persistenceService.load();
                if (savedData.isPresent()) {
                    AppData data = savedData.get();
                    repository.replaceAll(data.getItems());
                    loanService.replaceAll(data.getLoans());
                }
            } catch (IOException exception) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Không thể đọc dữ liệu đã lưu. Chương trình sẽ dùng dữ liệu mẫu.\n" + exception.getMessage(),
                        "Lỗi đọc dữ liệu", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            LibraryService libraryService = new LibraryService(repository);
            MainWindow window = new MainWindow(repository, libraryService, loanService, persistenceService);
            window.setVisible(true);
        });
    }
}