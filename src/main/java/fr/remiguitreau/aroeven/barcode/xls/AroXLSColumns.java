package fr.remiguitreau.aroeven.barcode.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public enum AroXLSColumns {
    BRAND {
        @Override
        public String extractValueFromRow(final XSSFRow row) {
            return row.getCell(0).getStringCellValue();
        }
    },
    SIZE {
        @Override
        public String extractValueFromRow(final XSSFRow row) {
            switch (row.getCell(1).getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return row.getCell(1).getRawValue();
                default:
                    return row.getCell(1).getStringCellValue();
            }
        }
    },
    NUMBER {
        @Override
        public Integer extractValueFromRow(final XSSFRow row) {
            return Integer.parseInt(row.getCell(2).getRawValue());
        }
    },
    PAIR {
        @Override
        public Boolean extractValueFromRow(final XSSFRow row) {
            return row.getCell(3).getStringCellValue().equals("Oui");
        }
    };

    public abstract <T> T extractValueFromRow(final XSSFRow row);
}
