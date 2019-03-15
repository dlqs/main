package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyCardFolder;
import seedu.address.model.card.Card;

/**
 * Test a card folder identified using it's displayed index from the card folder list.
 */
public class TestCommand extends Command {

    public static final String COMMAND_WORD = "test";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Tests the card folder identified by the index number used in the displayed card folders list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ENTER_TEST_FOLDER_SUCCESS = "In Test Session";

    private final Index targetIndex;

    public TestCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<ReadOnlyCardFolder> cardFoldersList = model.getCardFolders();

        if (targetIndex.getZeroBased() >= cardFoldersList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOLDER_DISPLAYED_INDEX);
        }

        if (model.checkIfInsideTestSession()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_INSIDE_TEST_SESSION);
        }

        ReadOnlyCardFolder cardFolderToTest = cardFoldersList.get(targetIndex.getZeroBased());
        model.testCardFolder(cardFolderToTest);
        Card cardToTest = model.getCurrentTestedCard();
        return new CommandResult(MESSAGE_ENTER_TEST_FOLDER_SUCCESS, false, false, false, cardToTest, false,
                AnswerCommandResultType.NOT_ANSWER_COMMAND);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TestCommand // instanceof handles nulls
                && targetIndex.equals(((TestCommand) other).targetIndex)); // state check
    }
}
