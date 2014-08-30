package fr.remiguitreau.aroeven.barcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.barcode.xls.XLSEquipmentsImporter;

@Slf4j
public class AroBarcodeGeneratorLauncher {

    public static void main(final String[] args) {
        log.info("Launch barcode generation...");
        final AroBarcodeGenerator barcodeGenerator = new AroBarcodeGenerator();

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public String getDescription() {
                return "Fichiers Excel";
            }

            @Override
            public boolean accept(final File f) {
                return f.isDirectory() || f.getName().endsWith(".xlsx");
            }
        });
        fileChooser.showOpenDialog(null);
        if (fileChooser.getSelectedFile() != null) {
            log.info("Barcode generation will be done from Excel file'{}'",
                    fileChooser.getSelectedFile().getName());
            try {
                final List<AroEquipment> equipments = new XLSEquipmentsImporter().importEquipmentsFromInputStream(new FileInputStream(
                        fileChooser.getSelectedFile()));
                log.info("{} equipments have been imported !", equipments.size());
                final File dir = new File("barcodes_"
                        + new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss").format(new Date()));
                dir.mkdir();
                final ProgressMonitor progressMonitor = new ProgressMonitor(null,
                        "Génération des codes barres", "...", 1, equipments.size());
                progressMonitor.setMillisToDecideToPopup(0);
                int progress = 1;
                for (final AroEquipment equipment : equipments) {
                    log.info(" - {}", AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
                    progressMonitor.setNote("Barcode " + progress + "/" + equipments.size() + " : "
                            + AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
                    progressMonitor.setProgress(progress++);
                    barcodeGenerator.generateBarcode(dir, equipment);
                }
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de l'import du fichier : " + ex.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            log.info("No file selected...");
        }
    }
}
