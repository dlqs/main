package seedu.address.model.card;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import javafx.util.Pair;

/**
 * Represents a Card's score in the card.
 * Guarantees: immutable; is valid as declared in {@link #isValidScore(Pair<Integer, Integer>)}
 */
public class Score {

    public static final String MESSAGE_CONSTRAINTS = "Score should be a pair of integers, each greater than or equal 0" +
            ", and it should not be blank";

    public final Pair<Integer, Integer> value;

    /**
     * Constructs an {@code Score}.
     *
     * @param score A valid score.
     */
    public Score(Pair<Integer, Integer> score) {
        requireNonNull(score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);
        value = score;
    }

    /**
     * Returns true if a given score is valid score.
     */
    public static boolean isValidScore(Pair<Integer, Integer> test) {
        return test.getKey() >= 0 && test.getValue() >= test.getKey();
    }

    /**
     * Returns score as a percentage.
     *
     * @return score A score as double
     */
    public Double getAsDouble() {
        if (value.getValue() == 0) {
            return 0.0;
        }
        return (double)value.getKey()/value.getValue();
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
