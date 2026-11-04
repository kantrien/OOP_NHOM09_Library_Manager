package vn.edu.ptit.oop.nhom9.ui;

import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.ui.form.BookFieldPanel;
import vn.edu.ptit.oop.nhom9.ui.form.CommonData;
import vn.edu.ptit.oop.nhom9.ui.form.MagazineFieldPanel;
import vn.edu.ptit.oop.nhom9.ui.form.ThesisFieldPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog modal dùng chung cho Thêm và Sửa tài liệu.
 * Cách dùng:
 * <pre>
 *   ItemFormDialog d = new ItemFormDialog(this, null); // null = Thêm
 *   d.setVisible(true);                                // chặn tới khi đóng
 *   if (d.getResult() != null) libraryService.addItem(d.getResult());
 * </pre>
 * Ở chế độ Sửa, dialog KHÔNG sửa object gốc mà tạo object MỚI;
 * nơi gọi dùng libraryService.updateItem(...) → không bao giờ có object "sửa dở".
 */
public class ItemFormDialog extends JDialog {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_TEXT = 200;
    /** ID: chữ, số, '-' hoặc '_', dài 1–20 ký tự. Whitelist thay vì blacklist. */
    private static final String ID_PATTERN = "[A-Za-z0-9_-]{1,20}";

    /** null = chế độ Thêm; khác null = chế độ Sửa. */
    private final LibraryItem editing;
    private final FormUIHelper helper = new FormUIHelper(List.of(
            new BookFieldPanel(), new MagazineFieldPanel(), new ThesisFieldPanel()));

    private final JComboBox<String> cboType = helper.createTypeCombo();
    private final JTextField txtId = new JTextField(20);
    private final JTextField txtTitle = new JTextField(20);
    private final JTextField txtAuthor = new JTextField(20);
    private final JSpinner spnYear;

    private boolean confirmed;
    private LibraryItem result;

    public ItemFormDialog(Window owner, LibraryItem editing) {
        super(owner, editing == null ? "Thêm tài liệu" : "Sửa tài liệu", ModalityType.APPLICATION_MODAL);
        this.editing = editing;

        int thisYear = Year.now().getValue();
        spnYear = new JSpinner(new SpinnerNumberModel(thisYear, MIN_YEAR, thisYear, 1));
        spnYear.setEditor(new JSpinner.NumberEditor(spnYear, "#")); // hiện "2021" thay vì "2,021"

        buildUI();
        if (editing != null) {
            fillForEdit();
        }
        pack();
        setLocationRelativeTo(owner);
    }

    /** private: KHÔNG để lớp con override phương thức được gọi từ constructor. */
    private void buildUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Trên: loại tài liệu + các trường chung
        JPanel common = new JPanel(new GridLayout(0, 2, 5, 5));
        common.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));
        common.add(new JLabel("Loại tài liệu:"));
        common.add(cboType);
        common.add(new JLabel("Mã tài liệu:"));
        common.add(txtId);
        common.add(new JLabel("Tiêu đề:"));
        common.add(txtTitle);
        common.add(new JLabel("Tác giả / Đơn vị:"));
        common.add(txtAuthor);
        common.add(new JLabel("Năm xuất bản:"));
        common.add(spnYear);
        root.add(common, BorderLayout.NORTH);

        // Giữa: card các trường riêng (đổi theo ComboBox)
        root.add(helper.getContainer(), BorderLayout.CENTER);

        // Dưới: nút Lưu / Hủy
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnSave);
        buttons.add(btnCancel);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);

        // Enter = Lưu
        getRootPane().setDefaultButton(btnSave);
        // ESC = Hủy (Key Binding)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /** Chế độ Sửa: đổ dữ liệu cũ lên form, khóa những trường không được đổi. */
    private void fillForEdit() {
        txtId.setText(editing.getId());
        txtTitle.setText(editing.getTitle());
        txtAuthor.setText(editing.getAuthor());
        spnYear.setValue(editing.getYear());

        cboType.setSelectedItem(editing.getTypeName()); // listener tự đổi card
        helper.current().fill(editing);

        // ID là khóa trong repository; đổi loại thì mất trường riêng → khóa cả hai
        txtId.setEditable(false);
        cboType.setEnabled(false);
    }

    private void onSave() {
        List<String> errors = new ArrayList<>();

        String id = txtId.getText().trim();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();

        if (!id.matches(ID_PATTERN)) {
            errors.add("Mã tài liệu chỉ gồm chữ, số, '-' hoặc '_' (1–20 ký tự)");
        }
        checkText(title, "Tiêu đề", errors);
        checkText(author, "Tác giả / Đơn vị", errors);
        try {
            spnYear.commitEdit();
        } catch (ParseException e) {
            errors.add("Năm xuất bản phải từ " + MIN_YEAR + " đến " + Year.now().getValue());
        }
        errors.addAll(helper.current().validateFields()); // đa hình: panel nào tự kiểm tra panel đó

        if (!errors.isEmpty()) {
            showErrors(errors);
            return;
        }

        int year = ((Number) spnYear.getValue()).intValue();
        // Thêm mới: còn trong thư viện; Sửa: giữ nguyên trạng thái mượn của tài liệu cũ
        boolean available = editing == null || editing.isAvailable();
        try {
            // Tạo object MỚI; model ném InvalidDataException nếu vi phạm bất biến của lớp
            result = helper.current().build(new CommonData(id, title, author, year, available));
        } catch (InvalidDataException ex) {
            showErrors(List.of(ex.getMessage()));
            return;
        }
        confirmed = true;
        dispose();
    }

    private static void checkText(String value, String label, List<String> errors) {
        if (value.isEmpty()) {
            errors.add(label + " không được để trống");
        } else if (value.length() > MAX_TEXT) {
            errors.add(label + " không được dài quá " + MAX_TEXT + " ký tự");
        }
    }

    /** Gom tất cả lỗi vào một hộp thoại, mỗi lỗi một dòng. */
    private void showErrors(List<String> errors) {
        JOptionPane.showMessageDialog(this,
                "Vui lòng sửa các lỗi sau:\n• " + String.join("\n• ", errors),
                "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    /** Đối tượng MỚI đã tạo; null nếu người dùng Hủy. */
    public LibraryItem getResult() {
        return result;
    }
}
