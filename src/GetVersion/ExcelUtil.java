package GetVersion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    /**
     * 读取Excel中所有行
     * 
     * @param filePath
     *            Excel的路径
     * @param sheetnum
     *            sheet页码
     * @return 返回Excel行数组
     */
    public ArrayList<Row> readExcelHssfRows(String filePath, int sheetnum) {
        ArrayList<Row> rows = new ArrayList<Row>();
        int rowIndex = 0;
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook workbook = null;
            if (filePath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(in);
            } else {
                workbook = new HSSFWorkbook(in);
            }
            Sheet sheet = workbook.getSheetAt(sheetnum);
            while (true) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    break;
                }
                rows.add(row);
                rowIndex++;
            }
            workbook.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 返回Excel中单元格的{@link String}值
     * 
     * @param row
     *            单元格所在行
     * @param index
     *            单元格所在列的索引
     * @return 当所在行为空或类型不正确时返回{@code null}
     */
    public String getCellStringValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String string = cell.getStringCellValue().trim();
            return string == "" ? null : string;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            int num = (int) cell.getNumericCellValue();
            return String.valueOf(num);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
    }
}