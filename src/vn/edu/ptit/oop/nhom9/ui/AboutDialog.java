package vn.edu.ptit.oop.nhom9.ui;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AboutDialog extends JDialog {
    private static final String[][] TEAM_MEMBERS = {
        {"[Malang Trần Ngọc Kiên]", "[N24DCAT043]"},
        {"[Lê Tuấn Minh]", "[N24DCAT047]"},
        {"[Trương Trần Hoàng Sang]", "[N24DCAT067]"},
        {"[Võ Thanh Tâm]", "[N24DCAT069]"},
        {"[Lưu Phi Hiếu]", "[N24DCAT026]"},
        {"[Phan Ngọc Hải]", "[N22DCAT023]"},
        {"[Nguyễn Quốc Vương]", "[N22DCAT068]"},
        {"[Nguyễn Trung Nguyên ]", "[N24DCAT039]"},
        {"[Bùi Lê Lâm ]", "[N24DCAT044]"},
        {"[Bùi Thanh Sơn]", "[N24DCAT068]"}
    };

    public AboutDialog(Window owner) {
        super(owner, "Thông tin nhóm thực hiện", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Nhóm 9 - Đề tài: Library Manager"), BorderLayout.NORTH);
        String[] columns = {"STT", "Họ và tên", "MSSV"};
        Object[][] members = new Object[TEAM_MEMBERS.length][3];
        for (int i = 0; i < TEAM_MEMBERS.length; i++) {
            members[i][0] = i + 1;
            members[i][1] = TEAM_MEMBERS[i][0];
            members[i][2] = TEAM_MEMBERS[i][1];
        }
        DefaultTableModel model = new DefaultTableModel(members, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable memberTable = new JTable(model);
        add(new JScrollPane(memberTable), BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.add(new JLabel("Thay các ô trong ngoặc vuông bằng họ tên và MSSV thật trước khi nộp."), BorderLayout.CENTER);
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(event -> dispose());
        footer.add(closeButton, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
        setSize(620, 390);
        setLocationRelativeTo(owner);
    }
}