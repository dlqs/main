package seedu.address.model;

/**
 * Signals that the operation is unable to find the specified card folder.
 */
public class CardFolderNotFoundException extends RuntimeException {

    public CardFolderNotFoundException(String message) {
        super(message);
    }
}
