package be.one16.cerbainterview.domain;

import be.one16.cerbainterview.db.entities.IngredientEntity;

public record ValidatedIngredient (
        IngredientEntity ingredientEntity,
        String quantity
){}
