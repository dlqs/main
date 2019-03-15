package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Ends the current test session.
 * This command is valid only when user is in a test session.
 */
public class EndCommand extends Command {

    public static final String COMMAND_WORD = "end";

    public static final String MESSAGE_END_TEST_SESSION_SUCCESS = "End test session";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        if (!model.checkIfInsideTestSession()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_OUTSIDE_TEST_SESSION);
        }
        model.endTestSession();
        return new CommandResult(MESSAGE_END_TEST_SESSION_SUCCESS, false, false, false, null, true,
                AnswerCommandResultType.NOT_ANSWER_COMMAND);
    }

}
