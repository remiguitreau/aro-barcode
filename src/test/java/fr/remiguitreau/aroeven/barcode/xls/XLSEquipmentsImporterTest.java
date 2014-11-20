package fr.remiguitreau.aroeven.barcode.xls;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import fr.remiguitreau.aroeven.barcode.AroEquipment;

@RunWith(MockitoJUnitRunner.class)
public class XLSEquipmentsImporterTest {

    @InjectMocks
    private XLSEquipmentsImporter importer;

    @Test
    public void test_equipment_import() {
        final ImportResult result = importer.importEquipmentsFromInputStream(XLSEquipmentsImporterTest.class.getResourceAsStream("equipments_ok.xlsx"));

        Assert.assertEquals(4, result.getEquipments().size());

        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "5", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Head", "110", "4", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "3", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "6", false)));
        Assert.assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void test_equipment_import_with_some_bad_lines() {
        final ImportResult result = importer.importEquipmentsFromInputStream(XLSEquipmentsImporterTest.class.getResourceAsStream("equipments_bad.xlsx"));

        Assert.assertEquals(1, result.getEquipments().size());
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "3", true)));
        Assert.assertEquals(3, result.getErrors().size());
    }

}
