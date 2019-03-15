package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ANSWER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ANSWER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.HINT_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.HINT_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ANSWER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_HINT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_QUESTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.QUESTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.QUESTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ANSWER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HINT_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_QUESTION_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalCards.AMY;
import static seedu.address.testutil.TypicalCards.BOB;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Question;
import seedu.address.model.hint.Hint;
import seedu.address.testutil.CardBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Card expectedCard = new CardBuilder(BOB).withHint(VALID_HINT_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                + HINT_DESC_FRIEND, new AddCommand(expectedCard));

        // multiple names - last name accepted
        assertParseSuccess(parser, QUESTION_DESC_AMY + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                + HINT_DESC_FRIEND, new AddCommand(expectedCard));

        // multiple answers - last answer accepted
        assertParseSuccess(parser, QUESTION_DESC_BOB + ANSWER_DESC_AMY + ANSWER_DESC_BOB
                + HINT_DESC_FRIEND, new AddCommand(expectedCard));

        // multiple hints - last hint accepted
        assertParseSuccess(parser, QUESTION_DESC_BOB + ANSWER_DESC_BOB + HINT_DESC_HUSBAND
                + HINT_DESC_FRIEND, new AddCommand(expectedCard));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero hints
        Card expectedCard = new CardBuilder(AMY).withHint().build();
        assertParseSuccess(parser, QUESTION_DESC_AMY + ANSWER_DESC_AMY, new AddCommand(expectedCard));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_QUESTION_BOB + ANSWER_DESC_BOB, expectedMessage);

        // missing answer prefix
        assertParseFailure(parser, QUESTION_DESC_BOB + VALID_ANSWER_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_QUESTION_BOB + VALID_ANSWER_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_QUESTION_DESC + ANSWER_DESC_BOB + HINT_DESC_HUSBAND
                + HINT_DESC_FRIEND, Question.MESSAGE_CONSTRAINTS);

        // invalid answer
        assertParseFailure(parser, QUESTION_DESC_BOB + INVALID_ANSWER_DESC + HINT_DESC_HUSBAND
                + HINT_DESC_FRIEND, Answer.MESSAGE_CONSTRAINTS);

        // invalid hint
        assertParseFailure(parser, QUESTION_DESC_BOB + ANSWER_DESC_BOB + INVALID_HINT_DESC
                + VALID_HINT_FRIEND, Hint.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_QUESTION_DESC + ANSWER_DESC_BOB, Question.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + QUESTION_DESC_BOB + ANSWER_DESC_BOB
                + HINT_DESC_HUSBAND + HINT_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
