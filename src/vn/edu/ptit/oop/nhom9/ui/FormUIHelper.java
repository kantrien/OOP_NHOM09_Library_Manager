package vn.edu.ptit.oop.nhom9.ui;

import vn.edu.ptit.oop.nhom9.ui.form.TypeFieldPanel;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Quản lý CardLayout: mỗi loại tài liệu là một "card" (một TypeFieldPanel).
 * Thêm loại mới = viết thêm 1 TypeFieldPanel và đưa vào danh sách, không sửa lớp này (Open-Closed).
 */
public final class FormUIHelper {

    private final CardLayout cards = new CardLayout();
    private final JPanel container = new JPanel(cards);
    /** LinkedHashMap giữ đúng thứ tự thêm vào → ComboBox hiện Sách, Tạp chí, Luận văn theo thứ tự. */
    private final Map<String, TypeFieldPanel> panels = new LinkedHashMap<>();
    private String currentType;

    public FormUIHelper(List<TypeFieldPanel> typePanels) {
        if (typePanels == null || typePanels.isEmpty()) {
            throw new IllegalArgumentException("Cần ít nhất một loại tài liệu");
        }
        for (TypeFieldPanel p : typePanels) {
            String key = p.typeName();
            // Trùng khóa thì card sau sẽ âm thầm đè card trước → báo lỗi ngay khi khởi tạo
            if (panels.containsKey(key)) {
                throw new IllegalArgumentException("Trùng loại tài liệu: " + key);
            }
            panels.put(key, p);
            container.add(p, key);   // tên card = typeName
        }
        currentType = typePanels.get(0).typeName();
    }

    /** Tạo ComboBox chứa tên các loại; đổi lựa chọn → đổi card. */
    public JComboBox<String> createTypeCombo() {
        JComboBox<String> combo = new JComboBox<>(panels.keySet().toArray(new String[0]));
        combo.setSelectedItem(currentType);
        combo.addActionListener(e -> show((String) combo.getSelectedItem()));
        return combo;
    }

    /** Hiển thị card ứng với loại tài liệu. */
    public void show(String typeName) {
        // Fail-secure: loại lạ thì từ chối, không để form ở trạng thái mơ hồ
        if (!panels.containsKey(typeName)) {
            throw new IllegalArgumentException("Loại tài liệu không hỗ trợ: " + typeName);
        }
        cards.show(container, typeName);
        currentType = typeName;
    }

    /** Panel của loại đang chọn. */
    public TypeFieldPanel current() {
        return panels.get(currentType);
    }

    public JPanel getContainer() {
        return container;
    }
}
