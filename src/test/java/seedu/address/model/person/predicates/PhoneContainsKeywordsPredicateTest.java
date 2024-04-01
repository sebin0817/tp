package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("92345678");
        List<String> secondPredicateKeywordList = Arrays.asList("92345678", "98765432");

        PhoneContainsKeywordsPredicate firstPredicate = new PhoneContainsKeywordsPredicate(
                firstPredicateKeywordList);
        PhoneContainsKeywordsPredicate secondPredicate = new PhoneContainsKeywordsPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PhoneContainsKeywordsPredicate firstPredicateCopy = new PhoneContainsKeywordsPredicate(
                firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(
                Collections.singletonList("92345678"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("92345678").build()));

        // Multiple keywords
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("92345678", "98765432"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("92345678").build()));
        assertTrue(predicate.test(new PersonBuilder().withPhone("98765432").build()));

        // Only one matching keyword
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("98765432", "81111111"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("81111111").build()));
    }

    @Test
    public void test_phoneDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withPhone("82345678").build()));

        // Non-matching keyword
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("98765432"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("62345678").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(keywords);

        String expected = PhoneContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
