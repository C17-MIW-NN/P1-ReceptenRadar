package nl.miwnn.ch17.briljant.receptenradar.controller;

import nl.miwnn.ch17.briljant.receptenradar.model.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Iris Loermans
 * purpose of the code
 */
class DirectionsControllerTest {

    @Test
    @DisplayName("Test the amount of strings present in the Directions Array list")
    void testTheAmountOfStringsPresentInDirectionsArrayList() {
        int expectedAmountOfStrings = 2;

        Recipe recipe = new Recipe();
        recipe.getSteps().add("Preheat oven to 180°C");
        recipe.getSteps().add("Mix ingredients");

        int amountOfStrings = recipe.getSteps().size();

        assertEquals(expectedAmountOfStrings, amountOfStrings);
    }
}