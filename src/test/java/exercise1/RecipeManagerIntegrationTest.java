package exercise1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeManagerIntegrationTest {

    RecipeManager recipeManager;

    @BeforeEach
    void setUp() {
        recipeManager = new RecipeManager();
    }

    // Exercise 1.1 - Test that a recipe can be added and retrieved from the recipe manager object
    @Test
    @DisplayName("Test add and retrieval of new recipe")
    void testAddAndGetRecipe_ShouldReturnAddedRecipe() {
        // arrange
        final String PANCAKES = "Pancakes";
        Recipe recipe1 = new Recipe(PANCAKES, "flour, milk, eggs", "Mix ingredients and cook.");
        RecipeManager recipeManager1 = new RecipeManager();

        // act
        recipeManager1.addRecipe(recipe1);

        // assert that objects are the same (reference equality)
        assertSame(recipe1, recipeManager1.getRecipe(PANCAKES));
    }

    // Exercise 1.2 - Test the retrieval of a recipe that doesn't exist
    @Test
    @DisplayName("Test retrieval of non-existent recipe")
    void testRetrievalOfNonExistentRecipe_ShouldReturnNull() {
        // arrange
        final String PANCAKES = "Pancakes";
        final String NOT_PANCAKES = "Not Pancakes";
        Recipe recipe1 = new Recipe(PANCAKES, "flour, milk, eggs", "Mix ingredients and cook.");
        RecipeManager recipeManager1 = new RecipeManager();

        // act
        recipeManager1.addRecipe(recipe1);

        // assert returns null
        assertNull( recipeManager1.getRecipe(NOT_PANCAKES));
    }

    // Exercise 1.3 - Test that duplicate recipe names retrieve the first occurrence
    @Test
    @DisplayName("Adding recipes with duplicate names should retrieve the first added recipe on search")
    void testRecipesWithDuplicateNamesShouldRetrieveFirstAdded() {
        final String PANCAKES = "Pancakes";
        Recipe recipe1 = new Recipe(PANCAKES, "flour, milk, eggs", "Mix ingredients and cook.");
        Recipe recipe2 = new Recipe(PANCAKES, "flour, milk, eggs", "Mix ingredients and cook.");
        RecipeManager recipeManager1 = new RecipeManager();

        // act
        recipeManager1.addRecipe(recipe2);
        recipeManager1.addRecipe(recipe1);

        // assert same object returned as first added
        assertSame(recipe2, recipeManager1.getRecipe(PANCAKES));
    }
}
