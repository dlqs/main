package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.card.*;
import seedu.address.model.card.Score;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Card}.
 */
class JsonAdaptedCard {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Card's %s field is missing!";

    private final String question;
    private final String answer;
    private final String email;
    private final String score;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedCard} with the given card details.
     */
    @JsonCreator
    public JsonAdaptedCard(@JsonProperty("question") String question, @JsonProperty("answer") String answer,
                           @JsonProperty("email") String email, @JsonProperty("score") String score,
                           @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.question = question;
        this.answer = answer;
        this.email = email;
        this.score = score;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Card} into this class for Jackson use.
     */
    public JsonAdaptedCard(Card source) {
        question = source.getQuestion().fullQuestion;
        answer = source.getAnswer().value;
        email = source.getEmail().value;
        score = source.getScore().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted card object into the model's {@code Card} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted card.
     */
    public Card toModelType() throws IllegalValueException {
        final List<Tag> cardTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            cardTags.add(tag.toModelType());
        }

        if (question == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Question.class.getSimpleName()));
        }
        if (!Question.isValidQuestion(question)) {
            throw new IllegalValueException(Question.MESSAGE_CONSTRAINTS);
        }
        final Question modelQuestion = new Question(question);

        if (answer == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Answer.class.getSimpleName()));
        }
        if (!Answer.isValidAnswer(answer)) {
            throw new IllegalValueException(Answer.MESSAGE_CONSTRAINTS);
        }
        final Answer modelAnswer = new Answer(answer);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (score == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Score.class.getSimpleName()));
        }
        if (!Score.isValidScore(score)) {
            throw new IllegalValueException(Score.MESSAGE_CONSTRAINTS);
        }
        final Score modelAddress = new Score(score);

        final Set<Tag> modelTags = new HashSet<>(cardTags);
        return new Card(modelQuestion, modelAnswer, modelEmail, modelAddress, modelTags);
    }

}
