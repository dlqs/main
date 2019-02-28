package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.JsonAdaptedCard.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalCards.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.card.Score;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Email;
import seedu.address.model.card.Question;
import seedu.address.testutil.Assert;

public class JsonAdaptedCardTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ANSWER = "+651234";
    private static final String INVALID_SCORE = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getQuestion().toString();
    private static final String VALID_ANSWER = BENSON.getAnswer().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_SCORE = BENSON.getScore().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validCardDetails_returnsCard() throws Exception {
        JsonAdaptedCard card = new JsonAdaptedCard(BENSON);
        assertEquals(BENSON, card.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedCard card =
                new JsonAdaptedCard(INVALID_NAME, VALID_ANSWER, VALID_EMAIL, VALID_SCORE, VALID_TAGS);
        String expectedMessage = Question.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(null, VALID_ANSWER, VALID_EMAIL, VALID_SCORE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Question.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidAnswer_throwsIllegalValueException() {
        JsonAdaptedCard card =
                new JsonAdaptedCard(VALID_NAME, INVALID_ANSWER, VALID_EMAIL, VALID_SCORE, VALID_TAGS);
        String expectedMessage = Answer.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullAnswer_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_NAME, null, VALID_EMAIL, VALID_SCORE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Answer.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedCard card =
                new JsonAdaptedCard(VALID_NAME, VALID_ANSWER, INVALID_EMAIL, VALID_SCORE, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_NAME, VALID_ANSWER, null, VALID_SCORE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedCard card =
                new JsonAdaptedCard(VALID_NAME, VALID_ANSWER, VALID_EMAIL, INVALID_SCORE, VALID_TAGS);
        String expectedMessage = Score.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedCard card = new JsonAdaptedCard(VALID_NAME, VALID_ANSWER, VALID_EMAIL, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Score.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, card::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedCard card =
                new JsonAdaptedCard(VALID_NAME, VALID_ANSWER, VALID_EMAIL, VALID_SCORE, invalidTags);
        Assert.assertThrows(IllegalValueException.class, card::toModelType);
    }

}
