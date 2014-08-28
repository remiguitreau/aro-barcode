package fr.remiguitreau.aroeven.barcode;

public class BarcodeGenerationException extends RuntimeException {

    public BarcodeGenerationException(final String message, final Exception cause) {
        super(message, cause);
    }

}
