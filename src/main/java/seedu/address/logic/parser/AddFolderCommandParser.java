package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddFolderCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.CardFolder;


/**
 * Parses input arguments and creates a new AddFolderCommand object
 */
public class AddFolderCommandParser implements Parser<AddFolderCommand> {

    /**
     * Parses the given {@code String} in the context of the AddFolderCommand
     * and returns an AddFolderCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddFolderCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddFolderCommand.MESSAGE_USAGE));
        }

        CardFolder cardFolder = new CardFolder(trimmedArgs);

        return new AddFolderCommand(cardFolder);
    }
}
