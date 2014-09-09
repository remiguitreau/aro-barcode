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
        for (int i = equipmentSheet.getFirstRowNum() + 1; i <= equipmentSheet.getLastRowNum(); i++) {
            try {
                for (final AroEquipment equipment : extractEquipmentsFromRow(equipmentSheet.getRow(i))) {
                    addEquipmentToSize(equipmentsBySize, equipment);
                }
            } catch (final Exception ex) {
                log.warn("Error on line " + i, ex);
                result.getErrors().put(Integer.valueOf(i), ex.getMessage());
            }
        }
        final List<AroEquipment> equipments = new ArrayList<>();
        for (final List<AroEquipment> list : equipmentsBySize.values()) {
            for (final AroEquipment equipment : list) {
                equipments.add(equipment);
            }
        }
        result.setEquipments(equipments);
        return result;
    }

    private void addEquipmentToSize(final Map<String, List<AroEquipment>> equipmentsBySize,
            final AroEquipment equipment) {
        if (!equipmentsBySize.containsKey(equipment.getSize())) {
            equipmentsBySize.put(equipment.getSize(), new ArrayList<AroEquipment>());
        }
        equipment.setNumber(String.valueOf(equipmentsBySize.get(equipment.getSize()).size() + 1));
        equipmentsBySize.get(equipment.getSize()).add(equipment);
    }

    private List<AroEquipment> extractEquipmentsFromRow(final XSSFRow row) {
        final List<AroEquipment> equipments = new ArrayList<AroEquipment>();
        try {
            final int numberOfEquipments = AroXLSColumns.NUMBER.extractValueFromRow(row);
            final String brand = AroXLSColumns.BRAND.extractValueFromRow(row);
            final String size = AroXLSColumns.SIZE.extractValueFromRow(row);
            final boolean pair = AroXLSColumns.PAIR.extractValueFromRow(row);
            log.info("Extract {} equipments with brand '{}', size={} and pair={}", numberOfEquipments, brand,
                    size, pair);
            for (int i = 0; i < numberOfEquipments; i++) {
                equipments.add(new AroEquipment(brand, size, String.valueOf(i + 1), pair));
            }
        } catch (final AroEquipmentImportException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new AroEquipmentImportException("Erreur d'import de la ligne " + row.getRowNum(), ex);
        }
        return equipments;
    }
}