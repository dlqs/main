package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANSWER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HINT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUESTION;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Question;
import seedu.address.model.card.Score;
import seedu.address.model.hint.Hint;

/**
 * Edits the details of an existing card in the card folder.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the card identified "
            + "by the index number used in the displayed card list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_QUESTION + "QUESTION] "
            + "[" + PREFIX_ANSWER + "ANSWER] "
            + "[" + PREFIX_HINT + "HINT]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ANSWER + "91234567 ";

    public static final String MESSAGE_EDIT_CARD_SUCCESS = "Edited Card: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CARD = "This card already exists in the card folder.";

    private final Index index;
    private final EditCardDescriptor editCardDescriptor;

    /**
     * @param index of the card in the filtered card list to edit
     * @param editCardDescriptor details to edit the card with
     */
    public EditCommand(Index index, EditCardDescriptor editCardDescriptor) {
        requireNonNull(index);
        requireNonNull(editCardDescriptor);

        this.index = index;
        this.editCardDescriptor = new EditCardDescriptor(editCardDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!model.isInFolder()) {
            throw new CommandException(Messages.MESSAGE_ILLEGAL_COMMAND_NOT_IN_FOLDER);
        }

        List<Card> lastShownList = model.getFilteredCards();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CARD_DISPLAYED_INDEX);
        }

        Card cardToEdit = lastShownList.get(index.getZeroBased());
        Card editedCard = createEditedCard(cardToEdit, editCardDescriptor);

        if (!cardToEdit.isSameCard(editedCard) && model.hasCard(editedCard)) {
            throw new CommandException(MESSAGE_DUPLICATE_CARD);
        }

        model.setCard(cardToEdit, editedCard);
        model.updateFilteredCard(PREDICATE_SHOW_ALL_CARDS);
        model.commitActiveCardFolder();
        return new CommandResult(String.format(MESSAGE_EDIT_CARD_SUCCESS, editedCard));
    }

    /**
     * Creates and returns a {@code Card} with the details of {@code cardToEdit}
     * edited with {@code editCardDescriptor}.
     */
    private static Card createEditedCard(Card cardToEdit, EditCardDescriptor editCardDescriptor) {
        assert cardToEdit != null;

        Question updatedQuestion = editCardDescriptor.getQuestion().orElse(cardToEdit.getQuestion());
        Answer updatedAnswer = editCardDescriptor.getAnswer().orElse(cardToEdit.getAnswer());
        // Score cannot be edited, so copy original
        Score originalScore = cardToEdit.getScore();
        Set<Hint> updatedHints = editCardDescriptor.getHints().orElse(cardToEdit.getHints());

        return new Card(updatedQuestion, updatedAnswer, originalScore, updatedHints);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editCardDescriptor.equals(e.editCardDescriptor);
    }

    /**
     * Stores the details to edit the card with. Each non-empty field value will replace the
     * corresponding field value of the card.
     */
    public static class EditCardDescriptor {
        private Question question;
        private Answer answer;
        private Set<Hint> hints;

        public EditCardDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code hints} is used internally.
         */
        public EditCardDescriptor(EditCardDescriptor toCopy) {
            setQuestion(toCopy.question);
            setAnswer(toCopy.answer);
            setHints(toCopy.hints);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(question, answer, hints);
        }

        public void setQuestion(Question question) {
            this.question = question;
        }

        public Optional<Question> getQuestion() {
            return Optional.ofNullable(question);
        }

        public void setAnswer(Answer answer) {
            this.answer = answer;
        }

        public Optional<Answer> getAnswer() {
            return Optional.ofNullable(answer);
        }

        /**
         * Sets {@code hints} to this object's {@code hints}.
         * A defensive copy of {@code hints} is used internally.
         */
        public void setHints(Set<Hint> hints) {
            this.hints = (hints != null) ? new HashSet<>(hints) : null;
        }

        /**
         * Returns an unmodifiable hint set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code hints} is null.
         */
        public Optional<Set<Hint>> getHints() {
            return (hints != null) ? Optional.of(Collections.unmodifiableSet(hints)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCardDescriptor)) {
                return false;
            }

            // state check
            EditCardDescriptor e = (EditCardDescriptor) other;

            return getQuestion().equals(e.getQuestion())
                    && getAnswer().equals(e.getAnswer())
                    && getHints().equals(e.getHints());
        }
    }
}
