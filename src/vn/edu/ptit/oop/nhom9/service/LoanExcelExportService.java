package vn.edu.ptit.oop.nhom9.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import vn.edu.ptit.oop.nhom9.model.LoanRecord;

public class LoanExcelExportService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void export(List<LoanRecord> records, Path filePath) throws IOException {
        if (records == null) {
            throw new IllegalArgumentException("Danh sách mượn - trả không được để trống.");
        }
        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (OutputStream output = Files.newOutputStream(filePath);
                ZipOutputStream zip = new ZipOutputStream(output, StandardCharsets.UTF_8)) {
            writeEntry(zip, "[Content_Types].xml", contentTypesXml());
            writeEntry(zip, "_rels/.rels", rootRelationshipsXml());
            writeEntry(zip, "xl/workbook.xml", workbookXml());
            writeEntry(zip, "xl/_rels/workbook.xml.rels", workbookRelationshipsXml());
            writeEntry(zip, "xl/worksheets/sheet1.xml", worksheetXml(records));
        }
    }

    private String worksheetXml(List<LoanRecord> records) {
        StringBuilder xml = new StringBuilder(8192);
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xml.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        xml.append("<sheetViews><sheetView workbookViewId=\"0\"><pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/></sheetView></sheetViews>");
        xml.append("<sheetFormatPr defaultRowHeight=\"18\"/>");
        xml.append("<sheetData>");

        String[] headers = {
            "Mã tài liệu", "Tên tài liệu", "Loại tài liệu", "Người mượn", "Số điện thoại",
            "MSSV", "Lớp", "Ngày mượn", "Hạn trả", "Ngày trả", "Số ngày quá hạn", "Tiền phạt (VND)", "Trạng thái"
        };
        appendRowStart(xml, 1);
        for (int column = 0; column < headers.length; column++) {
            appendTextCell(xml, cellReference(column, 1), headers[column]);
        }
        xml.append("</row>");

        int rowNumber = 2;
        for (LoanRecord record : records) {
            long overdueDays = record.getOverdueDays();
            String status;
            if (record.isReturned() && overdueDays > 0) {
                status = "Đã trả trễ";
            } else if (record.isReturned()) {
                status = "Đã trả";
            } else if (overdueDays > 0) {
                status = "Quá hạn";
            } else {
                status = "Đang mượn";
            }

            String[] values = {
                record.getItem().getId(),
                record.getItem().getTitle(),
                record.getItem().getTypeName(),
                record.getBorrowerName(),
                record.getBorrowerPhone(),
                record.getStudentId(),
                record.getClassName(),
                DATE_FORMAT.format(record.getBorrowDate()),
                DATE_FORMAT.format(record.getDueDate()),
                record.getReturnDate() == null ? "" : DATE_FORMAT.format(record.getReturnDate()),
                Long.toString(overdueDays),
                Long.toString(record.getFineAmount()),
                status
            };

            appendRowStart(xml, rowNumber);
            for (int column = 0; column < values.length; column++) {
                String reference = cellReference(column, rowNumber);
                if (column == 10 || column == 11) {
                    appendNumberCell(xml, reference, values[column]);
                } else {
                    appendTextCell(xml, reference, values[column]);
                }
            }
            xml.append("</row>");
            rowNumber++;
        }
        xml.append("</sheetData></worksheet>");
        return xml.toString();
    }

    private void appendRowStart(StringBuilder xml, int rowNumber) {
        xml.append("<row r=\"").append(rowNumber).append("\">");
    }

    private void appendTextCell(StringBuilder xml, String reference, String value) {
        xml.append("<c r=\"").append(reference).append("\" t=\"inlineStr\"><is><t xml:space=\"preserve\">")
                .append(escapeXml(value == null ? "" : value))
                .append("</t></is></c>");
    }

    private void appendNumberCell(StringBuilder xml, String reference, String value) {
        xml.append("<c r=\"").append(reference).append("\"><v>")
                .append(value == null || value.isBlank() ? "0" : value)
                .append("</v></c>");
    }

    private String cellReference(int columnIndex, int rowNumber) {
        int value = columnIndex + 1;
        StringBuilder column = new StringBuilder();
        while (value > 0) {
            int remainder = (value - 1) % 26;
            column.insert(0, (char) ('A' + remainder));
            value = (value - 1) / 26;
        }
        return column.append(rowNumber).toString();
    }

    private String escapeXml(String value) {
        StringBuilder escaped = new StringBuilder(value.length());
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character == '&') {
                escaped.append("&amp;");
            } else if (character == '<') {
                escaped.append("&lt;");
            } else if (character == '>') {
                escaped.append("&gt;");
            } else if (character == '"') {
                escaped.append("&quot;");
            } else if (character == '\'') {
                escaped.append("&apos;");
            } else if (character == '\t' || character == '\n' || character == '\r' || character >= 0x20) {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    private void writeEntry(ZipOutputStream zip, String name, String content) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private String contentTypesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "</Types>";
    }

    private String rootRelationshipsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    private String workbookXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets><sheet name=\"Danh sach muon tra\" sheetId=\"1\" r:id=\"rId1\"/></sheets>"
                + "</workbook>";
    }

    private String workbookRelationshipsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "</Relationships>";
    }
}