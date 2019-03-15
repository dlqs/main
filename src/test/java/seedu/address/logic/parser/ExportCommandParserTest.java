package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.FILENAME_DESC_SAMPLE;
import static seedu.address.logic.commands.CommandTestUtil.FOLDER_DESC_SAMPLE_1;
import static seedu.address.logic.commands.CommandTestUtil.FOLDER_DESC_SAMPLE_2;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FILENAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FILENAME_EXT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FOLDER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FILENAME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FOLDER_NAME_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FOLDER_NAME_2;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import seedu.address.logic.commands.ExportCommand;

import seedu.address.storage.csvmanager.CardFolderExport;
import seedu.address.storage.csvmanager.CsvFile;


public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_allFields_present() {
        ExportCommand exportCommandWithOneFolder = buildExportCommand(VALID_FILENAME, VALID_FOLDER_NAME_1);
        ExportCommand exportCommandWithMultipleFolder = buildExportCommand(VALID_FILENAME,
                VALID_FOLDER_NAME_1, VALID_FOLDER_NAME_2);

        // whitespace preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + FOLDER_DESC_SAMPLE_1
                + FILENAME_DESC_SAMPLE, exportCommandWithOneFolder);

        // multiple folders
        assertParseSuccess(parser, FOLDER_DESC_SAMPLE_1 + FOLDER_DESC_SAMPLE_2
                + FILENAME_DESC_SAMPLE, exportCommandWithMultipleFolder);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);

        // missing folder prefix
        assertParseFailure(parser, VALID_FOLDER_NAME_1
                + FILENAME_DESC_SAMPLE, String.format(expectedMessage));

        // missing filename prefix
        assertParseFailure(parser, FOLDER_DESC_SAMPLE_1
                + VALID_FILENAME, String.format(expectedMessage));

        // missing all prefix
        assertParseFailure(parser, VALID_FOLDER_NAME_1
                + VALID_FILENAME, String.format(expectedMessage));
    }

    @Test
    public void parse_invalidValue_failure() {

        // missing Folder name
        assertParseFailure(parser, INVALID_FOLDER_DESC + FILENAME_DESC_SAMPLE,
                CardFolderExport.MESSAGE_CONSTRAINTS);

        // missing filename
        assertParseFailure(parser, FOLDER_DESC_SAMPLE_1 + INVALID_FILENAME_DESC,
                CsvFile.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, FOLDER_DESC_SAMPLE_1 + INVALID_FILENAME_EXT,
                CsvFile.MESSAGE_CONSTRAINTS);
    }


    /**
     *
     * helper method to build export command for testing
     */
    private ExportCommand buildExportCommand(String validFileName, String... validFolderDescriptions) {
        Set<CardFolderExport> cardFolderExports = Stream.of(validFolderDescriptions)
                .map(CardFolderExport::new)
                .collect(Collectors.toSet());
        CsvFile csvFile = new CsvFile(validFileName);
        return new ExportCommand(cardFolderExports, csvFile);
    }
}
