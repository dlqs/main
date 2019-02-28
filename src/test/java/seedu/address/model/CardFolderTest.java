package seedu.address.model;

import static org.junit.Assert.*;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCORE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalCards.ALICE;
import static seedu.address.testutil.TypicalCards.getTypicalCardFolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.card.Card;
import seedu.address.model.card.exceptions.DuplicateCardException;
import seedu.address.testutil.CardBuilder;

public class CardFolderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final CardFolder cardFolder = new CardFolder();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), cardFolder.getCardList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardFolder.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyCardFolder_replacesData() {
        CardFolder newData = getTypicalCardFolder();
        cardFolder.resetData(newData);
        assertEquals(newData, cardFolder);
    }

    @Test
    public void resetData_withDuplicateCards_throwsDuplicateCardException() {
        // Two cards with the same identity fields
        Card editedAlice = new CardBuilder(ALICE).withScore(VALID_SCORE_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Card> newCards = Arrays.asList(ALICE, editedAlice);
        CardFolderStub newData = new CardFolderStub(newCards);

        thrown.expect(DuplicateCardException.class);
        cardFolder.resetData(newData);
    }

    @Test
    public void hasCard_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        cardFolder.hasCard(null);
    }

    @Test
    public void hasCard_cardNotInCardFolder_returnsFalse() {
        assertFalse(cardFolder.hasCard(ALICE));
    }

    @Test
    public void hasCard_cardInCardFolder_returnsTrue() {
        cardFolder.addCard(ALICE);
        assertTrue(cardFolder.hasCard(ALICE));
    }

    @Test
    public void hasCard_cardWithSameIdentityFieldsInCardFolder_returnsTrue() {
        cardFolder.addCard(ALICE);
        Card editedAlice = new CardBuilder(ALICE).withScore(VALID_SCORE_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(cardFolder.hasCard(editedAlice));
    }

    @Test
    public void getCardList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        cardFolder.getCardList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardFolder.addListener(listener);
        cardFolder.addCard(ALICE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        cardFolder.addListener(listener);
        cardFolder.removeListener(listener);
        cardFolder.addCard(ALICE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyCardFolder whose cards list can violate interface constraints.
     */
    private static class CardFolderStub implements ReadOnlyCardFolder {
        private final ObservableList<Card> cards = FXCollections.observableArrayList();

        CardFolderStub(Collection<Card> cards) {
            this.cards.setAll(cards);
        }

        @Override
        public ObservableList<Card> getCardList() {
            return cards;
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

}
