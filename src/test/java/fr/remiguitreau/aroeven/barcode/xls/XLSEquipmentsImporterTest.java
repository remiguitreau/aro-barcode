package fr.remiguitreau.aroeven.barcode.xls;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import fr.remiguitreau.aroeven.barcode.AroEquipment;

@RunWith(MockitoJUnitRunner.class)
public class XLSEquipmentsImporterTest {

    @InjectMocks
    private XLSEquipmentsImporter importer;

    @Test
    public void test_equipment_import() {
        final List<AroEquipment> equipments = importer.importEquipmentsFromInputStream(XLSEquipmentsImporterTest.class.getResourceAsStream("equipments_ok.xlsx"));

        Assert.assertEquals(14, equipments.size());
        checkEquipements("Salomon", "120", 3, true, equipments);
        checkEquipements("Salomon", "110", 5, true, equipments);
        checkEquipements("Helmet", "XL", 6, false, equipments);
    }

    // -------------------------------------------------------------------------

    private void checkEquipements(final String expectedBrand, final String expectedSize, final int number,
            final boolean pair, final List<AroEquipment> equipments) {
        for (int i = 1; i <= number; i++) {
            Assert.assertTrue(equipments.contains(new AroEquipment(expectedBrand, expectedSize,
                    String.valueOf(i), pair)));
        }
    }
}
