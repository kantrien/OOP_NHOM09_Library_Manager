package vn.edu.ptit.oop.nhom9.ui.form;

import vn.edu.ptit.oop.nhom9.common.ValidationUtil;
import vn.edu.ptit.oop.nhom9.exception.InvalidDataException;
import vn.edu.ptit.oop.nhom9.model.Book;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/** Trường riêng của Sách: ISBN, số trang. */
public class BookFieldPanel extends TypeFieldPanel {

    private final JTextField txtIsbn = new JTextField(20);
    private final JSpinner spnPages = new JSpinner(new SpinnerNumberModel(1, 1, 5000, 1));

    public BookFieldPanel() {
        setBorder(BorderFactory.createTitledBorder("Thông tin sách"));
        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Mã ISBN:"));
        add(txtIsbn);

        add(new JLabel("Số trang:"));
        add(spnPages);
    }

    @Override
    public String typeName() {
        return "Sách";
    }

    /** Bỏ dấu '-' và khoảng trắng: người dùng gõ "978-604-..." vẫn được. */
    private String normalizedIsbn() {
        return txtIsbn.getText().replaceAll("[-\\s]", "");
    }

    @Override
    public List<String> validateFields() {
        List<String> errors = new ArrayList<>();
        String isbn = normalizedIsbn();
        if (isbn.isEmpty()) {
            errors.add("ISBN không được để trống");
        } else {
            try {
                ValidationUtil.validateISBN(isbn);
            } catch (InvalidDataException e) {
                errors.add(e.getMessage());
            }
        }

        try {
            spnPages.commitEdit();
        } catch (ParseException e) {
            errors.add("Số trang phải là số nguyên từ 1 đến 5000");
        }
        return errors;
    }

    @Override
    public LibraryItem build(CommonData common) {
        int pages = ((Number) spnPages.getValue()).intValue();
        return new Book(common.id(), common.title(), common.author(), common.year(),
                common.available(), normalizedIsbn(), pages);
    }

    @Override
    public void fill(LibraryItem item) {
        if (!(item instanceof Book book)) {
            throw new IllegalArgumentException("BookFieldPanel chỉ nhận Book, nhận được: "
                    + (item == null ? "null" : item.getClass().getSimpleName()));
        }
        txtIsbn.setText(book.getIsbn());
        spnPages.setValue(book.getPages());
    }

    @Override
    public void clear() {
        txtIsbn.setText("");
        spnPages.setValue(1);
    }
}
