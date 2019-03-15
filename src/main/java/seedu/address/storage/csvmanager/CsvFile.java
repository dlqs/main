package seedu.address.storage.csvmanager;


/**
 * Represents a Csv file for either import or export
 */
public class CsvFile {

    public static final String MESSAGE_CONSTRAINTS = "File name should not be left blank and should have"
            + ".csv format";

    public final String filename;

    public CsvFile(String filename) {
        this.filename = filename;
    }

    public static boolean isValidFileName(String filename) {
        return !isFileNameEmpty(filename) && isCorrectFileExtension(filename);
    }

    private static boolean isFileNameEmpty(String filename) {
        return filename.isEmpty();
    }


    /**
     * Returns true if file extension is of .csv format.
     */
    private static boolean isCorrectFileExtension(String filename) {
        return filename.split("\\.(?=[^\\.]+$)")[1].equals("csv");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof CsvFile && ((CsvFile) obj).filename.equals(this.filename);
    }

    @Override
    public int hashCode() {
        return filename.hashCode();
    }
}
