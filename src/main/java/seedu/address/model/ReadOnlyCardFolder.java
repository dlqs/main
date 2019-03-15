package seedu.address.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.address.model.card.Card;

/**
 * Unmodifiable view of an card folder
 */
public interface ReadOnlyCardFolder extends Observable {

    /**
     * Returns an unmodifiable view of the cards list.
     * This list will not contain any duplicate cards.
     */
    ObservableList<Card> getCardList();

    /**
     * Returns the name of the folder.
     */
    String getFolderName();
}
