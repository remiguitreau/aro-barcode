package fr.remiguitreau.aroeven.barcode.xls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import fr.remiguitreau.aroeven.barcode.AroEquipment;

@Data
public class ImportResult {

    private List<AroEquipment> equipments;

    private Map<Integer, String> errors = new HashMap<>();
}
