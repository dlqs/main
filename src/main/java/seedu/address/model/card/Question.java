package seedu.address.model.card;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Card's question in the card folder.
 * Guarantees: immutable; is valid as declared in {@link #isValidQuestion(String)}
 */
public class Question implements Comparable<Question> {

    public static final String MESSAGE_CONSTRAINTS = "Questions can take any values, and should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String fullQuestion;

    /**
     * Constructs a {@code Question}.
     *
     * @param name A valid name.
     */
    public Question(String name) {
        requireNonNull(name);
        checkArgument(isValidQuestion(name), MESSAGE_CONSTRAINTS);
        fullQuestion = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidQuestion(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullQuestion;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Question // instanceof handles nulls
                && fullQuestion.equals(((Question) other).fullQuestion)); // state check
    }

    @Override
    public int compareTo(Question other) {
        return fullQuestion.compareTo(other.fullQuestion);
    }

    @Override
    public int hashCode() {
        return fullQuestion.hashCode();
    }

}
