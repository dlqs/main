package seedu.knowitall.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.knowitall.testutil.TypicalCards.getTypicalCardFolders;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.knowitall.logic.CommandHistory;
import seedu.knowitall.logic.commands.exceptions.CommandException;
import seedu.knowitall.model.Model;
import seedu.knowitall.model.ModelManager;
import seedu.knowitall.model.UserPrefs;



public class ExportCommandTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalCardFolders(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.exitFolderToHome();
    }



    @Test
    public void execute_exportSingleValidCardFolderIndex_success() throws Exception {
        System.out.println(tmpFolder.getRoot());
        List<Integer> myList = new ArrayList<>(Arrays.asList(1));
        ExportCommand exportCommand = new ExportCommand(myList);


        CommandResult commandResult = exportCommand.execute(model, commandHistory);
        assertEquals(ExportCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
    }


    @After
    public void deleteExportFile() {
        File file = new File("./Typical Cards.csv");
        if (file.exists()) {
            boolean isDeleted = file.delete();
            assert (isDeleted);
        }
    }


    @Test
    public void execute_exportSingleInvalidCardFolderIndex_failure() throws Exception {
        List<Integer> myList = new ArrayList<>(Arrays.asList(1, 3));
        ExportCommand exportCommand = new ExportCommand(myList);

        thrown.expect(CommandException.class);
        thrown.expectMessage(ExportCommand.MESSAGE_MISSING_CARD_FOLDERS);
        CommandResult commandResult = exportCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_exportMultipleValidCardFolderIndex_success() {

    }

}
