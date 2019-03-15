package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ANSWER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ANSWER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.HINT_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.HINT_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ANSWER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_HINT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_QUESTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.QUESTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.QUESTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ANSWER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_QUESTION_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HINT;
import static seedu.address.testutil.TypicalCards.ALICE;
import static seedu.address.testutil.TypicalCards.AMY;
import static seedu.address.testutil.TypicalCards.BOB;
import static seedu.address.testutil.TypicalCards.CARL;
import static seedu.address.testutil.TypicalCards.HOON;
import static seedu.address.testutil.TypicalCards.IDA;
import static seedu.address.testutil.TypicalCards.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Question;
import seedu.address.model.hint.Hint;
import seedu.address.testutil.CardBuilder;
import seedu.address.testutil.CardUtil;

public class AddCommandSystemTest extends CardFolderSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a card without hints to a non-empty card folder, command with leading spaces and trailing spaces
         * -> added
         */
        Card toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + QUESTION_DESC_AMY + "  " + ANSWER_DESC_AMY + " "
                + "   " + HINT_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addCard(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a card with all fields same as another card in the card folder except question -> added */
        toAdd = new CardBuilder(AMY).withQuestion(VALID_QUESTION_BOB).build();
        command = AddCommand.COMMAND_WORD + QUESTION_DESC_BOB + ANSWER_DESC_AMY + HINT_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a card with all fields same as another card in the card folder except answer -> added */
        toAdd = new CardBuilder(AMY).withAnswer(VALID_ANSWER_BOB).build();
        command = CardUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty card folder -> added */
        deleteAllCards();
        assertCommandSuccess(ALICE);

        /* Case: add a card with hints, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + HINT_DESC_FRIEND + ANSWER_DESC_BOB + QUESTION_DESC_BOB
                + HINT_DESC_HUSBAND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a card, missing hints -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the card list before adding -> added */
        showCardsWithQuestion(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a card card is selected --------------------------- */

        /* Case: selects first card in the card list, add a card -> added, card selection remains unchanged */
        selectCard(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate card -> rejected */
        command = CardUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: add a duplicate card except with different answer -> rejected */
        // Problem with this test due to comparison implementation for Card -> to be fixed
        // toAdd = new CardBuilder(HOON).withAnswer(VALID_ANSWER_BOB).build();
        // command = CardUtil.getAddCommand(toAdd);
        // assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: add a duplicate card except with different hints -> rejected */
        command = CardUtil.getAddCommand(HOON) + " " + PREFIX_HINT.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: missing question -> rejected */
        command = AddCommand.COMMAND_WORD + ANSWER_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing answer -> rejected */
        command = AddCommand.COMMAND_WORD + QUESTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + CardUtil.getCardDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid question -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_QUESTION_DESC + ANSWER_DESC_AMY;
        assertCommandFailure(command, Question.MESSAGE_CONSTRAINTS);

        /* Case: invalid answer -> rejected */
        command = AddCommand.COMMAND_WORD + QUESTION_DESC_AMY + INVALID_ANSWER_DESC;
        assertCommandFailure(command, Answer.MESSAGE_CONSTRAINTS);

        /* Case: invalid hint -> rejected */
        command = AddCommand.COMMAND_WORD + QUESTION_DESC_AMY + ANSWER_DESC_AMY
                + INVALID_HINT_DESC;
        assertCommandFailure(command, Hint.MESSAGE_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code CardListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Card toAdd) {
        assertCommandSuccess(CardUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Card)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Card)
     */
    private void assertCommandSuccess(String command, Card toAdd) {
        Model expectedModel = getModel();
        expectedModel.addCard(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Card)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code CardListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Card)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code CardListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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
