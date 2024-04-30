package be.one16.cerbainterview.core;

import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.ValidatedIngredient;
import be.one16.cerbainterview.model.IngredientDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface IngredientService {
    Ingredient createIngredient(IngredientDataDTO ingredientData);
    Ingredient getIngredient(UUID uuid);
    IngredientEntity getIngredientEntityByUuid(UUID uuid);
    Page<Ingredient> getPagedIngredients(PageRequest pageRequest);
    Ingredient updateIngredient(UUID ingredientUUID, IngredientDataDTO createIngredientDTO);
    void deleteIngredient(UUID uuid);
    List<ValidatedIngredient> validateIngredients(List<RecipeIngredientDataDTO> recipeIngredients);
}
