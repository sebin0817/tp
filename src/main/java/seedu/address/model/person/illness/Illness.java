package seedu.address.model.person.illness;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an Illness in the patient book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidIllnessName(String)} (String)}
 */
public class Illness {
    public static final String MESSAGE_CONSTRAINTS = "Illness names should be of the following options \n"
            + "(You could type just the first word / letters for convenience "
            + "as long it corresponds to a single category.): \n"
            + "Infectious Disease, Chronic Conditions, Autoimmune Disorders, Genetic Disorders, \n"
            + "Mental Disorders, Neurological Disorders, Metabolic Disorders, Nutritional Deficiencies, \n"
            + "Environmental Illnesses, Degenerative Diseases or Others.";

    public static final String FIND_MESSAGE_CONSTRAINTS = "Illness names should only contain alphabetical characters "
            + "and match any of the predefined illness keywords: \n"
            + "Infectious Disease, Chronic Conditions, Autoimmune Disorders, Genetic Disorders, \n"
            + "Mental Disorders, Neurological Disorders, Metabolic Disorders, Nutritional Deficiencies, \n"
            + "Environmental Illnesses, Degenerative Diseases or Others. \n"
            + "Additionally, users may specify only a portion of the illness keywords, "
            + "such as 'Infectious' from 'Infectious Diseases'.";

    private static final List<String> ILLNESSES_ARRAY = Arrays.asList(
            "Infectious Diseases", "Chronic Conditions", "Autoimmune Disorders", "Genetic Disorders",
            "Mental Disorders", "Neurological Disorders", "Metabolic Disorders",
            "Nutritional Deficiencies", "Environmental Illnesses", "Degenerative Diseases", "Others");

    private static final Set<String> VALID_ILLNESS_CATEGORIES = new HashSet<>(ILLNESSES_ARRAY);
    private static final List<String> VALID_FIND_ILLNESS_KEYWORDS = VALID_ILLNESS_CATEGORIES.stream()
            .flatMap(category -> Arrays.stream(category.split("\\s+")))
            .map(String::toLowerCase)
            .collect(Collectors.toList());

    public final String illnessName;

    /**
     * Constructs a {@code Illness}.
     *
     * @param illnessName A valid illness name.
     */
    public Illness(String illnessName) {
        requireNonNull(illnessName);
        String matchedIllness = findMatchingCategory(illnessName);
        checkArgument(isValidIllnessName(illnessName), MESSAGE_CONSTRAINTS);
        this.illnessName = matchedIllness;
    }

    /**
     * Returns true if a given string is a valid illness.
     */
    public static boolean isValidIllnessName(String test) {
        return findMatchingCategory(test) != null;
    }

    /**
     * Returns true if a given string is a valid illness find query.
     */
    public static boolean isValidIllnessFind(String test) {
        List<String> keywords = Arrays.asList(test.split("\\s+"));
        for (int i = 0; i < keywords.size(); i++) {
            if (!VALID_FIND_ILLNESS_KEYWORDS.contains(keywords.get(i).toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds and returns the full illness category that partially and case-insensitively matches the input.
     *
     * @param illnessInput The input string to match.
     * @return The full matched category name if match is found and is unique; null otherwise.
     */
    private static String findMatchingCategory(String illnessInput) {
        String inputLowerCase = illnessInput.toLowerCase();
        Set<String> matchedCategories = VALID_ILLNESS_CATEGORIES.stream()
                .filter(category -> category.toLowerCase().startsWith(inputLowerCase))
                .collect(Collectors.toSet());

        if (matchedCategories.size() != 1) {
            return null;
        }

        return matchedCategories.iterator().next();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Illness)) {
            return false;
        }

        Illness otherIllness = (Illness) other;
        return illnessName.equals(otherIllness.illnessName);
    }

    @Override
    public int hashCode() {
        return illnessName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + illnessName + ']';
    }

}
