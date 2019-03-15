package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CardFolder;
import seedu.address.model.Model;

/**
 * Adds a card folder.
 */
public class AddFolderCommand extends Command {

    public static final String COMMAND_WORD = "addfolder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a card folder. "
            + "Parameters: "
            + "FOLDER_NAME \n"
            + "Example: " + COMMAND_WORD + " "
            + "Nervous System ";

    public static final String MESSAGE_SUCCESS = "New card folder added: %1$s";
    public static final String MESSAGE_DUPLICATE_CARD_FOLDER = "This card folder already exists";

    private final CardFolder toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Card}
     */
    public AddFolderCommand(CardFolder cardFolder) {
        requireNonNull(cardFolder);
        toAdd = cardFolder;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasFolder(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CARD_FOLDER);
        }

        model.addFolder(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddFolderCommand // instanceof handles nulls
                && toAdd.equals(((AddFolderCommand) other).toAdd));
    }
}
