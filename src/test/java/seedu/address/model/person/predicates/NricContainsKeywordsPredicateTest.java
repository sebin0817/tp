package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NricContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("S1234567A");
        List<String> secondPredicateKeywordList = Arrays.asList("S1234567A", "T9876543B");

        NricContainsKeywordsPredicate firstPredicate = new NricContainsKeywordsPredicate(firstPredicateKeywordList);
        NricContainsKeywordsPredicate secondPredicate = new NricContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NricContainsKeywordsPredicate firstPredicateCopy = new NricContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nricContainsKeywords_returnsTrue() {
        // One keyword
        NricContainsKeywordsPredicate predicate = new NricContainsKeywordsPredicate(
                Collections.singletonList("S1234567A"));
        assertTrue(predicate.test(new PersonBuilder().withNric("S1234567A").build()));

        // Multiple keywords
        predicate = new NricContainsKeywordsPredicate(Arrays.asList("S1234567A", "T9876543B"));
        assertTrue(predicate.test(new PersonBuilder().withNric("S1234567A").build()));
        assertTrue(predicate.test(new PersonBuilder().withNric("T9876543B").build()));

        // Only one matching keyword
        predicate = new NricContainsKeywordsPredicate(Arrays.asList("T9876543B", "S1111111C"));
        assertTrue(predicate.test(new PersonBuilder().withNric("S1111111C").build()));
    }

    @Test
    public void test_nricDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NricContainsKeywordsPredicate predicate = new NricContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withNric("S1234567A").build()));

        // Non-matching keyword
        predicate = new NricContainsKeywordsPredicate(Arrays.asList("T9876543B"));
        assertFalse(predicate.test(new PersonBuilder().withNric("S1234567A").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NricContainsKeywordsPredicate predicate = new NricContainsKeywordsPredicate(keywords);

        String expected = NricContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
