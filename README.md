# Library Manager - Nhóm 1

Ứng dụng quản lý thư viện viết bằng Java Swing, package `vn.edu.ptit.oop.nhom09`.

## Đối chiếu bảng phân công

| Phần | Các lớp chính |
|---|---|
| Mô hình hóa dữ liệu | `LibraryItem`, `Book`, `Magazine`, `Thesis` |
| Quản lý kho dữ liệu | `LibraryRepository`, `InMemoryLibraryRepository`, `MockDataGenerator` |
| Lưu trữ & Tiện ích | `FileService`, `LoanCsvExportService`, `ValidationUtil` |
| Xử lý JTable | `ItemTableModel`, `LoanTableModel`, `LoanTableCellRenderer` |
| Giao diện Thêm/Sửa | `ItemFormDialog`, `FormUIHelper` |
| Nghiệp vụ Mượn/Trả | `LoanRecord`, `LoanService` |
| Giao diện Mượn/Trả & Tính phạt | `LoanDialog`, `FineCalculator`, `BookFine`, `MagazineFine`, `ThesisFine` |
| Thống kê & Đồ thị | `ChartWindow`, `ChartHelper` |
| Kiểm thử, xử lý lỗi & Báo cáo | `ItemNotFoundException`, `InvalidDataException`, `OverdueException`, `AboutDialog`, `LibraryManagerTest` |

## Chức năng

- Quản lý sách, tạp chí và luận văn.
- Tạo sẵn 15 tài liệu mẫu khi chưa có dữ liệu đã lưu.
- Tìm kiếm theo tên tài liệu hoặc tác giả.
- Thêm/sửa tài liệu bằng `JDialog`, `JComboBox` và `CardLayout`.
- Mượn/trả tài liệu, lưu họ tên, điện thoại, MSSV, lớp, ngày mượn, hạn trả và ngày trả.
- Tự tính ngày quá hạn và tiền phạt theo loại tài liệu.
- Tô màu các lượt mượn quá hạn/trả trễ.
- Xuất lịch sử mượn - trả CSV.
- Lưu/đọc dữ liệu bằng `ObjectOutputStream` và `ObjectInputStream` tại `data/library-manager.dat`.
- Biểu đồ tròn tỷ lệ loại tài liệu và biểu đồ cột lượt mượn theo tháng.

## Công nghệ

- JDK 17 trở lên.
- Maven.
- Swing.
- JFreeChart 1.5.6.
- JUnit 5 cho kiểm thử.

## Chạy trên Windows

1. Giải nén ZIP.
2. Mở thư mục `LibraryManager` chứa `pom.xml`, `src` và `run.bat`.
3. Cài JDK 17 trở lên.
4. Nhấn đúp `run.bat`. Lần đầu cần Internet để tải JFreeChart.

`run.bat` cũng tự tìm thư mục `LibraryManager` con nếu bạn đặt file ở thư mục ngoài cùng.

## Chạy bằng Maven

Trong thư mục có `pom.xml`, chạy:

```bash
mvn clean test
mvn exec:java
```

## Trước khi nộp

- Mở `AboutDialog.java` và thay 10 dòng placeholder bằng họ tên/MSSV chính xác của thành viên.
- Xác nhận mức tiền phạt với nhóm. Mức đang dùng chỉ là mức minh họa: sách 2.000 đồng/ngày, tạp chí 1.000 đồng/ngày, luận văn 3.000 đồng/ngày.
- Khi chưa có lượt mượn thực tế, biểu đồ cột hiển thị dữ liệu minh họa và ghi rõ đây không phải số liệu thật.
