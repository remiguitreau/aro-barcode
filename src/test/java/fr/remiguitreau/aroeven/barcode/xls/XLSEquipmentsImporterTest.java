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

        Assert.assertEquals(18, result.getEquipments().size());

        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "1", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "2", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "3", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "4", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "110", "5", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Head", "110", "6", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Head", "110", "7", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Head", "110", "8", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Head", "110", "9", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "1", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "2", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "3", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "1", false)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "2", false)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "3", false)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "4", false)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "5", false)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Helmet", "XL", "6", false)));
        Assert.assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void test_equipment_import_with_some_bad_lines() {
        final ImportResult result = importer.importEquipmentsFromInputStream(XLSEquipmentsImporterTest.class.getResourceAsStream("equipments_bad.xlsx"));

        Assert.assertEquals(3, result.getEquipments().size());
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "1", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "2", true)));
        Assert.assertTrue(result.getEquipments().contains(new AroEquipment("Salomon", "120", "3", true)));
        Assert.assertEquals(3, result.getErrors().size());
    }

}
