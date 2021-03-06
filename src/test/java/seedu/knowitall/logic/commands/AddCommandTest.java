package seedu.knowitall.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.knowitall.commons.core.GuiSettings;
import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.CardFolder;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.ReadOnlyCardFolder;
import seedu.knowitall.model.ReadOnlyUserPrefs;
import seedu.knowitall.model.VersionedCardFolder;
import seedu.knowitall.model.card.Answer;
import seedu.knowitall.model.card.Card;
import seedu.knowitall.storage.csvmanager.CsvFile;
import seedu.knowitall.testutil.CardBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_cardAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCardAdded modelStub = new ModelStubAcceptingCardAdded();
        Card validCard = new CardBuilder().build();

        CommandResult commandResult = new AddCommand(validCard).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validCard), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validCard), modelStub.cardsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateCard_throwsCommandException() throws Exception {
        Card validCard = new CardBuilder().build();
        AddCommand addCommand = new AddCommand(validCard);
        ModelStub modelStub = new ModelStubWithCard(validCard);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_CARD);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Card alice = new CardBuilder().withQuestion("Alice").build();
        Card bob = new CardBuilder().withQuestion("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different card -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getcardFolderFilesPath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setcardFolderFilesPath(Path cardFolderFilesPath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addCard(Card card) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetCardFolder(ReadOnlyCardFolder newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyCardFolder getActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<ReadOnlyCardFolder> getCardFolders() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void testCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Card getCurrentTestedCard() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentTestedCard(Card card) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void endTestSession() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean testNextCard() {
            throw new AssertionError("This method should not be called.");
        }

        public boolean markAttemptedAnswer(Answer attemptedAnswer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean markAttemptedMcqAnswer(int answerIndex) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCardAsAnswered() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isCardAlreadyAnswered() {
            throw new AssertionError("This method should not be called.");
        }

        public void exportCardFolders(List<Integer> cardFolderExports) throws IOException {
            throw new AssertionError("This method should not be called.");
        }

        public boolean inReportDisplay() {
            throw new AssertionError("This method should not be called.");
        };

        @Override
        public void enterReportDisplay() {
            throw new AssertionError("This method should not be called.");
        };

        @Override
        public void exitReportDisplay() {
            throw new AssertionError("This method should not be called.");
        };


        @Override
        public void importCardFolders(CsvFile csvFile) throws IOException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTestCsvPath() throws IOException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getDefaultPath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasCard(Card card) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteCard(Card target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCard(Card target, Card editedCard) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeSelectedCard() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasFolder(CardFolder cardFolder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasFolderWithName(String name) {
            return false;
        }

        @Override
        public void deleteFolder(int index) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addFolder(CardFolder cardFolder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void renameFolder(int index, String newName) {

        }

        @Override
        public int getActiveCardFolderIndex() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void enterFolder(int newIndex) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void exitFolderToHome() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Card> getFilteredCards() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<VersionedCardFolder> getFilteredFolders() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredCard(Predicate<Card> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortFilteredCard(Comparator<Card> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitActiveCardFolder() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Card> selectedCardProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Card getSelectedCard() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedCard(Card card) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public State getState() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single card.
     */
    private class ModelStubWithCard extends ModelStub {
        private final Card card;

        ModelStubWithCard(Card card) {
            requireNonNull(card);
            this.card = card;
        }

        @Override
        public State getState() {
            return State.IN_FOLDER;
        }

        @Override
        public boolean hasCard(Card card) {
            requireNonNull(card);
            return this.card.isSameCard(card);
        }

    }

    /**
     * A Model stub that always accept the card being added.
     */
    private class ModelStubAcceptingCardAdded extends ModelStub {
        final ArrayList<Card> cardsAdded = new ArrayList<>();

        @Override
        public boolean hasCard(Card card) {
            requireNonNull(card);
            return cardsAdded.stream().anyMatch(card::isSameCard);
        }

        @Override
        public void addCard(Card card) {
            requireNonNull(card);
            cardsAdded.add(card);
        }

        @Override
        public State getState() {
            return State.IN_FOLDER;
        }

        @Override
        public void commitActiveCardFolder() {
            // called by {@code AddCommand#execute()}
        }
    }

}
