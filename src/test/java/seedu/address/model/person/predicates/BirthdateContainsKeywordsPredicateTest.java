package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class BirthdateContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("05-11-2000");
        List<String> secondPredicateKeywordList = Arrays.asList("05-11-2000", "10-10-1999");

        BirthdateContainsKeywordsPredicate firstPredicate = new BirthdateContainsKeywordsPredicate(
                    firstPredicateKeywordList);
        BirthdateContainsKeywordsPredicate secondPredicate = new BirthdateContainsKeywordsPredicate(
                    secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        BirthdateContainsKeywordsPredicate firstPredicateCopy = new BirthdateContainsKeywordsPredicate(
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
    public void test_birthdateContainsKeywords_returnsTrue() {
        // One keyword
        BirthdateContainsKeywordsPredicate predicate = new BirthdateContainsKeywordsPredicate(
                Collections.singletonList("05-11-2000"));
        assertTrue(predicate.test(new PersonBuilder().withBirthDate("05-11-2000").build()));

        // Multiple keywords
        predicate = new BirthdateContainsKeywordsPredicate(Arrays.asList("05-11-2000", "10-10-1999"));
        assertTrue(predicate.test(new PersonBuilder().withBirthDate("05-11-2000").build()));
        assertTrue(predicate.test(new PersonBuilder().withBirthDate("10-10-1999").build()));

        // Only one matching keyword
        predicate = new BirthdateContainsKeywordsPredicate(Arrays.asList("10-10-1999", "01-01-2001"));
        assertTrue(predicate.test(new PersonBuilder().withBirthDate("01-01-2001").build()));
    }

    @Test
    public void test_birthdateDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        BirthdateContainsKeywordsPredicate predicate = new BirthdateContainsKeywordsPredicate(
                Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withBirthDate("05-11-2000").build()));

        // Non-matching keyword
        predicate = new BirthdateContainsKeywordsPredicate(Arrays.asList("10-10-1999"));
        assertFalse(predicate.test(new PersonBuilder().withBirthDate("05-11-2000").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        BirthdateContainsKeywordsPredicate predicate = new BirthdateContainsKeywordsPredicate(keywords);

        String expected = BirthdateContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
