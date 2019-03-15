package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ChangeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ChangeCommand object
 */
public class ChangeCommandParser implements Parser<ChangeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ChangeCommand
     * and returns an ChangeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ChangeCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeCommand.MESSAGE_USAGE), pe);
        }
    }
}
