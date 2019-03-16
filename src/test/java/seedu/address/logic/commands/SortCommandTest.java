package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCards.getTypicalCardFolders;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class SortCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCardFolders(), new UserPrefs());
        expectedModel = new ModelManager(model.getCardFolders(), new UserPrefs());
        expectedModel.sortFilteredCard(Model.COMPARATOR_ASC_SCORE_CARDS);
    }

    @Test
    public void execute_sort() {
        assertCommandSuccess(new SortCommand(), model, commandHistory, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
