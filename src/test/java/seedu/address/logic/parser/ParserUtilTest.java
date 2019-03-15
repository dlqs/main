package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CARD;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Question;
import seedu.address.model.hint.Hint;
import seedu.address.testutil.Assert;

public class ParserUtilTest {
    private static final String INVALID_QUESTION = " ";
    private static final String INVALID_ANSWER = " ";
    private static final String INVALID_HINT = "#friend";

    private static final String VALID_QUESTION = "Rachel Walker";
    private static final String VALID_ANSWER = "123456";
    private static final String VALID_HINT_1 = "friend";
    private static final String VALID_HINT_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_CARD, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_CARD, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseQuestion_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseQuestion((String) null));
    }

    @Test
    public void parseQuestion_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseQuestion(INVALID_QUESTION));
    }

    @Test
    public void parseQuestion_validValueWithoutWhitespace_returnsQuestion() throws Exception {
        Question expectedQuestion = new Question(VALID_QUESTION);
        assertEquals(expectedQuestion, ParserUtil.parseQuestion(VALID_QUESTION));
    }

    @Test
    public void parseQuestion_validValueWithWhitespace_returnsTrimmedQuestion() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_QUESTION + WHITESPACE;
        Question expectedQuestion = new Question(VALID_QUESTION);
        assertEquals(expectedQuestion, ParserUtil.parseQuestion(nameWithWhitespace));
    }

    @Test
    public void parseAnswer_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseAnswer((String) null));
    }

    @Test
    public void parseAnswer_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseAnswer(INVALID_ANSWER));
    }

    @Test
    public void parseAnswer_validValueWithoutWhitespace_returnsAnswer() throws Exception {
        Answer expectedAnswer = new Answer(VALID_ANSWER);
        assertEquals(expectedAnswer, ParserUtil.parseAnswer(VALID_ANSWER));
    }

    @Test
    public void parseAnswer_validValueWithWhitespace_returnsTrimmedAnswer() throws Exception {
        String answerWithWhitespace = WHITESPACE + VALID_ANSWER + WHITESPACE;
        Answer expectedAnswer = new Answer(VALID_ANSWER);
        assertEquals(expectedAnswer, ParserUtil.parseAnswer(answerWithWhitespace));
    }

    @Test
    public void parseHint_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseHint(null);
    }

    @Test
    public void parseHint_invalidValue_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseHint(INVALID_HINT);
    }

    @Test
    public void parseHint_validValueWithoutWhitespace_returnsHint() throws Exception {
        Hint expectedHint = new Hint(VALID_HINT_1);
        assertEquals(expectedHint, ParserUtil.parseHint(VALID_HINT_1));
    }

    @Test
    public void parseHint_validValueWithWhitespace_returnsTrimmedHint() throws Exception {
        String hintWithWhitespace = WHITESPACE + VALID_HINT_1 + WHITESPACE;
        Hint expectedHint = new Hint(VALID_HINT_1);
        assertEquals(expectedHint, ParserUtil.parseHint(hintWithWhitespace));
    }

    @Test
    public void parseHints_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseHints(null);
    }

    @Test
    public void parseHints_collectionWithInvalidHints_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseHints(Arrays.asList(VALID_HINT_1, INVALID_HINT));
    }

    @Test
    public void parseHints_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseHints(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseHints_collectionWithValidHints_returnsHintSet() throws Exception {
        Set<Hint> actualHintSet = ParserUtil.parseHints(Arrays.asList(VALID_HINT_1, VALID_HINT_2));
        Set<Hint> expectedHintSet = new HashSet<Hint>(Arrays.asList(new Hint(VALID_HINT_2)));

        assertEquals(expectedHintSet, actualHintSet);
    }
}
