package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.card.*;
import seedu.address.model.card.Score;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Card objects.
 */
public class CardBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_ANSWER = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "5/11";

    private Question question;
    private Answer answer;
    private Email email;
    private Score score;
    private Set<Tag> tags;

    public CardBuilder() {
        question = new Question(DEFAULT_NAME);
        answer = new Answer(DEFAULT_ANSWER);
        email = new Email(DEFAULT_EMAIL);
        score = new Score(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the CardBuilder with the data of {@code cardToCopy}.
     */
    public CardBuilder(Card cardToCopy) {
        question = cardToCopy.getQuestion();
        answer = cardToCopy.getAnswer();
        email = cardToCopy.getEmail();
        score = cardToCopy.getScore();
        tags = new HashSet<>(cardToCopy.getTags());
    }

    /**
     * Sets the {@code Question} of the {@code Card} that we are building.
     */
    public CardBuilder withQuestion(String question) {
        this.question = new Question(question);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Card} that we are building.
     */
    public CardBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Score} of the {@code Card} that we are building.
     */
    public CardBuilder withScore(String score) {
        this.score = new Score(score);
        return this;
    }

    /**
     * Sets the {@code Answer} of the {@code Card} that we are building.
     */
    public CardBuilder withAnswer(String answer) {
        this.answer = new Answer(answer);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Card} that we are building.
     */
    public CardBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Card build() {
        return new Card(question, answer, email, score, tags);
    }

}
