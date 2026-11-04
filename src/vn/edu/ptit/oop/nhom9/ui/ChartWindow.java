package vn.edu.ptit.oop.nhom9.ui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class ChartWindow extends JFrame {
    public ChartWindow(List<LibraryItem> items, List<LoanRecord> loanRecords) {
        super("Thống kê thư viện");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 680);
        setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Tỷ lệ tài liệu", ChartHelper.createPieChart(items));
        tabs.addTab("Lượt mượn theo tháng", ChartHelper.createMonthlyBarChart(loanRecords));
        add(tabs, BorderLayout.CENTER);
    }
}