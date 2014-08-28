package fr.remiguitreau.aroeven.barcode;

import org.apache.commons.io.IOUtils;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AroBarcodeGenerator {

    public void generateBarcode(final File rootFolder, final AroEquipment equipment) {
        try {
            final Code39Bean bean = new Code39Bean();
            final String barcode = AroBarcodeUtils.buildBarcodeFromEquipment(equipment);

            final int dpi = 150;

            // Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); // makes the narrow
            // bar
            // width exactly one
            // pixel
            bean.setWideFactor(3);
            bean.doQuietZone(false);

            // Open output file
            final OutputStream out = new FileOutputStream(new File(rootFolder, barcode + ".png"));
            try {
                // Set up the canvas provider for monochrome PNG output
                final BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
                        BufferedImage.TYPE_BYTE_BINARY, false, 0);

                // Generate the barcode
                bean.generateBarcode(canvas, barcode);

                // Signal end of generation
                canvas.finish();
            } finally {
                IOUtils.closeQuietly(out);
            }
        } catch (final Exception ex) {
            throw new BarcodeGenerationException("Error while generating barcode for equipment " + equipment,
                    ex);
        }
    }

    public static void main(final String[] args) {
        new AroBarcodeGenerator().generateBarcode(new File("/tmp"), new AroEquipment("Salomon", "120", "15",
                true));
    }
}
