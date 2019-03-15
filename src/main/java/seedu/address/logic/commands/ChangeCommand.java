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

/**
 * Selects a card identified using it's displayed index from the card folder.
 */
public class ChangeCommand extends Command {

    public static final String COMMAND_WORD = "change";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes the card folder that the user is in.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_FOLDER_SUCCESS = "Entered Card Folder: %1$s";

    private final Index targetIndex;

    public ChangeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<ReadOnlyCardFolder> cardFolderList = model.getCardFolders();

        if (targetIndex.getZeroBased() >= cardFolderList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOLDER_DISPLAYED_INDEX);
        }

        model.setActiveCardFolderIndex(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_SELECT_FOLDER_SUCCESS, targetIndex.getOneBased()),
                false, false, true, null, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeCommand // instanceof handles nulls
                && targetIndex.equals(((ChangeCommand) other).targetIndex)); // state check
    }
}
