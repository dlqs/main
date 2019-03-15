package seedu.address.model.hint;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Hint in the card folder.
 * Guarantees: immutable; question is valid as declared in {@link #isValidHintName(String)}
 */
public class Hint {

    public static final String MESSAGE_CONSTRAINTS = "Hints names should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String hintName;

    /**
     * Constructs a {@code Hint}.
     *
     * @param hintName A valid hint name.
     */
    public Hint(String hintName) {
        requireNonNull(hintName);
        checkArgument(isValidHintName(hintName), MESSAGE_CONSTRAINTS);
        this.hintName = hintName;
    }

    /**
     * Returns true if a given string is a valid hint name.
     */
    public static boolean isValidHintName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Hint // instanceof handles nulls
                && hintName.equals(((Hint) other).hintName)); // state check
    }

    @Override
    public int hashCode() {
        return hintName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + hintName + ']';
    }

}
