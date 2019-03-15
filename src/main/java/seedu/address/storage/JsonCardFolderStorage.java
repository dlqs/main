package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyCardFolder;

/**
 * A class to access CardFolder data stored as a json file on the hard disk.
 */
public class JsonCardFolderStorage implements CardFolderStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonCardFolderStorage.class);

    private Path filePath;

    public JsonCardFolderStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getcardFolderFilesPath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCardFolder> readCardFolder() throws DataConversionException, IOException {
        return readCardFolder(filePath);
    }

    /**
     * Similar to {@link #readCardFolder()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCardFolder> readCardFolder(Path filePath) throws DataConversionException, IOException {
        requireNonNull(filePath);

        Optional<JsonSerializableCardFolder> jsonCardFolder = JsonUtil.readJsonFile(
                filePath, JsonSerializableCardFolder.class);
        if (!jsonCardFolder.isPresent()) {
            logger.info("File not found at " + filePath);
            throw new IOException();
        }

        try {
            return Optional.of(jsonCardFolder.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public Optional<String> getCardFolderName() throws DataConversionException, IOException {
        Optional<ReadOnlyCardFolder> readOnlyCardFolder = readCardFolder();
        if (!readOnlyCardFolder.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(readOnlyCardFolder.get().getFolderName());
    }

    @Override
    public void saveCardFolder(ReadOnlyCardFolder cardFolder) throws IOException {
        saveCardFolder(cardFolder, filePath);
    }

    /**
     * Similar to {@link #saveCardFolder(ReadOnlyCardFolder)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveCardFolder(ReadOnlyCardFolder cardFolder, Path filePath) throws IOException {
        requireNonNull(cardFolder);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableCardFolder(cardFolder), filePath);
    }

    @Override
    public void deleteCardFolder(Path filePath) throws IOException {
        requireNonNull(filePath);

        FileUtil.deleteFile(filePath);
    }

}
