package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class GenderContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("m");
        List<String> secondPredicateKeywordList = Arrays.asList("m", "f");

        GenderContainsKeywordsPredicate firstPredicate = new GenderContainsKeywordsPredicate(
                firstPredicateKeywordList);
        GenderContainsKeywordsPredicate secondPredicate = new GenderContainsKeywordsPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        GenderContainsKeywordsPredicate firstPredicateCopy = new GenderContainsKeywordsPredicate(
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
    public void test_genderContainsKeywords_returnsTrue() {
        // One keyword
        GenderContainsKeywordsPredicate predicate = new GenderContainsKeywordsPredicate(Collections.singletonList("m"));
        assertTrue(predicate.test(new PersonBuilder().withGender("m").build()));

        // Multiple keywords
        predicate = new GenderContainsKeywordsPredicate(Arrays.asList("m", "f"));
        assertTrue(predicate.test(new PersonBuilder().withGender("m").build()));
        assertTrue(predicate.test(new PersonBuilder().withGender("f").build()));

        // Mixed-case keywords
        predicate = new GenderContainsKeywordsPredicate(Arrays.asList("M"));
        assertTrue(predicate.test(new PersonBuilder().withGender("m").build()));
    }

    @Test
    public void test_genderDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        GenderContainsKeywordsPredicate predicate = new GenderContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withGender("m").build()));

        // Non-matching keyword
        predicate = new GenderContainsKeywordsPredicate(Arrays.asList("f"));
        assertFalse(predicate.test(new PersonBuilder().withGender("m").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("m", "f");
        GenderContainsKeywordsPredicate predicate = new GenderContainsKeywordsPredicate(keywords);

        String expected = GenderContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
