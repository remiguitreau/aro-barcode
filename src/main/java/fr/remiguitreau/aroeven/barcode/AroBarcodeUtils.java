package fr.remiguitreau.aroeven.barcode;

public class AroBarcodeUtils {

    public static String buildBarcodeFromEquipment(final AroEquipment equipment) {
        return equipment.getBrand() + "-" + equipment.getSize() + "-" + equipment.getNumber();
    }
}
