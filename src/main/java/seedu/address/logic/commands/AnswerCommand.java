package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;

import seedu.address.commons.core.Messages;
import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Score;

/**
 * Allows user to input an answer for the currently displayed card, compares it with the
 * correct answer in that card and tell the user if it is correct or wrong.
 * Answer matching is case insensitive.
 */
public class AnswerCommand extends Command {

    public static final String COMMAND_WORD = "ans";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": User inputs an answer for the"
            + " currently displayed card.\n"
            + "Parameters: ANSWER \n"
            + "Example: " + COMMAND_WORD + " Mitochondrion";

    public static final String MESSAGE_ANSWER_SUCCESS = "Answer sent successfully";

    private final Answer attemptedAnswer;

    public AnswerCommand(Answer attemptedAnswer) {
        this.attemptedAnswer = attemptedAnswer;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.checkIfInsideTestSession()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_OUTSIDE_TEST_SESSION);
        }
        if (model.checkIfCardAlreadyAnswered()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ANSWER_COMMAND);
        }
        model.setCardAsAnswered();
        Card cardToMark = model.getCurrentTestedCard();

        boolean isAttemptCorrect = model.markAttemptedAnswer(attemptedAnswer);

        Card scoredCard = createScoredCard(cardToMark, isAttemptCorrect);
        model.setCard(cardToMark, scoredCard);
        model.updateFilteredCard(PREDICATE_SHOW_ALL_CARDS);
        model.commitActiveCardFolder();
        if (isAttemptCorrect) {
            return new CommandResult(MESSAGE_ANSWER_SUCCESS, false, false, false, null,
                    false, AnswerCommandResultType.ANSWER_CORRECT);
        } else {
            return new CommandResult(MESSAGE_ANSWER_SUCCESS, false, false, false, null,
                    false, AnswerCommandResultType.ANSWER_WRONG);
        }
    }

    /**
     *
     * @param cardToMark {@code Card} which is being marked correct or wrong
     * @param markCorrect Boolean representing if card should be graded correct or wrong
     * @return Card created with new score
     */
    private static Card createScoredCard(Card cardToMark, boolean markCorrect) {
        Score newScore;
        if (markCorrect) {
            newScore = new Score(cardToMark.getScore().correctAttempts + 1,
                    cardToMark.getScore().totalAttempts + 1);
        } else {
            newScore = new Score(cardToMark.getScore().correctAttempts,
                    cardToMark.getScore().totalAttempts + 1);
        }
        return new Card(cardToMark.getQuestion(), cardToMark.getAnswer(), newScore, cardToMark.getHints());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AnswerCommand // instanceof handles nulls
                && attemptedAnswer.equals(((AnswerCommand) other).attemptedAnswer)); // state check
    }
}
