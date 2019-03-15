package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANSWER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLDERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HINT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUESTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CardFolder;
import seedu.address.model.Model;
import seedu.address.model.card.Card;
import seedu.address.model.card.QuestionContainsKeywordsPredicate;
import seedu.address.testutil.EditCardDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_QUESTION_AMY = "Amy Bee";
    public static final String VALID_QUESTION_BOB = "Bob Choo";
    public static final String VALID_ANSWER_AMY = "11111111";
    public static final String VALID_ANSWER_BOB = "22222222";
    public static final String VALID_SCORE_AMY = "0/0";
    public static final String VALID_HINT_HUSBAND = "husband";
    public static final String VALID_HINT_FRIEND = "friend";
    public static final String VALID_FOLDER_NAME_1 = "Sample Folder 1";
    public static final String VALID_FOLDER_NAME_2 = "Sample Folder 2";
    public static final String VALID_FILENAME = "sample_folder.csv";
    public static final String INVALID_FILENAME = "sample_folder.json";

    public static final String QUESTION_DESC_AMY = " " + PREFIX_QUESTION + VALID_QUESTION_AMY;
    public static final String QUESTION_DESC_BOB = " " + PREFIX_QUESTION + VALID_QUESTION_BOB;
    public static final String ANSWER_DESC_AMY = " " + PREFIX_ANSWER + VALID_ANSWER_AMY;
    public static final String ANSWER_DESC_BOB = " " + PREFIX_ANSWER + VALID_ANSWER_BOB;
    public static final String HINT_DESC_FRIEND = " " + PREFIX_HINT + VALID_HINT_FRIEND;
    public static final String HINT_DESC_HUSBAND = " " + PREFIX_HINT + VALID_HINT_HUSBAND;
    public static final String FOLDER_DESC_SAMPLE_1 = " " + PREFIX_FOLDERNAME + VALID_FOLDER_NAME_1;
    public static final String FOLDER_DESC_SAMPLE_2 = " " + PREFIX_FOLDERNAME + VALID_FOLDER_NAME_2;
    public static final String FILENAME_DESC_SAMPLE = " " + PREFIX_FILENAME + VALID_FILENAME;
    public static final String INVALID_FILENAME_EXT = " " + PREFIX_FILENAME + INVALID_FILENAME;

    public static final String INVALID_QUESTION_DESC = " " + PREFIX_QUESTION; // empty string not allowed for questions
    public static final String INVALID_ANSWER_DESC = " " + PREFIX_ANSWER; // empty string not allowed for answers

    public static final String INVALID_HINT_DESC = " " + PREFIX_HINT + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_FOLDER_DESC = " " + PREFIX_FOLDERNAME;
    public static final String INVALID_FILENAME_DESC = " " + PREFIX_FILENAME;


    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditCardDescriptor DESC_AMY;
    public static final EditCommand.EditCardDescriptor DESC_BOB;



    static {
        DESC_AMY = new EditCardDescriptorBuilder().withQuestion(VALID_QUESTION_AMY)
                .withAnswer(VALID_ANSWER_AMY).withHint(VALID_HINT_FRIEND).build();
        DESC_BOB = new EditCardDescriptorBuilder().withQuestion(VALID_QUESTION_BOB)
                .withAnswer(VALID_ANSWER_BOB).withHint(VALID_HINT_HUSBAND, VALID_HINT_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the card folder, filtered card list and selected card in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        CardFolder expectedCardFolder = new CardFolder(actualModel.getActiveCardFolder());
        List<Card> expectedFilteredList = new ArrayList<>(actualModel.getFilteredCards());
        Card expectedSelectedCard = actualModel.getSelectedCard();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedCardFolder, actualModel.getActiveCardFolder());
            assertEquals(expectedFilteredList, actualModel.getFilteredCards());
            assertEquals(expectedSelectedCard, actualModel.getSelectedCard());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the card at the given {@code targetIndex} in the
     * {@code model}'s card folder.
     */
    public static void showCardAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredCards().size());

        Card card = model.getFilteredCards().get(targetIndex.getZeroBased());
        final String[] splitQuestion = card.getQuestion().fullQuestion.split("\\s+");
        model.updateFilteredCard(new QuestionContainsKeywordsPredicate(Arrays.asList(splitQuestion[0])));

        assertEquals(1, model.getFilteredCards().size());
    }

    /**
     * Deletes the first card in {@code model}'s filtered list from {@code model}'s card folder.
     */
    public static void deleteFirstCard(Model model) {
        Card firstCard = model.getFilteredCards().get(0);
        model.deleteCard(firstCard);
        model.commitActiveCardFolder();
    }

}
