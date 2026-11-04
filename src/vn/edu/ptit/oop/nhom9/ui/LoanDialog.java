package vn.edu.ptit.oop.nhom9.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class LoanDialog extends JDialog {
    private final LibraryItem item;
    private final boolean borrowing;
    private final JTextField borrowerField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField studentIdField = new JTextField(20);
    private final JTextField classField = new JTextField(20);
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
    private boolean accepted;

    public LoanDialog(Window owner, LibraryItem item, LoanRecord record, boolean borrowing) {
        super(owner, borrowing ? "Mượn tài liệu" : "Trả tài liệu", Dialog.ModalityType.APPLICATION_MODAL);
        this.item = item;
        this.borrowing = borrowing;
        buildInterface(record);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    private void buildInterface(LoanRecord record) {
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(borrowing ? 6 : 2, 2, 8, 8));
        form.add(new JLabel("Tài liệu"));
        form.add(new JLabel(item.getId() + " - " + item.getTitle()));
        if (borrowing) {
            form.add(new JLabel("Họ tên người mượn"));
            form.add(borrowerField);
            form.add(new JLabel("Số điện thoại"));
            form.add(phoneField);
            form.add(new JLabel("Mã số sinh viên (MSSV)"));
            form.add(studentIdField);
            form.add(new JLabel("Lớp"));
            form.add(classField);
        }
        form.add(new JLabel(borrowing ? "Ngày mượn" : "Ngày trả"));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        if (!borrowing && record != null) {
            Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateSpinner.setValue(date);
        }
        form.add(dateSpinner);
        JPanel buttons = new JPanel();
        JButton confirmButton = new JButton(borrowing ? "Xác nhận mượn" : "Xác nhận trả");
        JButton cancelButton = new JButton("Hủy");
        confirmButton.addActionListener(event -> confirm());
        cancelButton.addActionListener(event -> dispose());
        buttons.add(confirmButton);
        buttons.add(cancelButton);
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void confirm() {
        if (borrowing) {
            try {
                LocalDate borrowDate = getSelectedDate();
                new LoanRecord(item, borrowerField.getText(), phoneField.getText(),
                        studentIdField.getText(), classField.getText(), borrowDate,
                        borrowDate.plusDays(item.getLoanDays()), null);
            } catch (InvalidDataException exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Thông tin chưa hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        accepted = true;
        dispose();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getBorrowerName() {
        return borrowerField.getText().trim();
    }

    public String getBorrowerPhone() {
        return phoneField.getText().trim();
    }

    public String getStudentId() {
        return studentIdField.getText().trim();
    }

    public String getClassName() {
        return classField.getText().trim();
    }

    public LocalDate getSelectedDate() {
        Date date = (Date) dateSpinner.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
