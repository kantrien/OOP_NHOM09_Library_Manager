package vn.edu.ptit.oop.nhom9.ui.form;

import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.Magazine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Trường riêng của Tạp chí: số phát hành (dạng chữ, ví dụ "Số 01/2025"). */
public class MagazineFieldPanel extends TypeFieldPanel {

    private static final int MAX_LEN = 50;

    private final JTextField txtIssue = new JTextField(20);

    public MagazineFieldPanel() {
        setBorder(BorderFactory.createTitledBorder("Thông tin tạp chí"));
        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Số phát hành:"));
        add(txtIssue);
    }

    @Override
    public String typeName() {
        return "Tạp chí";
    }

    @Override
    public List<String> validateFields() {
        List<String> errors = new ArrayList<>();
        String issue = txtIssue.getText().trim();
        if (issue.isEmpty()) {
            errors.add("Số phát hành không được để trống");
        } else if (issue.length() > MAX_LEN) {
            errors.add("Số phát hành không được dài quá " + MAX_LEN + " ký tự");
        }
        return errors;
    }

    @Override
    public LibraryItem build(CommonData common) {
        return new Magazine(common.id(), common.title(), common.author(), common.year(),
                common.available(), txtIssue.getText().trim());
    }

    @Override
    public void fill(LibraryItem item) {
        if (!(item instanceof Magazine magazine)) {
            throw new IllegalArgumentException("MagazineFieldPanel chỉ nhận Magazine, nhận được: "
                    + (item == null ? "null" : item.getClass().getSimpleName()));
        }
        txtIssue.setText(magazine.getIssueNumber());
    }

    @Override
    public void clear() {
        txtIssue.setText("");
    }
}
