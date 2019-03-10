package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AnswerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AnswerCommand object
 */
public class AnswerCommandParser implements Parser<AnswerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AnswerCommand
     * and returns an AnswerCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AnswerCommand parse(String args) throws ParseException {
        String attemptedAnswer = args.trim();
        if (attemptedAnswer.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AnswerCommand.MESSAGE_USAGE));
        }

        return new AnswerCommand(attemptedAnswer);
    }

}
