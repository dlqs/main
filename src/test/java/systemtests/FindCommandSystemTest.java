package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_CARDS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalCards.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

public class FindCommandSystemTest extends CardFolderSystemTest {

    @Test
    public void find() {
        /* Case: find multiple cards in card folder, command with leading spaces and trailing spaces
         * -> 2 cards found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where card list is displaying the cards we are finding
         * -> 2 cards found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find card where card list is not displaying the card we are finding -> 1 card found */
        command = FindCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple cards in card folder, 2 keywords -> 2 cards found */
        command = FindCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple cards in card folder, 2 keywords in reversed order -> 2 cards found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple cards in card folder, 2 keywords with 1 repeat -> 2 cards found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple cards in card folder, 2 matching keywords and 1 non-matching keyword
         * -> 2 cards found
         */
        command = FindCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same cards in card folder after deleting 1 of them -> 1 card found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getCardFolder().getCardList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find card in card folder, keyword is same as question but of different case -> 1 card found */
        command = FindCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find card in card folder, keyword is substring of question -> 0 cards found */
        command = FindCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find card in card folder, question is substring of keyword -> 0 cards found */
        command = FindCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find card not in card folder -> 0 cards found */
        command = FindCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find answer number of card in card folder -> 0 cards found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getAnswer().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of card in card folder -> 0 cards found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of card in card folder -> 0 cards found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a card is selected -> selected card deselected */
        showAllCards();
        selectCard(Index.fromOneBased(1));
        assertFalse(
                getCardListPanel().getHandleToSelectedCard().getQuestion().equals(DANIEL.getQuestion().fullQuestion));
        command = FindCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find card in empty card folder -> 0 cards found */
        deleteAllCards();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_CARDS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_CARDS_LISTED_OVERVIEW, expectedModel.getFilteredCardList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
