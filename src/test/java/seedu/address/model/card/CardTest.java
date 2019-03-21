package seedu.address.model.card;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ANSWER_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HINT_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_QUESTION_2;
import static seedu.address.testutil.TypicalCards.ALICE;
import static seedu.address.testutil.TypicalCards.CARD_2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.testutil.CardBuilder;

public class CardTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Card card = new CardBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        card.getHints().remove(0);
    }

    @Test
    public void isSameCard() {
        // same object -> returns true
        assertTrue(ALICE.isSameCard(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameCard(null));

        // different answer -> returns false
        Card editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_2).build();
        assertFalse(ALICE.isSameCard(editedAlice));

        // different question -> returns false
        editedAlice = new CardBuilder(ALICE).withQuestion(VALID_QUESTION_2).build();
        assertFalse(ALICE.isSameCard(editedAlice));

        // same question, same answer, different attributes -> returns true
        editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND).build();
        assertTrue(ALICE.isSameCard(editedAlice));

        // same question, different answer, different attributes -> returns false
        editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_2).withHint(VALID_HINT_HUSBAND).build();
        assertFalse(ALICE.isSameCard(editedAlice));

        // same question, same answer, different attributes -> returns true
        editedAlice = new CardBuilder(ALICE).withHint(VALID_HINT_HUSBAND).build();
        assertTrue(ALICE.isSameCard(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Card aliceCopy = new CardBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different card -> returns false
        assertFalse(ALICE.equals(CARD_2));

        // different question -> returns false
        Card editedAlice = new CardBuilder(ALICE).withQuestion(VALID_QUESTION_2).build();
        assertFalse(ALICE.equals(editedAlice));

        // different answer -> returns false
        editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_2).build();
        assertFalse(ALICE.equals(editedAlice));

    }
}
