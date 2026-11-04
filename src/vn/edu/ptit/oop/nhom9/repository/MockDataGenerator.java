package vn.edu.ptit.oop.nhom9.repository;

import java.util.ArrayList;
import java.util.List;
import vn.edu.ptit.oop.nhom9.model.Book;
import vn.edu.ptit.oop.nhom9.model.LibraryItem;
import vn.edu.ptit.oop.nhom9.model.Magazine;
import vn.edu.ptit.oop.nhom9.model.Thesis;

public final class MockDataGenerator {
    private MockDataGenerator() {
    }

    public static List<LibraryItem> generateItems() {
        List<LibraryItem> items = new ArrayList<>();
        items.add(new Book("B001", "Lập trình Java căn bản", "Nguyễn Văn A", 2021, true, "9786041234567", 320));
        items.add(new Book("B002", "Lập trình hướng đối tượng", "Trần Thị B", 2022, true, "9786041234568", 285));
        items.add(new Book("B003", "Cấu trúc dữ liệu và giải thuật", "Lê Văn C", 2020, true, "9786041234569", 410));
        items.add(new Book("B004", "Cơ sở dữ liệu", "Phạm Thị D", 2023, true, "9786041234570", 360));
        items.add(new Book("B005", "Hệ điều hành", "Hoàng Văn E", 2019, true, "9786041234571", 390));
        items.add(new Book("B006", "Mạng máy tính", "Vũ Thị F", 2021, true, "9786041234572", 340));
        items.add(new Magazine("M001", "Tạp chí Công nghệ thông tin", "Ban biên tập CNTT", 2025, true, "Số 01/2025"));
        items.add(new Magazine("M002", "Tạp chí Khoa học trẻ", "Ban biên tập KHT", 2024, true, "Số 12/2024"));
        items.add(new Magazine("M003", "Tạp chí Giáo dục", "Ban biên tập Giáo dục", 2025, true, "Số 02/2025"));
        items.add(new Magazine("M004", "Tạp chí Khoa học máy tính", "Ban biên tập KHM", 2023, true, "Số 04/2023"));
        items.add(new Thesis("T001", "Ứng dụng Java trong quản lý", "Đặng Văn G", 2023, true, "TS. Nguyễn H", "Đại học Bách khoa"));
        items.add(new Thesis("T002", "Phân tích dữ liệu sinh viên", "Bùi Thị I", 2022, true, "TS. Trần K", "Đại học Công nghệ"));
        items.add(new Thesis("T003", "Xây dựng website thư viện", "Đỗ Văn L", 2024, true, "ThS. Phạm M", "Đại học PTIT"));
        items.add(new Thesis("T004", "Hệ thống quản lý mượn trả", "Ngô Thị N", 2021, true, "TS. Lê O", "Đại học PTIT"));
        items.add(new Thesis("T005", "Nghiên cứu cơ sở dữ liệu phân tán", "Mai Văn P", 2020, true, "TS. Vũ Q", "Đại học Khoa học tự nhiên"));
        return items;
    }
}