

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelUtil {

    /**
     * 读取Excel中所有行
     * 
     * @param filePath
     *            Excel的路径
     * @return 返回Excel行数组
     */
    public ArrayList<HSSFRow> readExcelHssfRows(String filePath) {
        ArrayList<HSSFRow> rows = new ArrayList<HSSFRow>();
        int rowIndex = 0;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(in));
            HSSFSheet sheet = workbook.getSheetAt(0);
            while (true) {
                HSSFRow row = sheet.getRow(rowIndex);
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
    public String getCellStringValue(HSSFRow row, int index) {
        HSSFCell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            String string = cell.getStringCellValue().trim();
            return string == "" ? null : string;
        }
        return null;
    }
}