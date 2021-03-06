package fr.remiguitreau.aroeven.barcode.xls;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.barcode.AroEquipment;

@Slf4j
public class XLSEquipmentsImporter {

    public ImportResult importEquipmentsFromInputStream(final InputStream xlsInputStream) {
        try {
            final XSSFWorkbook workbook = new XSSFWorkbook(xlsInputStream);
            final XSSFSheet equipmentSheet = workbook.getSheetAt(0);
            return extractEquipmentsFromSheet(equipmentSheet);
        } catch (final AroEquipmentImportException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new AroEquipmentImportException("Error while importing equipments...", ex);
        }
    }

    // -------------------------------------------------------------------------

    private ImportResult extractEquipmentsFromSheet(final XSSFSheet equipmentSheet) {
        final Map<String, List<AroEquipment>> equipmentsBySize = new HashMap<String, List<AroEquipment>>();
        final ImportResult result = new ImportResult();
        log.info("Import equipments from line {} to {}", equipmentSheet.getFirstRowNum() + 2,
                equipmentSheet.getLastRowNum() + 1);
        final List<AroEquipment> equipments = new ArrayList<>();
        for (int i = equipmentSheet.getFirstRowNum() + 1; i <= equipmentSheet.getLastRowNum(); i++) {
            try {
                equipments.addAll(extractEquipmentsFromRow(equipmentSheet.getRow(i)));
            } catch (final Exception ex) {
                log.warn("Error on line " + i, ex);
                result.getErrors().put(Integer.valueOf(i), ex.getMessage());
            }
        }
        result.setEquipments(equipments);
        return result;
    }

    private List<AroEquipment> extractEquipmentsFromRow(final XSSFRow row) {
        final List<AroEquipment> equipments = new ArrayList<AroEquipment>();
        try {
            final int number = AroXLSColumns.NUMBER.extractValueFromRow(row);
            final String brand = AroXLSColumns.BRAND.extractValueFromRow(row);
            final String size = AroXLSColumns.SIZE.extractValueFromRow(row);
            final boolean pair = AroXLSColumns.PAIR.extractValueFromRow(row);
            log.info("Extract new equipment '{}' with brand '{}', size={} and pair={}", number, brand, size,
                    pair);
            equipments.add(new AroEquipment(brand, size, String.valueOf(number), pair));
        } catch (final AroEquipmentImportException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new AroEquipmentImportException("Erreur d'import de la ligne " + row.getRowNum(), ex);
        }
        return equipments;
    }
}