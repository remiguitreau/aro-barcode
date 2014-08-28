package fr.remiguitreau.aroeven.barcode;

import org.junit.Assert;
import org.junit.Test;

public class AroBarcodeUtilsTest {

    @Test
    public void test_barcode_generation() {
        Assert.assertEquals("ARO-Salomon-120-1",
                AroBarcodeUtils.buildBarcodeFromEquipment(new AroEquipment("Salomon", "120", "1", true)));
    }
}
