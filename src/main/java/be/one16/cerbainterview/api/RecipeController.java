package be.one16.cerbainterview.api;

import be.one16.cerbainterview.core.RecipeService;
import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.model.GetAllRecipes200ResponseDTO;
import be.one16.cerbainterview.model.RecipeDTO;
import be.one16.cerbainterview.model.RecipeDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RecipeController implements RecipesApi {

    private final RecipeService recipeService;
    private final ApiMapper mapper;

    @Override
    public ResponseEntity<RecipeDTO> addIngredientToRecipe(UUID recipeUUID, RecipeIngredientDataDTO recipeIngredientDataDTO) {
        Recipe recipe = recipeService.addIngredientToRecipe(recipeUUID, recipeIngredientDataDTO);
        return ResponseEntity.ok(mapper.toDTO(recipe));
    }

    @Override
    public ResponseEntity<Void> removeIngredientFromRecipe(UUID recipeUUID, UUID ingredientUUID) {
        recipeService.removeIngredientFromRecipe(recipeUUID, ingredientUUID);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RecipeDTO> createRecipe(RecipeDataDTO recipeDataDTO) {
        Recipe recipe = recipeService.createRecipe(recipeDataDTO);
        RecipeDTO recipeDTO = mapper.toDTO(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeDTO);
    }

    @Override
    public ResponseEntity<RecipeDTO> getRecipeByUUID(UUID recipeUUID) {
        Recipe recipe = recipeService.getRecipe(recipeUUID);
        return ResponseEntity.ok(mapper.toDTO(recipe));
    }

    @Override
    public ResponseEntity<RecipeDTO> updateRecipeByUUID(UUID recipeUUID, RecipeDataDTO recipeDataDTO) {
        Recipe recipe = recipeService.updateRecipe(recipeUUID, recipeDataDTO);
        return ResponseEntity.ok(mapper.toDTO(recipe));
    }

    @Override
    public ResponseEntity<Void> deleteRecipeByUUID(UUID recipeUUID) {
        recipeService.deleteRecipe(recipeUUID);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetAllRecipes200ResponseDTO> getAllRecipes(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));
        Page<Recipe> pagedIngredients = recipeService.getPagedRecipes(pageRequest);
        return ResponseEntity.ok(mapper.toGetPagedRecipesDTO(pagedIngredients));
    }
}
