package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class DrugAllergyContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("Penicillin");
        List<String> secondPredicateKeywordList = Arrays.asList("Penicillin", "Sulfa Drugs");

        DrugAllergyContainsKeywordsPredicate firstPredicate = new DrugAllergyContainsKeywordsPredicate(
                firstPredicateKeywordList);
        DrugAllergyContainsKeywordsPredicate secondPredicate = new DrugAllergyContainsKeywordsPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        DrugAllergyContainsKeywordsPredicate firstPredicateCopy = new DrugAllergyContainsKeywordsPredicate(
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
    public void test_drugAllergyContainsKeywords_returnsTrue() {
        // One keyword
        DrugAllergyContainsKeywordsPredicate predicate = new DrugAllergyContainsKeywordsPredicate(
                Collections.singletonList("Penicillin"));
        assertTrue(predicate.test(new PersonBuilder().withDrugAllergy("Penicillin").build()));

        // Multiple keywords
        predicate = new DrugAllergyContainsKeywordsPredicate(Arrays.asList("Penicillin", "Sulfa"));
        assertTrue(predicate.test(new PersonBuilder().withDrugAllergy("Penicillin").build()));
        assertTrue(predicate.test(new PersonBuilder().withDrugAllergy("Sulfa").build()));

        // Only one matching keyword
        predicate = new DrugAllergyContainsKeywordsPredicate(Arrays.asList("Drug1", "Drug2"));
        assertTrue(predicate.test(new PersonBuilder().withDrugAllergy("Drug1").build()));

        // Mixed-case keywords
        predicate = new DrugAllergyContainsKeywordsPredicate(Arrays.asList("PeNiCiLlIn", "SuLfA dRuGs"));
        assertTrue(predicate.test(new PersonBuilder().withDrugAllergy("Penicillin Sulfa Drugs").build()));
    }

    @Test
    public void test_drugAllergyDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        DrugAllergyContainsKeywordsPredicate predicate = new DrugAllergyContainsKeywordsPredicate(
                Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withDrugAllergy("Penicillin").build()));

        // Non-matching keyword
        predicate = new DrugAllergyContainsKeywordsPredicate(Arrays.asList("Sulfa"));
        assertFalse(predicate.test(new PersonBuilder().withDrugAllergy("Penicillin").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        DrugAllergyContainsKeywordsPredicate predicate = new DrugAllergyContainsKeywordsPredicate(keywords);

        String expected = DrugAllergyContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
