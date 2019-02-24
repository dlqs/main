package seedu.address.model.card;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Card's score in the card.
 * Guarantees: immutable; is valid as declared in {@link #isValidScore(Double)}
 */
public class Score {

    public static final String MESSAGE_CONSTRAINTS = "Score should be a Double between 0 and 1, and it should not be blank";

    public final Double value;

    /**
     * Constructs an {@code Score}.
     *
     * @param score A valid score.
     */
    public Score(Double score) {
        requireNonNull(score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);
        value = score;
    }

    /**
     * Returns true if a given score is valid score.
     */
    public static boolean isValidScore(Double score) {
        return score >= 0.0 || score <= 1.0;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Score // instanceof handles nulls
                && value.equals(((Score) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
