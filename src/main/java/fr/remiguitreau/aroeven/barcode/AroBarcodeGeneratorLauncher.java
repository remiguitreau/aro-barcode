package fr.remiguitreau.aroeven.barcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.barcode.export.DocxBarcodeExporter;
import fr.remiguitreau.aroeven.barcode.xls.ImportResult;
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
                final ImportResult result = new XLSEquipmentsImporter().importEquipmentsFromInputStream(new FileInputStream(
                        fileChooser.getSelectedFile()));
                log.info("{} equipments have been imported with {} erros !", result.getEquipments().size(),
                        result.getErrors().size());
                if (result.getErrors().isEmpty()
                        || JOptionPane.showConfirmDialog(null, buildErrorsMessage(result.getErrors()),
                                "Générer les codes barres malgré les erreurs ?", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {

                    final File dir = new File("barcodes_"
                            + new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss").format(new Date()));
                    dir.mkdir();
                    final ProgressMonitor progressMonitor = new ProgressMonitor(null,
                            "Génération des codes barres", "...", 1, result.getEquipments().size());
                    progressMonitor.setMillisToDecideToPopup(0);

                    new DocxBarcodeExporter(barcodeGenerator).export(dir, result.getEquipments(),
                            new BarcodeExportListener() {
                                int progress = 1;

                                @Override
                                public void newAroBarcodeGenerate(final AroEquipment equipment) {
                                    log.info(" - {}", AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
                                    progressMonitor.setNote("Barcode " + progress + "/"
                                            + result.getEquipments().size() + " : "
                                            + AroBarcodeUtils.buildBarcodeFromEquipment(equipment));
                                    progressMonitor.setProgress(progress++);
                                }

                            });
                } else {
                    log.info("Génération annulée");
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

    private static Object buildErrorsMessage(final Map<Integer, String> errors) {
        final List<String> list = new ArrayList<>();
        final List<Integer> lines = new ArrayList<>(errors.keySet());
        Collections.sort(lines);
        for (final int line : lines) {
            list.add("Ligne '" + (line + 1) + "' : " + errors.get(line));
        }
        return new JList(list.toArray());
    }
}
