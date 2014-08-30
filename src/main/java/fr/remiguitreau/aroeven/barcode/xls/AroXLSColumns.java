package fr.remiguitreau.aroeven.barcode.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public enum AroXLSColumns {
    BRAND {
        @Override
        public String extractValueFromRow(final XSSFRow row) {
            try {
                return row.getCell(0).getStringCellValue();
            } catch (final Exception ex) {
                throw new AroEquipmentImportException("'Marque' incorrecte à la ligne "
                        + (row.getRowNum() + 1), ex);
            }
        }
    },
    SIZE {
        @Override
        public String extractValueFromRow(final XSSFRow row) {
            try {
                switch (row.getCell(1).getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        return row.getCell(1).getRawValue();
                    default:
                        return row.getCell(1).getStringCellValue();
                }
            } catch (final Exception ex) {
                throw new AroEquipmentImportException("'Taille' incorrecte à la ligne "
                        + (row.getRowNum() + 1), ex);
            }
        }
    },
    NUMBER {
        @Override
        public Integer extractValueFromRow(final XSSFRow row) {
            try {
                return Double.valueOf(row.getCell(2).getNumericCellValue()).intValue();
            } catch (final Exception ex) {
                throw new AroEquipmentImportException("'Nombre d'equipements' incorrecte à la ligne "
                        + (row.getRowNum() + 1), ex);
            }
        }
    },
    PAIR {
        @Override
        public Boolean extractValueFromRow(final XSSFRow row) {
            try {
                return row.getCell(3).getStringCellValue().equals("Oui");
            } catch (final Exception ex) {
                throw new AroEquipmentImportException("Valeur de 'Pair' incorrecte à la ligne "
                        + (row.getRowNum() + 1), ex);
            }
        }
    };

    public abstract <T> T extractValueFromRow(final XSSFRow row);
}
