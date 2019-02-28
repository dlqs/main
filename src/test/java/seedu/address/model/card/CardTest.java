package seedu.address.model.card;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCORE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ANSWER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_QUESTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalCards.ALICE;
import static seedu.address.testutil.TypicalCards.BOB;

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
        card.getTags().remove(0);
    }

    @Test
    public void isSameCard() {
        // same object -> returns true
        assertTrue(ALICE.isSameCard(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameCard(null));

        // different answer and email -> returns false
        Card editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSameCard(editedAlice));

        // different question -> returns false
        editedAlice = new CardBuilder(ALICE).withQuestion(VALID_QUESTION_BOB).build();
        assertFalse(ALICE.isSameCard(editedAlice));

        // same question, same answer, different attributes -> returns true
        editedAlice = new CardBuilder(ALICE).withEmail(VALID_EMAIL_BOB).withScore(VALID_SCORE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameCard(editedAlice));

        // same question, same email, different attributes -> returns true
        editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_BOB).withScore(VALID_SCORE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameCard(editedAlice));

        // same question, same answer, same email, different attributes -> returns true
        editedAlice = new CardBuilder(ALICE).withScore(VALID_SCORE_BOB).withTags(VALID_TAG_HUSBAND).build();
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
        assertFalse(ALICE.equals(BOB));

        // different question -> returns false
        Card editedAlice = new CardBuilder(ALICE).withQuestion(VALID_QUESTION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different answer -> returns false
        editedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new CardBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new CardBuilder(ALICE).withScore(VALID_SCORE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new CardBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
