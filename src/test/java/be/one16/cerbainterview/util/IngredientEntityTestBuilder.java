package be.one16.cerbainterview.util;

import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.db.entities.RecipeIngredientEntity;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientEntityTestBuilder {
    private Long id;
    private UUID uuid = UUID.randomUUID();
    private String name = RandomStringUtils.randomAlphanumeric(10);
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    public static IngredientEntityTestBuilder defaultIngredientEntityBuilder() {
        return new IngredientEntityTestBuilder();
    }

    public IngredientEntityTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public IngredientEntityTestBuilder withUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public IngredientEntityTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IngredientEntityTestBuilder withRecipeIngredients(List<RecipeIngredientEntity> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public IngredientEntity build() {
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(id);
        ingredientEntity.setUuid(uuid);
        ingredientEntity.setName(name);
        ingredientEntity.setRecipeIngredients(recipeIngredients);
        return ingredientEntity;
    }
}
