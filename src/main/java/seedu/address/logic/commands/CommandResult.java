package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.logic.AnswerCommandResultType;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.card.Card;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The side panel should be updated. */
    private final boolean sidePanelUpdated;

    /** The application should enter a test session. */
    private final Card testSessionCard;

    /** The current test session should end. */
    private final boolean endTestSession;

    /** The application should show to user whether answer attempted is correct or wrong. */
    private final AnswerCommandResultType answerCommandResult;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean sidePanelUpdated,
                         Card testSessionCard, boolean endTestSession, AnswerCommandResultType answerCommandResult) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.sidePanelUpdated = sidePanelUpdated;
        this.testSessionCard = testSessionCard;
        this.endTestSession = endTestSession;
        this.answerCommandResult = answerCommandResult;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default fullAnswer.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false, null, false, AnswerCommandResultType.NOT_ANSWER_COMMAND);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isEndTestSession() {
        return endTestSession;
    }

    /**
     * Check if command is to enter test session
     * @return a boolean variable to state if command is test or not
     */
    public boolean isTestSession() {
        if (testSessionCard == null) {
            return false;
        }
        return true;
    }

    public Card getTestSessionCard() {
        return testSessionCard;
    }

    /**
     * Check if command is an ans command
     * @return a boolean variable to state if command is ans or not
     */
    public boolean isAnswerCommand() {
        if (answerCommandResult == AnswerCommandResultType.NOT_ANSWER_COMMAND) {
            return false;
        }
        return true;
    }

    /**
     * Check if attempted answer is correct or wrong
     * This method should be called only if a valid ans command is called
     * @return a boolean variable to state if command is ans or not
     */
    public boolean isAnswerCorrect() throws CommandException {
        if (answerCommandResult == AnswerCommandResultType.ANSWER_CORRECT) {
            return true;
        } else if (answerCommandResult == AnswerCommandResultType.ANSWER_WRONG) {
            return false;
        } else {
            throw new CommandException(Messages.MESSAGE_INVALID_ANSWER_COMMAND);
        }
    }

    public boolean isSidePanelUpdated() throws CommandException {
        return sidePanelUpdated;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && testSessionCard == otherCommandResult.testSessionCard
                && endTestSession == otherCommandResult.endTestSession
                && answerCommandResult == otherCommandResult.answerCommandResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, testSessionCard, endTestSession, answerCommandResult);
    }

}
