package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.Test;

import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager(this.getClass().getName());
    private Model expectedModel = new ModelManager(this.getClass().getName());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false, false,
                null, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
        assertCommandSuccess(new HelpCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }
}
