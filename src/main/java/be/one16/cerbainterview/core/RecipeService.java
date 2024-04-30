package be.one16.cerbainterview.core;

import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.model.RecipeDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface RecipeService {
    Recipe createRecipe(RecipeDataDTO recipe);
    Recipe getRecipe(UUID uuid);
    Page<Recipe> getPagedRecipes(PageRequest pageRequest);
    Recipe updateRecipe(UUID recipeUUID, RecipeDataDTO recipeDataDTO);
    void deleteRecipe(UUID uuid);
    Recipe addIngredientToRecipe(UUID recipeUUID, RecipeIngredientDataDTO recipeIngredientDataDTO);
    void removeIngredientFromRecipe(UUID recipeUUID, UUID ingredientUUID);
}
