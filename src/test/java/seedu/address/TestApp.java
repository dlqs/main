package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.LogicManager;
import seedu.address.model.CardFolder;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyCardFolder;
import seedu.address.model.UserPrefs;
import seedu.address.storage.CardFolderStorage;
import seedu.address.storage.JsonCardFolderStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.testutil.TestUtil;
import seedu.address.ui.UiManager;
import systemtests.ModelHelper;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final Path SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.json");

    protected static final Path DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    protected Supplier<ReadOnlyCardFolder> initialDataSupplier = () -> null;
    protected Path saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyCardFolder> initialDataSupplier, Path saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            JsonCardFolderStorage jsonCardFolderStorage = new JsonCardFolderStorage(saveFileLocation);
            try {
                jsonCardFolderStorage.saveCardFolder(initialDataSupplier.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
    }

    @Override
    public void init() throws Exception {
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        List<CardFolderStorage> cardFolderStorageList = new ArrayList<>();

        Path cardFolderFilesPath = userPrefs.getcardFolderFilesPath();
        cardFolderStorageList.add(new JsonCardFolderStorage(cardFolderFilesPath));

        storage = new StorageManager(cardFolderStorageList, userPrefsStorage);

        initLogging(config);

        model = initModelManager(false, storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    @Override
    protected Config initConfig(Path configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.setGuiSettings(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        userPrefs.setcardFolderFilesPath(saveFileLocation);
        return userPrefs;
    }

    /**
     * Returns a defensive copy of the card folder data stored inside the storage file.
     */
    public CardFolder readStorageCardFolder() {
        try {
            // TODO: Address hardcoding in the following line
            return new CardFolder(storage.readCardFolders().get(0));
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the CardFolder format.", dce);
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.", ioe);
        }
    }

    /**
     * Returns the file path of the storage file.
     */
    public Path getStorageSaveLocation() {
        return storage.getcardFolderFilesPath();
    }

    /**
     * Returns a defensive copy of the model.
     */
    public Model getModel() {
        Model copy = new ModelManager(Collections.singletonList(model.getActiveCardFolder()),
                new UserPrefs());
        ModelHelper.setFilteredList(copy, model.getFilteredCards());
        return copy;
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
