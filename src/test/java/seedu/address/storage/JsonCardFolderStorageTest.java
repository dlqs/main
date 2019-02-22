package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalCardFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.CardFolder;
import seedu.address.model.ReadOnlyCardFolder;

public class JsonCardFolderStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonCardFolderStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readCardFolder_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readCardFolder(null);
    }

    private java.util.Optional<ReadOnlyCardFolder> readCardFolder(String filePath) throws Exception {
        return new JsonCardFolderStorage(Paths.get(filePath)).readCardFolder(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readCardFolder("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readCardFolder("notJsonFormatCardFolder.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readCardFolder_invalidPersonCardFolder_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCardFolder("invalidPersonCardFolder.json");
    }

    @Test
    public void readCardFolder_invalidAndValidPersonCardFolder_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCardFolder("invalidAndValidPersonCardFolder.json");
    }

    @Test
    public void readAndSaveCardFolder_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempCardFolder.json");
        CardFolder original = getTypicalCardFolder();
        JsonCardFolderStorage jsonCardFolderStorage = new JsonCardFolderStorage(filePath);

        // Save in new file and read back
        jsonCardFolderStorage.saveCardFolder(original, filePath);
        ReadOnlyCardFolder readBack = jsonCardFolderStorage.readCardFolder(filePath).get();
        assertEquals(original, new CardFolder(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonCardFolderStorage.saveCardFolder(original, filePath);
        readBack = jsonCardFolderStorage.readCardFolder(filePath).get();
        assertEquals(original, new CardFolder(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonCardFolderStorage.saveCardFolder(original); // file path not specified
        readBack = jsonCardFolderStorage.readCardFolder().get(); // file path not specified
        assertEquals(original, new CardFolder(readBack));

    }

    @Test
    public void saveCardFolder_nullCardFolder_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveCardFolder(null, "SomeFile.json");
    }

    /**
     * Saves {@code cardFolder} at the specified {@code filePath}.
     */
    private void saveCardFolder(ReadOnlyCardFolder cardFolder, String filePath) {
        try {
            new JsonCardFolderStorage(Paths.get(filePath))
                    .saveCardFolder(cardFolder, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveCardFolder_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveCardFolder(new CardFolder(), null);
    }
}
