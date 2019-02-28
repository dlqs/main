package systemtests;

import static org.junit.Assert.*;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;
import static seedu.address.testutil.TypicalCards.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CARD;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CARD;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Email;
import seedu.address.model.card.Question;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.CardBuilder;
import seedu.address.testutil.CardUtil;

public class EditCommandSystemTest extends CardFolderSystemTest {

    @Test
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_CARD;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + QUESTION_DESC_BOB + "  "
                + ANSWER_DESC_BOB + " " + EMAIL_DESC_BOB + " " + TAG_DESC_HUSBAND + " ";
        Card editedCard = new CardBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedCard);

        /* Case: undo editing the last card in the list -> last card restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last card in the list -> last card edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.setCard(getModel().getFilteredCardList().get(INDEX_FIRST_CARD.getZeroBased()), editedCard);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a card with new values same as existing values -> edited */
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                        + EMAIL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit a card with new values same as another card's values but with different question -> edited */
        assertTrue(getModel().getCardFolder().getCardList().contains(BOB));
        index = INDEX_SECOND_CARD;
        assertNotEquals(getModel().getFilteredCardList().get(index.getZeroBased()), BOB);
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_AMY + ANSWER_DESC_BOB
                        + EMAIL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedCard = new CardBuilder(BOB).withQuestion(VALID_QUESTION_AMY).build();
        assertCommandSuccess(command, index, editedCard);

        /* Case: edit a card with new values same as another card's values but with different answer and email
         * -> edited
         */
        index = INDEX_SECOND_CARD;
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_AMY
                        + EMAIL_DESC_AMY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedCard = new CardBuilder(BOB).withAnswer(VALID_ANSWER_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertCommandSuccess(command, index, editedCard);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_CARD;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Card cardToEdit = getModel().getFilteredCardList().get(index.getZeroBased());
        editedCard = new CardBuilder(cardToEdit).withTags().build();
        assertCommandSuccess(command, index, editedCard);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered card list, edit index within bounds of card folder and card list -> edited */
        showCardsWithQuestion(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_CARD;
        assertTrue(index.getZeroBased() < getModel().getFilteredCardList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + QUESTION_DESC_BOB;
        cardToEdit = getModel().getFilteredCardList().get(index.getZeroBased());
        editedCard = new CardBuilder(cardToEdit).withQuestion(VALID_QUESTION_BOB).build();
        assertCommandSuccess(command, index, editedCard);

        /* Case: filtered card list, edit index within bounds of card folder but out of bounds of card list
         * -> rejected
         */
        showCardsWithQuestion(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getCardFolder().getCardList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + QUESTION_DESC_BOB,
                Messages.MESSAGE_INVALID_CARD_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a card card is selected -------------------------- */

        /* Case: selects first card in the card list, edit a card -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllCards();
        index = INDEX_FIRST_CARD;
        selectCard(index);
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_AMY + ANSWER_DESC_AMY
                        + EMAIL_DESC_AMY + TAG_DESC_FRIEND;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new card's question
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + QUESTION_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + QUESTION_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredCardList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + QUESTION_DESC_BOB,
                Messages.MESSAGE_INVALID_CARD_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + QUESTION_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CARD.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid question -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CARD.getOneBased()
                + INVALID_QUESTION_DESC, Question.MESSAGE_CONSTRAINTS);

        /* Case: invalid answer -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CARD.getOneBased()
                + INVALID_ANSWER_DESC, Answer.MESSAGE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CARD.getOneBased()
                + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CARD.getOneBased()
                + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        /* Case: edit a card with new values same as another card's values -> rejected */
        executeCommand(CardUtil.getAddCommand(BOB));
        assertTrue(getModel().getCardFolder().getCardList().contains(BOB));
        index = INDEX_FIRST_CARD;
        assertFalse(getModel().getFilteredCardList().get(index.getZeroBased()).equals(BOB));
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                        + EMAIL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: edit a card with new values same as another card's values but with different tags -> rejected */
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                        + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: edit a card with new values same as another card's values but with different address -> rejected */
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                        + EMAIL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: edit a card with new values same as another card's values but with different answer -> rejected */
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_AMY
                        + EMAIL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CARD);

        /* Case: edit a card with new values same as another card's values but with different email -> rejected */
        command =
                EditCommand.COMMAND_WORD + " " + index.getOneBased() + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                        + EMAIL_DESC_AMY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CARD);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Card, Index)} except that
     * the browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Card, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Card editedCard) {
        assertCommandSuccess(command, toEdit, editedCard, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the card at index {@code toEdit} being
     * updated to values specified {@code editedCard}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Card editedCard,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.setCard(expectedModel.getFilteredCardList().get(toEdit.getZeroBased()), editedCard);
        expectedModel.updateFilteredCardList(PREDICATE_SHOW_ALL_CARDS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_CARD_SUCCESS, editedCard), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see CardFolderSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredCardList(PREDICATE_SHOW_ALL_CARDS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code CardFolderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
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
