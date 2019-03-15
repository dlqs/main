package seedu.address.storage.csvmanager;

import static java.util.Objects.requireNonNull;

/**
 * represents a card folder for export
 */
public class CardFolderExport {

    public static final String MESSAGE_CONSTRAINTS = "Card Folder for export should not be blank";

    public final String folderName;

    public CardFolderExport(String folderName) {
        requireNonNull(folderName);
        this.folderName = folderName;
    }

    public static boolean isFolderNameEmpty(String folderName) {
        return folderName.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof CardFolderExport && ((CardFolderExport) obj)
                .folderName.equals(this.folderName);
    }

    @Override
    public int hashCode() {
        return folderName.hashCode();
    }
}
