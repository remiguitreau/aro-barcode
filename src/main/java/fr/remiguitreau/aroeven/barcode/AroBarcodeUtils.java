package fr.remiguitreau.aroeven.barcode;

public class AroBarcodeUtils {

    public static String buildBarcodeFromEquipment(final AroEquipment equipment) {
        return equipment.getType() + "-" + equipment.getSize() + "-" + equipment.getIdentifier();
    }
}
