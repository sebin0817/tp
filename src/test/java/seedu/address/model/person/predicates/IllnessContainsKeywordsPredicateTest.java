package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class IllnessContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("Infectious Disease");
        List<String> secondPredicateKeywordList = Arrays.asList("Infectious Disease", "Genetic Disorders");

        IllnessContainsKeywordsPredicate firstPredicate = new IllnessContainsKeywordsPredicate(
                firstPredicateKeywordList);
        IllnessContainsKeywordsPredicate secondPredicate = new IllnessContainsKeywordsPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        IllnessContainsKeywordsPredicate firstPredicateCopy = new IllnessContainsKeywordsPredicate(
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
    public void test_illnessContainsKeywords_returnsTrue() {
        // One keyword
        IllnessContainsKeywordsPredicate predicate = new IllnessContainsKeywordsPredicate(
                Collections.singletonList("Infectious"));
        assertTrue(predicate.test(new PersonBuilder().withIllnesses("Infectious Diseases").build()));

        // Multiple keywords
        predicate = new IllnessContainsKeywordsPredicate(Arrays.asList("Genetic", "Disorders"));
        assertTrue(predicate.test(new PersonBuilder().withIllnesses("Genetic").build()));
        assertTrue(predicate.test(new PersonBuilder().withIllnesses("Genetic Disorders").build()));

        // Only one matching keyword
        predicate = new IllnessContainsKeywordsPredicate(Arrays.asList("Infectious", "Genetic"));
        assertTrue(predicate.test(new PersonBuilder().withIllnesses("Infectious Diseases").build()));

        // Mixed-case keywords
        predicate = new IllnessContainsKeywordsPredicate(Arrays.asList("GeneTIC", "diOrders"));
        assertTrue(predicate.test(new PersonBuilder().withIllnesses("Genetic Disorders").build()));
    }

    @Test
    public void test_illnessDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        IllnessContainsKeywordsPredicate predicate = new IllnessContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withIllnesses("Infectious").build()));

        // Non-matching keyword
        predicate = new IllnessContainsKeywordsPredicate(Arrays.asList("Infectious"));
        assertFalse(predicate.test(new PersonBuilder().withIllnesses("Genetic").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        IllnessContainsKeywordsPredicate predicate = new IllnessContainsKeywordsPredicate(keywords);

        String expected = IllnessContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

