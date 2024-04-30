package be.one16.cerbainterview.util;

import be.one16.cerbainterview.domain.Recipe;

import java.util.Collections;
import java.util.UUID;

public class RecipeTestBuilder {

    private static final String DEFAULT_NAME = "Test Recipe";
    private static final String DEFAULT_DESCRIPTION = "Test Description";
    private static final String DEFAULT_COOKING_INSTRUCTIONS = "Test Cooking Instructions";

    public static Recipe.RecipeBuilder defaultRecipeBuilder() {
        return Recipe.builder()
                .uuid(UUID.randomUUID())
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .cookingInstructions(DEFAULT_COOKING_INSTRUCTIONS)
                .ingredients(Collections.emptyList());
    }

    public static Recipe buildDefaultRecipe() {
        return defaultRecipeBuilder().build();
    }
}
