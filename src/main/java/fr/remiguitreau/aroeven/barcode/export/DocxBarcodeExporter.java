package fr.remiguitreau.aroeven.barcode.export;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.barcode.AroBarcodeGenerator;
import fr.remiguitreau.aroeven.barcode.AroBarcodeUtils;
import fr.remiguitreau.aroeven.barcode.AroEquipment;
import fr.remiguitreau.aroeven.barcode.BarcodeExportListener;

@RequiredArgsConstructor
@Slf4j
public class DocxBarcodeExporter {

    private final AroBarcodeGenerator barcodeGenerator;

    public void export(final File rootFolder, final List<AroEquipment> equipments,
            final BarcodeExportListener barcodeExportListener) {
        int i = 0;
        try {
            final XWPFDocument doc = new XWPFDocument(
                    DocxBarcodeExporter.class.getResourceAsStream("model.docx"));
            while (!equipments.isEmpty()) {
                final CTTbl ctTbl = CTTbl.Factory.newInstance();
                ctTbl.set(loadNewTable().getCTTbl());
                final XWPFTable table = new XWPFTable(ctTbl, doc);
                fillTable(table, equipments, rootFolder, barcodeExportListener);
                if (i > 0) {
                    doc.createParagraph();
                    doc.createTable(); // Create a empty table in the document
                }
                doc.setTable(i, table); // Replace the empty table to table2
                i++;
            }
            final FileOutputStream out = new FileOutputStream(new File(rootFolder, "arobarcodeExport.docx"));
            doc.write(out);
            out.close();
        } catch (final Exception ex) {
            throw new AroBarcodeExportException(ex);
        }
    }

    private XWPFTable loadNewTable() throws IOException {
        return new XWPFDocument(DocxBarcodeExporter.class.getResourceAsStream("model.docx")).getTables().get(
                0);
    }

    private void fillTable(final XWPFTable table, final List<AroEquipment> equipments, final File rootFolder,
            final BarcodeExportListener barcodeExportListener) throws IOException, InvalidFormatException {
        int row = 0;
        int col = 0;
        log.info("-------- Fill table");
        for (final Iterator<AroEquipment> it = equipments.iterator(); it.hasNext() && row < 12;) {
            final AroEquipment equipment = it.next();
            log.info("       {}x{} = {}", row, col, AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
            fillCellWithBarcode(table, row, col, equipment);
            col += 2;
            if (col == 8) {
                row++;
                col = 0;
            }
            if (equipment.isPair()) {
                fillCellWithBarcode(table, row, col, equipment);
                col += 2;
                if (col == 8) {
                    row++;
                    col = 0;
                }
            }
            barcodeExportListener.newAroBarcodeGenerate(equipment);
            it.remove();
        }
    }

    private void fillCellWithBarcode(final XWPFTable table, final int row, final int col,
            final AroEquipment equipment) throws InvalidFormatException, IOException {
        final XWPFTableCell cell = table.getRow(row).getCell(col);
        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        final XWPFParagraph p1 = cell.getParagraphs().get(0);
        p1.setAlignment(ParagraphAlignment.LEFT);
        p1.setVerticalAlignment(TextAlignment.CENTER);
        final XWPFRun r1 = p1.createRun();
        final byte[] barcode = barcodeGenerator.generateBarcode(equipment);
        r1.addPicture(new ByteArrayInputStream(barcode), XWPFDocument.PICTURE_TYPE_PNG, "aroeven",
                Units.toEMU(119), Units.toEMU(34)); // 200x200
        r1.addBreak();
        r1.addPicture(DocxBarcodeExporter.class.getResourceAsStream("aroeven_470x199.png"),
                XWPFDocument.PICTURE_TYPE_PNG, "aroeven", Units.toEMU(48), Units.toEMU(20)); // 200x200
        r1.setFontSize(10);
        r1.setText(AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
    }

    public static void main(final String[] args) {
        final List<AroEquipment> equipments = new LinkedList<AroEquipment>();
        for (int i = 0; i < 50; i++) {
            equipments.add(new AroEquipment("Ski", "78", String.valueOf(i), i % 2 == 0));
        }
        new DocxBarcodeExporter(new AroBarcodeGenerator()).export(new File("/home/rgu/Bureau/arobarcode"),
                equipments, new BarcodeExportListener() {
                    @Override
                    public void newAroBarcodeGenerate(final AroEquipment equipment) {

                    }
                });
    }
}
