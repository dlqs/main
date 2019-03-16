package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.COMPARATOR_ASC_SCORE_CARDS;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Sorts flashcards based on score. Flashcards will be arranged in ascending order, from the lowest score to the
 * highest.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Sorted flashcards with lowest score first";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.sortFilteredCard(COMPARATOR_ASC_SCORE_CARDS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
