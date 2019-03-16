package seedu.address.model.card;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Card's score in the card folder.
 * Guarantees: immutable; is valid as declared in {@link #isValidScore(String)}
 */
public class Score implements Comparable<Score> {

    public static final String MESSAGE_CONSTRAINTS = "Score can take a float between 0 and 1 or a string a/b,"
            + "where 0 <= a <= b and a and b are integers.";

    public final int correctAttempts;
    public final int totalAttempts;

    /**
     * Constructs an {@code Score}.
     *
     * @param score A valid score string.
     */
    public Score(String score) {
        requireNonNull(score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);

        this.correctAttempts = parseCorrectAttempts(score);
        this.totalAttempts = parseTotalAttempts(score);
    }

    /**
     * Constructs an {@code Score}.
     *
     * @param correctAttempts A valid number of correct attempts.
     * @param totalAttempts A valid number of total attempts.
     */
    public Score(int correctAttempts, int totalAttempts) {
        requireNonNull(correctAttempts);
        requireNonNull(totalAttempts);
        checkArgument(isValidScore(correctAttempts, totalAttempts), MESSAGE_CONSTRAINTS);

        this.correctAttempts = correctAttempts;
        this.totalAttempts = totalAttempts;
    }

    /**
     * Returns true if a given string is a valid score.
     */
    public static boolean isValidScore(String score) {

        try {
            return isValidScore(parseCorrectAttempts(score), parseTotalAttempts(score));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true if given integers form valid score.
     */
    public static boolean isValidScore(int correctAttempts, int totalAttempts) {
        return 0 <= correctAttempts && correctAttempts <= totalAttempts;
    }

    /**
     * Returns score as correctAttempts divided by totalAttempts
     * @return score A double between 0 and 1
     */
    public double getAsDouble() {
        if (totalAttempts == 0) {
            return 0;
        }
        return (double) correctAttempts / totalAttempts;
    }

    /**
     * Returns number of correct attempts as integer from score string.
     * @param score Score formatted as string
     * @return correctAttempts The number of correct attempts
     * @throws NumberFormatException
     */
    private static int parseCorrectAttempts(String score) throws NumberFormatException {
        String[] splitString = score.split("/");

        if (splitString.length != 2) {
            throw new NumberFormatException("Score is not formatted correctly.");
        }

        return Integer.parseInt(splitString[0]);
    }

    /**
     * Returns number of total attempts as integer from score string.
     * @param score Score formatted as string
     * @return totalAttempts The number of total attempts
     * @throws NumberFormatException
     */
    private static int parseTotalAttempts(String score) throws NumberFormatException {
        String[] splitString = score.split("/");

        if (splitString.length != 2) {
            throw new NumberFormatException("Score is not formatted correctly.");
        }

        return Integer.parseInt(splitString[1]);
    }

    @Override
    public String toString() {
        return String.format("%d/%d", correctAttempts, totalAttempts);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Score // instanceof handles nulls
                && correctAttempts == (((Score) other).correctAttempts)
                && totalAttempts == (((Score) other).totalAttempts));
    }

    @Override
    public int compareTo(Score other) {
        // Get percentage difference and multiply by 100 to compare as int
        return (int) (100 * (this.getAsDouble() - other.getAsDouble()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctAttempts, totalAttempts);
    }

}
