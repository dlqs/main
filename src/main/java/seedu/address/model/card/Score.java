package seedu.address.model.card;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import javafx.util.Pair;

/**
 * Represents a Card's score in the card.
 * Guarantees: immutable; is valid as declared in {@link #isValidScore(Integer, Integer)}
 */
public class Score {

    public static final String MESSAGE_CONSTRAINTS = "Score should be a pair of integers, each greater than or equal 0" +
            ", and it should not be blank";

    public final Integer correctAttempts;

    public final Integer totalAttempts;

    /**
     * Constructs an {@code Score}.
     *
     * @param correctAttempts A valid integer representing of attempts, >= 0
     * @param totalAttempts A valid integer representing total attempts, >= correctAttempts
     */
    public Score(Integer correctAttempts, Integer totalAttempts) {
        requireNonNull(correctAttempts);
        requireNonNull(totalAttempts);
        checkArgument(isValidScore(correctAttempts, totalAttempts), MESSAGE_CONSTRAINTS);
        this.correctAttempts = correctAttempts;
        this.totalAttempts = totalAttempts;
    }

    /**
     * Constructs an {@code Score}.
     *
     * @param score A valid score string, in form in a/b.
     */
    public Score(String score) {
        requireNonNull(score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);
        String[] splitNumbers = score.split("/");
        this.correctAttempts = Integer.parseInt(splitNumbers[0]);
        this.totalAttempts = Integer.parseInt(splitNumbers[1]);
    }

    /**
     * Returns true if a given score is valid score.
     */
    public static boolean isValidScore(Integer correctAttempts, Integer totalAttempts) {
        return correctAttempts >= 0 && totalAttempts >= correctAttempts;
    }

    /**
     * Returns true if a given string is formatted correctly, i.e. a/b where a and b are integers and 0 <= a <= b.
     */
    public static boolean isValidScore(String test) {
        String[] splitNumbers = test.split("/");

        if (splitNumbers.length != 2) {
            return false;
        }

        Integer correctAttempts;
        Integer totalAttempts;
        try {
            correctAttempts = Integer.parseInt(splitNumbers[0]);
            totalAttempts = Integer.parseInt(splitNumbers[1]);
        } catch (NumberFormatException ex) {
            return false;
        }

        return isValidScore(correctAttempts, totalAttempts);
    }

    /**
     * Returns score as a percentage.
     *
     * @return score A score as double
     */
    public Double getAsDouble() {
        if (totalAttempts == 0) {
            return 0.0;
        }
        return (double)correctAttempts/totalAttempts;
    }


    @Override
    public String toString() {
        return String.format("%d/%d", correctAttempts, totalAttempts);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Score // instanceof handles nulls
                && correctAttempts.equals(((Score) other).correctAttempts)
                && totalAttempts.equals(((Score) other).totalAttempts));
    }

    @Override
    public int hashCode() {
        return correctAttempts.hashCode()*totalAttempts.hashCode();
    }

}
