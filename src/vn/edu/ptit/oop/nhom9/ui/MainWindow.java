package vn.edu.ptit.oop.nhom9.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.filechooser.FileNameExtensionFilter;
import vn.edu.ptit.oop.nhom9.common.BaseWindow;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;
import vn.edu.ptit.oop.nhom9.repository.InMemoryLibraryRepository;
import vn.edu.ptit.oop.nhom9.service.AppData;
import vn.edu.ptit.oop.nhom9.service.FileService;
import vn.edu.ptit.oop.nhom9.service.LibraryService;
import vn.edu.ptit.oop.nhom9.service.LoanCsvExportService;
import vn.edu.ptit.oop.nhom9.service.LoanService;
import vn.edu.ptit.oop.nhom9.ui.table.ItemTableModel;
import vn.edu.ptit.oop.nhom9.ui.table.LoanTableCellRenderer;
import vn.edu.ptit.oop.nhom9.ui.table.LoanTableModel;

public class MainWindow extends BaseWindow {
    private final InMemoryLibraryRepository repository;
    private final LibraryService libraryService;
    private final LoanService loanService;
    private final FileService persistenceService;
    private final ItemTableModel itemTableModel = new ItemTableModel();
    private final LoanTableModel loanTableModel = new LoanTableModel();
    private final JTable itemTable = new JTable(itemTableModel);
    private final JTable loanTable = new JTable(loanTableModel);
    private final TableRowSorter<ItemTableModel> itemSorter = new TableRowSorter<>(itemTableModel);
    private final JTextField searchField = new JTextField(28);
    private final JTextArea detailArea = new JTextArea();
    private final JLabel statusLabel = new JLabel("Sẵn sàng");

    public MainWindow(InMemoryLibraryRepository repository, LibraryService libraryService,
            LoanService loanService, FileService persistenceService) {
        super("Nhóm 9 - Quản lý Thư viện", 1150, 720);
        this.repository = repository;
        this.libraryService = libraryService;
        this.loanService = loanService;
        this.persistenceService = persistenceService;
        initUI();
        refreshAll();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveData(false);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        add(createHeader(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Quản lý tài liệu", createItemsPanel());
        tabs.addTab("Mượn - trả", createLoansPanel());
        add(tabs, BorderLayout.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 8, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(8, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 6, 12));
        JLabel title = new JLabel("LIBRARY MANAGER - QUẢN LÝ THƯ VIỆN");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);
        JPanel actions = new JPanel();
        JButton addButton = new JButton("Thêm tài liệu");
        JButton editButton = new JButton("Sửa tài liệu");
        JButton deleteButton = new JButton("Xóa tài liệu");
        JButton borrowButton = new JButton("Mượn tài liệu");
        JButton returnButton = new JButton("Trả tài liệu");
        JButton chartButton = new JButton("Biểu đồ");
        JButton saveButton = new JButton("Lưu dữ liệu");
        JButton exportLoansButton = new JButton("Xuất DS mượn - trả");
        JButton aboutButton = new JButton("Thông tin nhóm");
        addButton.addActionListener(event -> addItem());
        editButton.addActionListener(event -> editItem());
        deleteButton.addActionListener(event -> deleteItem());
        borrowButton.addActionListener(event -> borrowItem());
        returnButton.addActionListener(event -> returnItem());
        chartButton.addActionListener(event -> openCharts());
        saveButton.addActionListener(event -> saveData(true));
        exportLoansButton.addActionListener(event -> exportLoanData());
        aboutButton.addActionListener(event -> new AboutDialog(this).setVisible(true));
        actions.add(addButton);
        actions.add(editButton);
        actions.add(deleteButton);
        actions.add(borrowButton);
        actions.add(returnButton);
        actions.add(chartButton);
        actions.add(saveButton);
        actions.add(exportLoansButton);
        actions.add(aboutButton);
        panel.add(actions, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchPanel.add(new JLabel("Tìm theo tên tài liệu hoặc tác giả:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchField.setToolTipText("Nhập tên tài liệu hoặc tác giả để tìm kiếm");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                applySearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                applySearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                applySearchFilter();
            }
        });
        itemTable.setRowSorter(itemSorter);
        itemTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemTable.setAutoCreateRowSorter(false);
        itemTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                showSelectedItemDetails();
            }
        });
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setPreferredSize(new Dimension(330, 400));
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết tài liệu đang chọn"));
        detailPanel.add(detailScroll, BorderLayout.CENTER);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(itemTable), detailPanel);
        splitPane.setResizeWeight(0.70);
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JLabel instruction = new JLabel("Thông tin người mượn gồm họ tên, số điện thoại, MSSV và lớp. Lượt quá hạn được tô đỏ, kèm số ngày và tiền phạt.");
        instruction.setBorder(BorderFactory.createEmptyBorder(7, 10, 4, 10));
        loanTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        loanTable.setDefaultRenderer(Object.class, new LoanTableCellRenderer(loanTableModel));
        loanTable.setRowHeight(25);
        loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] loanColumnWidths = {95, 190, 140, 115, 110, 90, 95, 95, 95, 110, 110, 95};
        for (int index = 0; index < loanColumnWidths.length; index++) {
            loanTable.getColumnModel().getColumn(index).setPreferredWidth(loanColumnWidths[index]);
        }
        panel.add(instruction, BorderLayout.NORTH);
        panel.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        return panel;
    }

    private void applySearchFilter() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            itemSorter.setRowFilter(null);
        } else {
            itemSorter.setRowFilter(RowFilter.regexFilter("(?iu)" + Pattern.quote(text), 2, 3));
        }
    }

    private LibraryItem getSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài liệu.", "Chưa chọn tài liệu", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int modelRow = itemTable.convertRowIndexToModel(selectedRow);
        return itemTableModel.getItemAt(modelRow);
    }

    private LoanRecord getSelectedLoan() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lượt mượn.", "Chưa chọn lượt mượn", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return loanTableModel.getRecordAt(loanTable.convertRowIndexToModel(selectedRow));
    }

    private void addItem() {
        ItemFormDialog dialog = new ItemFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.getResult() != null) {
            try {
                libraryService.addItem(dialog.getResult());
                refreshAll();
                saveData(false);
                statusLabel.setText("Đã thêm tài liệu " + dialog.getResult().getId());
            } catch (IllegalArgumentException exception) {
                showError(exception.getMessage());
            }
        }
    }

    private void editItem() {
        LibraryItem selected = getSelectedItem();
        if (selected == null) {
            return;
        }
        ItemFormDialog dialog = new ItemFormDialog(this, selected);
        dialog.setVisible(true);
        if (dialog.getResult() != null) {
            try {
                libraryService.updateItem(selected.getId(), dialog.getResult());
                refreshAll();
                saveData(false);
                statusLabel.setText("Đã cập nhật tài liệu " + selected.getId());
            } catch (IllegalArgumentException exception) {
                showError(exception.getMessage());
            }
        }
    }

    private void deleteItem() {
        LibraryItem selected = getSelectedItem();
        if (selected == null) {
            return;
        }
        if (loanService.findActiveLoan(selected.getId()).isPresent()) {
            showError("Không thể xóa tài liệu đang được mượn.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa tài liệu " + selected.getTitle() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                libraryService.removeItem(selected.getId());
                refreshAll();
                saveData(false);
            } catch (IllegalArgumentException exception) {
                showError(exception.getMessage());
            }
        }
    }

    private void borrowItem() {
        LibraryItem selected = getSelectedItem();
        if (selected == null) {
            return;
        }
        if (!selected.isAvailable()) {
            showError("Tài liệu này đang được mượn.");
            return;
        }
        LoanDialog dialog = new LoanDialog(this, selected, null, true);
        dialog.setVisible(true);
        if (dialog.isAccepted()) {
            try {
                loanService.borrow(selected, dialog.getBorrowerName(), dialog.getBorrowerPhone(),
                        dialog.getStudentId(), dialog.getClassName(), dialog.getSelectedDate());
                refreshAll();
                saveData(false);
                statusLabel.setText("Đã tạo lượt mượn cho " + selected.getTitle());
            } catch (IllegalArgumentException exception) {
                showError(exception.getMessage());
            }
        }
    }

    private void returnItem() {
        LoanRecord selected = getSelectedLoan();
        if (selected == null) {
            return;
        }
        if (selected.isReturned()) {
            showError("Lượt mượn này đã được trả trước đó.");
            return;
        }
        LoanDialog dialog = new LoanDialog(this, selected.getItem(), selected, false);
        dialog.setVisible(true);
        if (dialog.isAccepted()) {
            try {
                loanService.returnItem(selected.getItem().getId(), dialog.getSelectedDate());
                refreshAll();
                saveData(false);
                statusLabel.setText("Đã ghi nhận trả tài liệu " + selected.getItem().getTitle());
                if (selected.getOverdueDays() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Tài liệu trả trễ " + selected.getOverdueDays() + " ngày.\nTiền phạt: "
                            + String.format("%,d", selected.getFineAmount()) + " đ",
                            "Thông tin tiền phạt", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tài liệu được trả đúng hạn. Không phát sinh tiền phạt.",
                            "Trả tài liệu thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IllegalArgumentException exception) {
                showError(exception.getMessage());
            }
        }
    }

    private void exportLoanData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất danh sách mượn - trả");
        chooser.setSelectedFile(new java.io.File("danh-sach-muon-tra.csv"));
        chooser.setFileFilter(new FileNameExtensionFilter("Tệp CSV (*.csv)", "csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        java.io.File selectedFile = chooser.getSelectedFile();
        if (!selectedFile.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".csv")) {
            selectedFile = new java.io.File(selectedFile.getParentFile(), selectedFile.getName() + ".csv");
        }
        if (selectedFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Tệp đã tồn tại. Bạn có muốn ghi đè không?", "Xác nhận ghi đè", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            new LoanCsvExportService().export(loanService.getLoanRecords(), selectedFile.toPath());
            JOptionPane.showMessageDialog(this,
                    "Đã xuất " + loanService.getLoanRecords().size() + " lượt mượn - trả vào:\n" + selectedFile,
                    "Xuất dữ liệu thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("Không thể xuất dữ liệu: " + exception.getMessage());
        }
    }

    private void openCharts() {
        ChartWindow chartWindow = new ChartWindow(libraryService.getAllItems(), loanService.getLoanRecords());
        chartWindow.setVisible(true);
    }

    private void showSelectedItemDetails() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            detailArea.setText("");
            return;
        }
        LibraryItem item = itemTableModel.getItemAt(itemTable.convertRowIndexToModel(selectedRow));
        detailArea.setText(item.toString() + "\n\nThời hạn mượn: " + item.getLoanDays() + " ngày");
        detailArea.setCaretPosition(0);
    }

    private void refreshAll() {
        itemTableModel.setItems(libraryService.getAllItems());
        loanTableModel.setRecords(loanService.getLoanRecords());
        showSelectedItemDetails();
        statusLabel.setText("Tổng số tài liệu: " + itemTableModel.getRowCount()
                + " | Tổng lượt mượn: " + loanTableModel.getRowCount());
    }

    private void saveData(boolean showMessage) {
        try {
            AppData data = new AppData(repository.exportItems(), loanService.getLoanRecords());
            persistenceService.save(data);
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Đã lưu dữ liệu thành công.", "Lưu dữ liệu", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException exception) {
            if (showMessage) {
                showError("Không thể lưu dữ liệu: " + exception.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.ERROR_MESSAGE);
    }
}
