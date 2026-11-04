# Library Manager - Nhóm 9

Ứng dụng quản lý thư viện viết bằng Java Swing, package `vn.edu.ptit.oop.nhom09`.

## Đối chiếu bảng phân công

|Tên| Phần | Các lớp chính |
|---|---|---|
| Tâm | Mô hình hóa dữ liệu | `LibraryItem`, `Book`, `Magazine`, `Thesis` |
| Minh | Quản lý kho dữ liệu | `LibraryRepository`, `InMemoryLibraryRepository`, `MockDataGenerator`, `LibraryService` |
| Hiếu | Lưu trữ & Tiện ích | `FileService`, `LoanCsvExportService`, `ValidationUtil`, `DataPersistenceService`, `AppData`,`LoanExcelExportService`  |
| Sơn | Xử lý JTable | `ItemTableModel`, `LoanTableModel`, `LoanTableCellRenderer` |
| Nguyên | Giao diện Thêm/Sửa | `ItemFormDialog`, `FormUIHelper` |
| Vương | Nghiệp vụ Mượn/Trả | `LoanRecord`, `LoanService` |
| Lâm | Giao diện Mượn/Trả & Tính phạt | `LoanDialog`, `FineCalculator`, `BookFine`, `MagazineFine`, `ThesisFine` |
| Sang | Thống kê & Đồ thị | `ChartWindow`, `ChartHelper` |
| Hải | Kiểm thử, xử lý lỗi & Báo cáo | `ItemNotFoundException`, `InvalidDataException`, `OverdueException`, `AboutDialog`, `LibraryManagerTest` |

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