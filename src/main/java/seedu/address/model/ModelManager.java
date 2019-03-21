package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.exceptions.CardNotFoundException;
import seedu.address.storage.csvmanager.CardFolderExport;
import seedu.address.storage.csvmanager.CsvFile;
import seedu.address.storage.csvmanager.CsvManager;

/**
 * Represents the in-memory model of the card folder data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    private final UserPrefs userPrefs;

    // CardFolder related
    private ObservableList<VersionedCardFolder> folders;
    private final FilteredList<VersionedCardFolder> filteredFolders;
    private final List<FilteredList<Card>> filteredCardsList;
    private int activeCardFolderIndex;
    private boolean inFolder;

    // Test Session related
    private final SimpleObjectProperty<Card> selectedCard = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Card> currentTestedCard = new SimpleObjectProperty<>();
    private boolean insideTestSession = false;
    private boolean cardAlreadyAnswered = false;

    // Export related
    private CsvManager csvManager = new CsvManager();

    /**
     * Initializes a ModelManager with the given cardFolders and userPrefs.
     */
    public ModelManager(List<ReadOnlyCardFolder> cardFolders, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(cardFolders, userPrefs);

        logger.fine("Initializing with card folder: " + cardFolders + " and user prefs " + userPrefs);

        List<VersionedCardFolder> versionedCardFolders = new ArrayList<>();
        for (ReadOnlyCardFolder cardFolder : cardFolders) {
            versionedCardFolders.add(new VersionedCardFolder(cardFolder));
        }
        folders = FXCollections.observableArrayList(versionedCardFolders);
        filteredFolders = new FilteredList<>(folders);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredCardsList = new ArrayList<>();
        for (int i = 0; i < filteredFolders.size(); i++) {
            FilteredList<Card> filteredCards = new FilteredList<>(filteredFolders.get(i).getCardList());
            filteredCardsList.add(filteredCards);
            filteredCards.addListener(this::ensureSelectedCardIsValid);
        }


        // ModelManager initialises to homepage
        activeCardFolderIndex = 0;
        inFolder = true;
    }

    public ModelManager(String newFolderName) {
        this(Collections.singletonList(new CardFolder(newFolderName)), new UserPrefs());
    }

    private VersionedCardFolder getActiveVersionedCardFolder() {
        return folders.get(activeCardFolderIndex);
    }

    private FilteredList<Card> getActiveFilteredCards() {
        // TODO: Only return if inFolder
        return filteredCardsList.get(activeCardFolderIndex);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getcardFolderFilesPath() {
        return userPrefs.getcardFolderFilesPath();
    }

    @Override
    public void setcardFolderFilesPath(Path cardFolderFilesPath) {
        requireNonNull(cardFolderFilesPath);
        userPrefs.setcardFolderFilesPath(cardFolderFilesPath);
    }

    //=========== CardFolder ================================================================================

    @Override
    public void resetCardFolder(ReadOnlyCardFolder cardFolder) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.resetData(cardFolder);
    }

    @Override
    public ReadOnlyCardFolder getActiveCardFolder() {
        return getActiveVersionedCardFolder();
    }

    @Override
    public List<ReadOnlyCardFolder> getCardFolders() {
        return new ArrayList<>(filteredFolders);
    }

    @Override
    public boolean hasCard(Card card) {
        requireNonNull(card);

        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.hasCard(card);
    }

    @Override
    public void deleteCard(Card target) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.removeCard(target);
    }

    @Override
    public void addCard(Card card) {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.addCard(card);
        updateFilteredCard(PREDICATE_SHOW_ALL_CARDS);
    }

    @Override
    public void setCard(Card target, Card editedCard) {
        requireAllNonNull(target, editedCard);

        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.setCard(target, editedCard);
    }

    @Override
    public boolean hasFolder(CardFolder cardFolder) {
        requireNonNull(cardFolder);

        for (VersionedCardFolder versionedCardFolder : filteredFolders) {
            if (versionedCardFolder.hasSameFolderName(cardFolder)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteFolder(int index) {
        folders.remove(index);
        filteredCardsList.remove(index);
        indicateModified();
    }

    @Override
    public void addFolder(CardFolder cardFolder) {
        VersionedCardFolder versionedCardFolder = new VersionedCardFolder(cardFolder);
        folders.add(versionedCardFolder);
        FilteredList<Card> filteredCards = new FilteredList<>(versionedCardFolder.getCardList());
        filteredCardsList.add(filteredCards);
        filteredCards.addListener(this::ensureSelectedCardIsValid);
        indicateModified();
    }

    @Override
    public int getActiveCardFolderIndex() {
        return activeCardFolderIndex;
    }

    @Override
    public void setActiveCardFolderIndex(int newIndex) {
        inFolder = true;
        activeCardFolderIndex = newIndex;
    }

    @Override
    public void exitFoldersToHome() {
        inFolder = false;
    }

    @Override
    public boolean isInFolder() {
        return inFolder;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the list of card folders has been modified.
     */
    private void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //=========== Filtered Card List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Card} backed by the internal list of
     * {@code filteredFolders}
     */
    @Override
    public ObservableList<Card> getFilteredCards() {
        return getActiveFilteredCards();
    }


    @Override
    public void updateFilteredCard(Predicate<Card> predicate) {
        requireNonNull(predicate);
        FilteredList<Card> filteredCards = getActiveFilteredCards();
        filteredCards.setPredicate(predicate);
    }
    @Override
    public void sortFilteredCard(Comparator<Card> cardComparator) {
        requireNonNull(cardComparator);
        folders.get(activeCardFolderIndex).sortCards(cardComparator);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.canUndo();
    }

    @Override
    public boolean canRedoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        return versionedCardFolder.canRedo();
    }

    @Override
    public void undoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.undo();
    }

    @Override
    public void redoActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.redo();
    }

    @Override
    public void commitActiveCardFolder() {
        VersionedCardFolder versionedCardFolder = getActiveVersionedCardFolder();
        versionedCardFolder.commit();
    }

    //=========== Test Session ===========================================================================

    @Override
    public void testCardFolder(int cardFolderToTestIndex) {
        ObservableList<Card> currentTestedCardFolder = getActiveCardFolder().getCardList();
        Card cardToTest = currentTestedCardFolder.get(0);
        setCurrentTestedCard(cardToTest);
        insideTestSession = true;
    }

    @Override
    public void setCurrentTestedCard(Card card) {
        if (card != null && !getActiveFilteredCards().contains(card)) {
            throw new CardNotFoundException();
        }
        currentTestedCard.setValue(card);
    }

    @Override
    public Card getCurrentTestedCard() {
        return currentTestedCard.getValue();
    }

    @Override
    public void endTestSession() {
        insideTestSession = false;
        cardAlreadyAnswered = false;
        setCurrentTestedCard(null);
        //TODO: exit card folder
    }

    @Override
    public boolean markAttemptedAnswer(Answer attemptedAnswer) {
        Answer correctAnswer = currentTestedCard.getValue().getAnswer();
        String correctAnswerInCapitals = correctAnswer.toString().toUpperCase();
        String attemptedAnswerInCapitals = attemptedAnswer.toString().toUpperCase();

        //LOOSEN MORE CRITERIAS?
        if (correctAnswerInCapitals.equals(attemptedAnswerInCapitals)) {
            return true;
        }
        return false;
    }

    @Override
    public void setCardAsAnswered() {
        cardAlreadyAnswered = true;
    }

    @Override
    public boolean checkIfCardAlreadyAnswered() {
        return cardAlreadyAnswered;
    }

    @Override
    public boolean checkIfInsideTestSession() {
        return insideTestSession;
    }

    //=========== Selected card ===========================================================================

    @Override
    public ReadOnlyProperty<Card> selectedCardProperty() {
        return selectedCard;
    }

    @Override
    public Card getSelectedCard() {
        return selectedCard.getValue();
    }

    @Override
    public void setSelectedCard(Card card) {
        if (card != null && !getActiveFilteredCards().contains(card)) {
            throw new CardNotFoundException();
        }
        selectedCard.setValue(card);
    }

    /**
     * Ensures {@code selectedCard} is a valid card in {@code filteredCardsList}.
     */
    private void ensureSelectedCardIsValid(ListChangeListener.Change<? extends Card> change) {
        while (change.next()) {
            if (selectedCard.getValue() == null) {
                // null is always a valid selected card, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedCardReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedCard.getValue());
            if (wasSelectedCardReplaced) {
                // Update selectedCard to its new value.
                int index = change.getRemoved().indexOf(selectedCard.getValue());
                selectedCard.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedCardRemoved = change.getRemoved().stream()
                    .anyMatch(removedCard -> selectedCard.getValue().isSameCard(removedCard));
            if (wasSelectedCardRemoved) {
                // Select the card that came before it in the list,
                // or clear the selection if there is no such card.
                selectedCard.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return filteredFolders.equals(other.filteredFolders)
                && userPrefs.equals(other.userPrefs)
                && filteredCardsList.equals(other.filteredCardsList)
                && Objects.equals(selectedCard.get(), other.selectedCard.get());
    }


    //=========== Export / Import card folders ========================================================================
    @Override
    public void exportCardFolders(Set<CardFolderExport> cardFolderExports, CsvFile csvFile) throws IOException {
        List<ReadOnlyCardFolder> cardFolderObject = returnValidCardFolders(cardFolderExports);
        csvManager.writeFoldersToCsv(cardFolderObject, csvFile);
    }

    @Override
    public void importCardFolders(CsvFile csvFile) throws IOException {

    }

    @Override
    public List<ReadOnlyCardFolder> returnValidCardFolders(Set<CardFolderExport> cardFolders) {
        List<ReadOnlyCardFolder> returnCardFolder = new ArrayList<>();
        for (CardFolderExport cardFolderExport : cardFolders) {
            addCardFolder(cardFolderExport, returnCardFolder);
        }
        return returnCardFolder;
    }


    /**
     * Private method to check if name of card folder to export matches name of ReadOnlyCardFolder in model.
     * Throws card Folder not found exception if card folder cannot be found.
     */
    private void addCardFolder(CardFolderExport cardFolderExport, List<ReadOnlyCardFolder> returnCardFolders) {
        String exportFolderName = cardFolderExport.folderName;
        for (ReadOnlyCardFolder readOnlyCardFolder : filteredFolders) {
            if (readOnlyCardFolder.getFolderName().equals(exportFolderName)) {
                returnCardFolders.add(readOnlyCardFolder);
                return;
            }
        }
        throw new CardFolderNotFoundException(cardFolderExport.folderName);
    }
}
