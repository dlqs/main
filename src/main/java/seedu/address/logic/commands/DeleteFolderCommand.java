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
 * Deletes a folder identified using it's displayed index from the home directory.
 */
public class DeleteFolderCommand extends Command {

    public static final String COMMAND_WORD = "deletefolder";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the card folder identified by the index number used in the displayed list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_FOLDER_SUCCESS = "Deleted Card Folder: %1$s";

    private final Index targetIndex;

    public DeleteFolderCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<ReadOnlyCardFolder> cardFolderList = model.getCardFolders();

        if (targetIndex.getZeroBased() >= cardFolderList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOLDER_DISPLAYED_INDEX);
        }

        ReadOnlyCardFolder cardFolderToDelete = cardFolderList.get(targetIndex.getZeroBased());

        model.deleteFolder(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_DELETE_FOLDER_SUCCESS, cardFolderToDelete),
                false, false, true, null, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteFolderCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteFolderCommand) other).targetIndex)); // state check
    }
}
