package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.CardFolder;
import seedu.address.model.card.Card;

/**
 * A utility class containing a list of {@code Card} objects to be used in tests.
 */
public class TypicalCards {

    public static final Card ALICE = new CardBuilder().withQuestion("Alice Pauline")
            .withScore("4/11").withEmail("alice@example.com")
            .withAnswer("94351253")
            .withTags("friends").build();
    public static final Card BENSON = new CardBuilder().withQuestion("Benson Meier")
            .withScore("3/5")
            .withEmail("johnd@example.com").withAnswer("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Card CARL = new CardBuilder().withQuestion("Carl Kurz").withAnswer("95352563")
            .withEmail("heinz@example.com").withScore("0/0").build();
    public static final Card DANIEL = new CardBuilder().withQuestion("Daniel Meier").withAnswer("87652533")
            .withEmail("cornelia@example.com").withScore("120/420").withTags("friends").build();
    public static final Card ELLE = new CardBuilder().withQuestion("Elle Meyer").withAnswer("9482224")
            .withEmail("werner@example.com").withScore("100/312").build();
    public static final Card FIONA = new CardBuilder().withQuestion("Fiona Kunz").withAnswer("9482427")
            .withEmail("lydia@example.com").withScore("1/99").build();
    public static final Card GEORGE = new CardBuilder().withQuestion("George Best").withAnswer("9482442")
            .withEmail("anna@example.com").withScore("42/61").build();

    // Manually added
    public static final Card HOON = new CardBuilder().withQuestion("Hoon Meier").withAnswer("8482424")
            .withEmail("stefan@example.com").withScore("4/61").build();
    public static final Card IDA = new CardBuilder().withQuestion("Ida Mueller").withAnswer("8482131")
            .withEmail("hans@example.com").withScore("20/32").build();

    // Manually added - Card's details found in {@code CommandTestUtil}
    public static final Card AMY = new CardBuilder().withQuestion(VALID_QUESTION_AMY).withAnswer(VALID_ANSWER_AMY)
            .withEmail(VALID_EMAIL_AMY).withScore(VALID_SCORE_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Card BOB = new CardBuilder().withQuestion(VALID_QUESTION_BOB).withAnswer(VALID_ANSWER_BOB)
            .withEmail(VALID_EMAIL_BOB).withScore(VALID_SCORE_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalCards() {} // prevents instantiation

    /**
     * Returns an {@code CardFolder} with all the typical cards.
     */
    public static CardFolder getTypicalCardFolder() {
        CardFolder ab = new CardFolder();
        for (Card card : getTypicalCards()) {
            ab.addCard(card);
        }
        return ab;
    }

    public static List<Card> getTypicalCards() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
