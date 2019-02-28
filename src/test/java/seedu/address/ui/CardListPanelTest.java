package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalCards.getTypicalCards;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CARD;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysCard;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.CardListPanelHandle;
import guitests.guihandles.CardThumbnailHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Email;
import seedu.address.model.card.Question;
import seedu.address.model.card.Score;

public class CardListPanelTest extends GuiUnitTest {
    private static final ObservableList<Card> TYPICAL_CARDS =
            FXCollections.observableList(getTypicalCards());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Card> selectedCard = new SimpleObjectProperty<>();
    private CardListPanelHandle cardListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_CARDS);

        for (int i = 0; i < TYPICAL_CARDS.size(); i++) {
            cardListPanelHandle.navigateToCard(TYPICAL_CARDS.get(i));
            Card expectedCard = TYPICAL_CARDS.get(i);
            CardThumbnailHandle actualCard = cardListPanelHandle.getCardCardHandle(i);

            assertCardDisplaysCard(expectedCard, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedCardChanged_selectionChanges() {
        initUi(TYPICAL_CARDS);
        Card secondCard = TYPICAL_CARDS.get(INDEX_SECOND_CARD.getZeroBased());
        guiRobot.interact(() -> selectedCard.set(secondCard));
        guiRobot.pauseForHuman();

        CardThumbnailHandle expectedCard = cardListPanelHandle.getCardCardHandle(INDEX_SECOND_CARD.getZeroBased());
        CardThumbnailHandle selectedCard = cardListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }

    /**
     * Verifies that creating and deleting large number of cards in {@code CardListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Card> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of card cards exceeded time limit");
    }

    /**
     * Returns a list of cards containing {@code cardCount} cards that is used to populate the
     * {@code CardListPanel}.
     */
    private ObservableList<Card> createBackingList(int cardCount) {
        ObservableList<Card> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < cardCount; i++) {
            Question question = new Question(i + "a");
            Answer answer = new Answer("000");
            Email email = new Email("a@aa");
            Score score = new Score("1/2");
            Card card = new Card(question, answer, email, score, Collections.emptySet());
            backingList.add(card);
        }
        return backingList;
    }

    /**
     * Initializes {@code cardListPanelHandle} with a {@code CardListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code CardListPanel}.
     */
    private void initUi(ObservableList<Card> backingList) {
        CardListPanel cardListPanel =
                new CardListPanel(backingList, selectedCard, selectedCard::set);
        uiPartRule.setUiPart(cardListPanel);

        cardListPanelHandle = new CardListPanelHandle(getChildNode(cardListPanel.getRoot(),
                CardListPanelHandle.CARD_LIST_VIEW_ID));
    }
}
