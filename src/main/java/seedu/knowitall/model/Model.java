package seedu.knowitall.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.knowitall.commons.core.GuiSettings;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.card.Answer;
import seedu.knowitall.model.card.Card;
import seedu.knowitall.storage.csvmanager.CsvFile;
import seedu.knowitall.storage.csvmanager.exceptions.CsvManagerNotInitialized;

/**
 * The API of the Model component.
 */
public interface Model extends Observable {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Card> PREDICATE_SHOW_ALL_CARDS = unused -> true;
    /** {@code Comparator} that sorts cards by ascending percentage score */
    Comparator<Card> COMPARATOR_ASC_SCORE_CARDS = Comparator.comparing(Card::getScore);
    /** {@code Comparator} that sorts cards by lexicographic order of questions */
    Comparator<Card> COMPARATOR_LEXICOGRAPHIC_CARDS = Comparator.comparing(Card::getQuestion);

    /**
     * Represents the location the user is in.
     */
    enum State {
        IN_FOLDER,
        IN_HOMEDIR,
        IN_TEST,
        IN_REPORT,
    }

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' card folder file path.
     */
    Path getcardFolderFilesPath();

    /**
     * Sets the user prefs' card folder file path.
     */
    void setcardFolderFilesPath(Path cardFolderFilesPath);

    /**
     * Replaces card folder data with the data in {@code cardFolder}.
     */
    void resetCardFolder(ReadOnlyCardFolder cardFolder);

    /** Returns the active CardFolder */
    ReadOnlyCardFolder getActiveCardFolder();

    /** Returns all CardFolders */
    List<ReadOnlyCardFolder> getCardFolders();

    /**
     * Returns true if a card with the same identity as {@code card} exists in the card folder.
     */
    boolean hasCard(Card card);

    /**
     * Deletes the given card.
     * The card must exist in the card folder.
     */
    void deleteCard(Card target);

    /**
     * Adds the given card.
     * {@code card} must not already exist in the card folder.
     */
    void addCard(Card card);

    /**
     * Replaces the given card {@code target} with {@code editedCard}.
     * {@code target} must exist in the card folder.
     * The card identity of {@code editedCard} must not be the same as another existing card in the card folder.
     */
    void setCard(Card target, Card editedCard);

    /**
     * Removes the selected card, if any.
     */
    void removeSelectedCard();

    /**
     * Returns true if a {@code CardFolder} with the same identity as {@code cardFolder} exists.
     */
    boolean hasFolder(CardFolder cardFolder);

    /**
     * Returns true if a{@code CardFolder} with the same name as {@code folderName} exists.
     */
    boolean hasFolderWithName(String name);

    /**
     * Deletes the folder at the given index.
     * The folder must exist.
     */
    void deleteFolder(int index);

    /**
     * Adds the given folder.
     * {@code cardFolder} must not already exist.
     */
    void addFolder(CardFolder cardFolder);

    /**
     * Renames the folder at the given index to the string passed.
     * {@code newName} must not be the name of an existing folder.
     */
    void renameFolder(int index, String newName);

    /**
     * Gets the index of the current active {@code CardFolder}.
     */
    int getActiveCardFolderIndex();

    /**
     * Enters the folder at the specified {@code index}.
     */
    void enterFolder(int index);

    /**
     * Sets the Model back to the home directory, outside of any {@code CardFolder}.
     */
    void exitFolderToHome();

    /** Returns an unmodifiable view of the filtered card list */
    ObservableList<Card> getFilteredCards();

    /** Returns an unmodifiable view of the filtered folders list */
    ObservableList<VersionedCardFolder> getFilteredFolders();

    /**
     * Updates the filter of the filtered card list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCard(Predicate<Card> predicate);

    /**
     * Updates the filter of the filtered card list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void sortFilteredCard(Comparator<Card> cardComparator);

    /**
     * Returns true if the model has previous card folder states to restore.
     */
    boolean canUndoActiveCardFolder();

    /**
     * Returns true if the model has undone card folder states to restore.
     */
    boolean canRedoActiveCardFolder();

    /**
     * Restores the model's card folder to its previous state.
     */
    void undoActiveCardFolder();

    /**
     * Restores the model's card folder to its previously undone state.
     */
    void redoActiveCardFolder();

    /**
     * Saves the current card folder state for undo/redo.
     */
    void commitActiveCardFolder();

    /**
     * Selected card in the filtered card list.
     * null if no card is selected.
     */
    ReadOnlyProperty<Card> selectedCardProperty();

    /**
     * Returns the selected card in the filtered card list.
     * null if no card is selected.
     */
    Card getSelectedCard();

    /**
     * Sets the selected card in the filtered card list.
     */
    void setSelectedCard(Card card);

    /**
     * Enters a test session using the specified card folder index.
     */
    void testCardFolder();

    /**
     * Sets the current card in the test session.
     */
    void setCurrentTestedCard(Card card);

    /**
     * Returns the current card in the test session
     * null if there is no cards in folder or user is not in a test session.
     */
    Card getCurrentTestedCard();

    /**
     * End the current test session.
     */
    void endTestSession();

    /**
     * Test the next card in the current folder in this test session.
     * Returns true if successfully found next card,
     * false if there is no next card.
     */
    boolean testNextCard();

    /**
     * Returns true if the given answer is right
     * false if answer is wrong
     */
    boolean markAttemptedAnswer(Answer attemptedAnswer);

    /**
     * Returns true if the index provided matches the answerIndex of the currently tested card,
     * false otherwise.
     */
    boolean markAttemptedMcqAnswer(int answerIndex);

    /**
     * Set cardAlreadyAnswered variable to true to indicate current card as answered
     */
    void setCardAsAnswered();

    /**
     * Returns true if the answer has already been input for that card
     * false if otherwise
     */
    boolean isCardAlreadyAnswered();

    State getState();

    void exportCardFolders(List<Integer> cardFolderExports) throws IOException, CsvManagerNotInitialized;

    void importCardFolders(CsvFile csvFile) throws IOException, CommandException;

    void enterReportDisplay();

    void exitReportDisplay();

    void setTestCsvPath() throws IOException;

    String getDefaultPath();


}
