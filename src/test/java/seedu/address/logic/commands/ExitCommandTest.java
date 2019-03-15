package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.Test;

import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager(this.getClass().getName());
    private Model expectedModel = new ModelManager(this.getClass().getName());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true, false,
                null, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
        assertCommandSuccess(new ExitCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }
}
