package vn.edu.ptit.oop.nhom9.ui;

import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public final class ChartHelper {
    private ChartHelper() {
    }

    public static ChartPanel createPieChart(List<LibraryItem> items) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        long books = items.stream().filter(item -> "Sách".equals(item.getTypeName())).count();
        long magazines = items.stream().filter(item -> "Tạp chí".equals(item.getTypeName())).count();
        long theses = items.stream().filter(item -> "Luận văn".equals(item.getTypeName())).count();
        dataset.setValue("Sách", books);
        dataset.setValue("Tạp chí", magazines);
        dataset.setValue("Luận văn", theses);
        long totalItems = books + magazines + theses;
        JFreeChart chart = ChartFactory.createPieChart(
                "Cơ cấu tài liệu trong thư viện - Tổng: " + totalItems + " tài liệu",
                dataset, true, true, false);
        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1} tài liệu ({2})",
                NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN")),
                NumberFormat.getPercentInstance(Locale.forLanguageTag("vi-VN"))));
        plot.setSimpleLabels(false);
        plot.setLabelFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        return new ChartPanel(chart);
    }

    public static ChartPanel createMonthlyBarChart(List<LoanRecord> records) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth currentMonth = YearMonth.now();
        boolean usingDemoData = records.isEmpty();
        int[] demoCounts = {8, 10, 7, 13, 11, 15, 12, 16, 14, 18, 17, 20};
        int demoIndex = 0;
        for (int offset = 11; offset >= 0; offset--) {
            YearMonth month = currentMonth.minusMonths(offset);
            long count;
            if (usingDemoData) {
                count = demoCounts[demoIndex++];
            } else {
                count = records.stream()
                        .filter(record -> YearMonth.from(record.getBorrowDate()).equals(month))
                        .count();
            }
            dataset.addValue(count, "Lượt mượn", formatter.format(month));
        }
        String title = usingDemoData
                ? "Số lượt mượn theo tháng (dữ liệu minh họa)"
                : "Số lượt mượn theo tháng";
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Tháng",
                "Số lượt mượn (lượt)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        return new ChartPanel(chart);
    }
}