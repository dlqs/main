package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.CardFolder;
import seedu.address.model.ReadOnlyCardFolder;
import seedu.address.model.card.Answer;
import seedu.address.model.card.Card;
import seedu.address.model.card.Email;
import seedu.address.model.card.Question;
import seedu.address.model.card.Score;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code CardFolder} with sample data.
 */
public class SampleDataUtil {
    public static Card[] getSampleCards() {
        return new Card[] {
            new Card(new Question("Alex Yeoh"), new Answer("87438807"), new Email("alexyeoh@example.com"),
                new Score(3, 5),
                getTagSet("friends")),
            new Card(new Question("Bernice Yu"), new Answer("99272758"), new Email("berniceyu@example.com"),
                new Score(5, 7),
                getTagSet("colleagues", "friends")),
            new Card(new Question("Charlotte Oliveiro"), new Answer("93210283"), new Email("charlotte@example.com"),
                new Score(11, 20),
                getTagSet("neighbours")),
            new Card(new Question("David Li"), new Answer("91031282"), new Email("lidavid@example.com"),
                new Score(3, 13),
                getTagSet("family")),
            new Card(new Question("Irfan Ibrahim"), new Answer("92492021"), new Email("irfan@example.com"),
                new Score(69, 420),
                getTagSet("classmates")),
            new Card(new Question("Roy Balakrishnan"), new Answer("92624417"), new Email("royb@example.com"),
                new Score(8, 102),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyCardFolder getSampleCardFolder() {
        CardFolder sampleAb = new CardFolder();
        for (Card sampleCard : getSampleCards()) {
            sampleAb.addCard(sampleCard);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
