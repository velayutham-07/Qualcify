package com.qualcify.utils;

import org.apache.poi.ss.usermodel.*;
import com.qualcify.model.BatchDefect;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public static List<BatchDefect> readDefects(InputStream is) throws Exception {
        List<BatchDefect> list = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // skip header
            Cell batchCell = row.getCell(0);
            Cell defectCell = row.getCell(1);
            Cell defectTypeCell = row.getCell(2);
            String batch = null;
            if (batchCell != null) {
                if (batchCell.getCellType() == CellType.STRING) {
                    batch = batchCell.getStringCellValue();
                } else if (batchCell.getCellType() == CellType.NUMERIC) {
                    batch = String.valueOf((long)batchCell.getNumericCellValue());
                }
            }
            int defect = (defectCell != null && defectCell.getCellType() == CellType.NUMERIC)
                ? (int) defectCell.getNumericCellValue() : 0;
            String defectType = null;
            if (defectTypeCell != null) {
                if (defectTypeCell.getCellType() == CellType.STRING) {
                    defectType = defectTypeCell.getStringCellValue();
                } else if (defectTypeCell.getCellType() == CellType.NUMERIC) {
                    defectType = String.valueOf((long)defectTypeCell.getNumericCellValue());
                }
            }
            if (batch != null) {
                list.add(new BatchDefect(batch, defect, defectType));
            }
        }
        workbook.close();
        return list;
    }
}