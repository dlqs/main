package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ANSWER_BOB;
import static seedu.address.model.Model.COMPARATOR_ASC_SCORE_CARDS;
import static seedu.address.model.Model.COMPARATOR_LEXICOGRAPHIC_CARDS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;
import static seedu.address.testutil.TypicalCards.ALICE;
import static seedu.address.testutil.TypicalCards.BENSON;
import static seedu.address.testutil.TypicalCards.BOB;
import static seedu.address.testutil.TypicalCards.FIONA;
import static seedu.address.testutil.TypicalCards.GEORGE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.card.Card;
import seedu.address.model.card.QuestionContainsKeywordsPredicate;
import seedu.address.model.card.exceptions.CardNotFoundException;
import seedu.address.testutil.CardBuilder;
import seedu.address.testutil.CardFolderBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager(this.getClass().getName());

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(null, modelManager.getSelectedCard());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setUserPrefs(null);
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setcardFolderFilesPath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setcardFolderFilesPath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setGuiSettings(null);
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setcardFolderFilesPath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setcardFolderFilesPath(null);
    }

    @Test
    public void setcardFolderFilesPath_validPath_setscardFolderFilesPath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setcardFolderFilesPath(path);
        assertEquals(path, modelManager.getcardFolderFilesPath());
    }

    @Test
    public void hasCard_nullCard_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasCard(null);
    }

    @Test
    public void hasCard_cardNotInCardFolder_returnsFalse() {
        assertFalse(modelManager.hasCard(ALICE));
    }

    @Test
    public void hasCard_cardInCardFolder_returnsTrue() {
        modelManager.addCard(ALICE);
        assertTrue(modelManager.hasCard(ALICE));
    }

    @Test
    public void deleteCard_cardIsSelectedAndFirstCardInFilteredCardList_selectionCleared() {
        modelManager.addCard(ALICE);
        modelManager.setSelectedCard(ALICE);
        modelManager.deleteCard(ALICE);
        assertEquals(null, modelManager.getSelectedCard());
    }

    @Test
    public void deleteCard_cardIsSelectedAndSecondCardInFilteredCardList_firstCardSelected() {
        modelManager.addCard(ALICE);
        modelManager.addCard(BOB);
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredCards());
        modelManager.setSelectedCard(BOB);
        modelManager.deleteCard(BOB);
        assertEquals(ALICE, modelManager.getSelectedCard());
    }

    @Test
    public void setCard_cardIsSelected_selectedCardUpdated() {
        modelManager.addCard(ALICE);
        modelManager.setSelectedCard(ALICE);
        Card updatedAlice = new CardBuilder(ALICE).withAnswer(VALID_ANSWER_BOB).build();
        modelManager.setCard(ALICE, updatedAlice);
        assertEquals(updatedAlice, modelManager.getSelectedCard());
    }

    @Test
    public void sortCards_byScore() {
        modelManager.addCard(FIONA);
        modelManager.addCard(GEORGE);
        modelManager.sortFilteredCard(COMPARATOR_ASC_SCORE_CARDS);
        // Fiona should be sorted after George because higher score
        Card lastCard = modelManager.getFilteredCards().get(modelManager.getFilteredCards().size() - 1);
        assertEquals(FIONA, lastCard);
    }

    @Test
    public void sortCards_byQuestionLexicographic() {
        modelManager.addCard(FIONA);
        modelManager.addCard(GEORGE);
        modelManager.sortFilteredCard(COMPARATOR_LEXICOGRAPHIC_CARDS);
        // Fiona should be sorted after George because higher score
        Card lastCard = modelManager.getFilteredCards().get(modelManager.getFilteredCards().size() - 1);
        assertEquals(GEORGE, lastCard);
    }

    @Test
    public void getFilteredCardList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredCards().remove(0);
    }

    @Test
    public void setSelectedCard_cardNotInFilteredCardList_throwsCardNotFoundException() {
        thrown.expect(CardNotFoundException.class);
        modelManager.setSelectedCard(ALICE);
    }

    @Test
    public void setSelectedCard_cardInFilteredCardList_setsSelectedCard() {
        modelManager.addCard(ALICE);
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredCards());
        modelManager.setSelectedCard(ALICE);
        assertEquals(ALICE, modelManager.getSelectedCard());
    }

    @Test
    public void equals() {
        CardFolder cardFolder = new CardFolderBuilder().withCard(ALICE).withCard(BENSON).build();
        CardFolder differentCardFolder = new CardFolder(this.getClass().getName());
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(Collections.singletonList(cardFolder), userPrefs);
        ModelManager modelManagerCopy = new ModelManager(Collections.singletonList(cardFolder), userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different cardFolder -> returns false
        assertFalse(modelManager.equals(new ModelManager(Collections.singletonList(differentCardFolder), userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getQuestion().fullQuestion.split("\\s+");
        modelManager.updateFilteredCard(new QuestionContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(Collections.singletonList(cardFolder), userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredCard(PREDICATE_SHOW_ALL_CARDS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setcardFolderFilesPath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(Collections.singletonList(cardFolder), differentUserPrefs)));

        // filteredList sorted differently -> returns true
        modelManager.sortFilteredCard(COMPARATOR_ASC_SCORE_CARDS);
        assertTrue(modelManager.equals(new ModelManager(Collections.singletonList(cardFolder), userPrefs)));

    }
}
