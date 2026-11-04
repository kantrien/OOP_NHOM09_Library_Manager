package vn.edu.ptit.oop.nhom9.ui.form;

/**
 * Gom các trường chung của mọi tài liệu, lấy từ phần trên của ItemFormDialog.
 * Dùng record (Java 16+): bất biến, tự sinh equals/hashCode/toString.
 * available: Thêm mới = true; Sửa = giữ nguyên trạng thái của tài liệu cũ.
 */
public record CommonData(String id, String title, String author, int year, boolean available) {}
