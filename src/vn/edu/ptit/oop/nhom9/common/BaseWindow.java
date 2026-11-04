package vn.edu.ptit.oop.nhom9.common;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class BaseWindow extends JFrame {
    public BaseWindow(String title, int width, int height) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        applyFont();
    }

    private void applyFont() {
        Font font = new Font("Arial", Font.PLAIN, 13);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 13));
        UIManager.put("ComboBox.font", font);
    }
}