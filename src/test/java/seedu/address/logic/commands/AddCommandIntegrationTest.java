package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCards.getTypicalCardFolders;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.card.Card;
import seedu.address.testutil.CardBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCardFolders(), new UserPrefs());
    }

    @Test
    public void execute_newCard_success() throws CommandException {
        Card validCard = new CardBuilder().build();

        Model expectedModel = new ModelManager(model.getCardFolders(), new UserPrefs());
        expectedModel.addCard(validCard);
        expectedModel.commitActiveCardFolder();

        assertCommandSuccess(new AddCommand(validCard), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validCard), expectedModel);
    }

    @Test
    public void execute_duplicateCard_throwsCommandException() {
        Card cardInList = model.getActiveCardFolder().getCardList().get(0);
        assertCommandFailure(new AddCommand(cardInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_CARD);
    }

}
