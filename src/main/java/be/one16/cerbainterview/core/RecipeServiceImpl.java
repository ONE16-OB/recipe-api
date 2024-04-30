package be.one16.cerbainterview.core;

import be.one16.cerbainterview.core.exceptions.IngredientNotFoundException;
import be.one16.cerbainterview.core.exceptions.RecipeException;
import be.one16.cerbainterview.core.exceptions.RecipeNotFoundException;
import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.db.entities.RecipeEntity;
import be.one16.cerbainterview.db.entities.RecipeIngredientEntity;
import be.one16.cerbainterview.db.mapper.RecipeDbMapper;
import be.one16.cerbainterview.db.repositories.RecipeRepository;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.domain.ValidatedIngredient;
import be.one16.cerbainterview.model.RecipeDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final RecipeDbMapper mapper;

    @Override
    @Transactional
    public Recipe createRecipe(RecipeDataDTO recipeData) {
        List<ValidatedIngredient> validatedIngredients = ingredientService.validateIngredients(recipeData.getIngredients());

        RecipeEntity recipeEntity = mapper.toEntity(recipeData);
        recipeEntity.setUuid(UUID.randomUUID());
        recipeEntity.setIngredients(validatedIngredients.stream().map(validatedIngredient -> {
            RecipeIngredientEntity recipeIngredientEntity = new RecipeIngredientEntity();
            recipeIngredientEntity.setIngredient(validatedIngredient.ingredientEntity());
            recipeIngredientEntity.setRecipe(recipeEntity);
            recipeIngredientEntity.setQuantity(validatedIngredient.quantity());
            return recipeIngredientEntity;
        }).toList());
        RecipeEntity savedRecipe = recipeRepository.save(recipeEntity);
        return mapper.toModel(savedRecipe);
    }

    @Override
    @Transactional(readOnly = true)
    public Recipe getRecipe(UUID uuid) {
        RecipeEntity recipeEntity = getRecipeByUuid(uuid);
        return mapper.toModel(recipeEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> getPagedRecipes(PageRequest pageRequest) {
        Page<RecipeEntity> pagedRecipeEntities = recipeRepository.findAll(pageRequest);

        //sort internal list
        List<Recipe> pagedRecipes = pagedRecipeEntities.stream()
                .map(pagedRecipe -> {
                    Recipe recipe = mapper.toModel(pagedRecipe);
                    List<Ingredient> sortedIngredients = recipe.getIngredients().stream()
                            .sorted(Comparator.comparing(Ingredient::getName))
                            .collect(Collectors.toList());
                    recipe.setIngredients(sortedIngredients);
                    return recipe;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(pagedRecipes, pageRequest, pagedRecipeEntities.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteRecipe(UUID uuid) {
        RecipeEntity recipeEntity = getRecipeByUuid(uuid);
        recipeRepository.delete(recipeEntity);
    }

    @Override
    @Transactional
    public void removeIngredientFromRecipe(UUID recipeUUID, UUID ingredientUUID) {
        RecipeEntity recipeEntity = getRecipeByUuid(recipeUUID);

        boolean ingredientRemoved = recipeEntity.getIngredients()
                .removeIf(ingredient -> ingredient.getIngredient().getUuid().equals(ingredientUUID));

        if (!ingredientRemoved) {
            throw new IngredientNotFoundException("Ingredient not found in recipe");
        }
        recipeRepository.save(recipeEntity);
    }

    private RecipeEntity getRecipeByUuid(UUID uuid) {
        return recipeRepository.findByUuid(uuid)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "No Recipe found for the provided uuid: %s".formatted(uuid)));
    }

    @Override
    @Transactional
    public Recipe updateRecipe(UUID recipeUUID, RecipeDataDTO recipeDataDTO) {
        RecipeEntity recipeEntity = getRecipeByUuid(recipeUUID);

        // Update recipe properties
        recipeEntity.setName(recipeDataDTO.getName());
        recipeEntity.setDescription(recipeDataDTO.getDescription());
        recipeEntity.setCookingInstructions(recipeDataDTO.getCookingInstructions());

        // Update recipe ingredients
        List<RecipeIngredientEntity> updatedRecipeIngredients = new ArrayList<>();
        for (RecipeIngredientDataDTO updatedIngredient : recipeDataDTO.getIngredients()) {
            // Check if the ingredient already exists in the recipe
            Optional<RecipeIngredientEntity> existingRecipeIngredient = recipeEntity.getIngredients().stream()
                    .filter(ingredient -> ingredient.getIngredient().getUuid().equals(updatedIngredient.getUuid()))
                    .findFirst();

            if (existingRecipeIngredient.isPresent()) {
                // Update existing ingredient
                RecipeIngredientEntity existingIngredientEntity = existingRecipeIngredient.get();
                existingIngredientEntity.setQuantity(updatedIngredient.getQuantity());
                updatedRecipeIngredients.add(existingIngredientEntity);
            } else {
                // Create new ingredient entity
                IngredientEntity ingredientEntity = ingredientService.getIngredientEntityByUuid(updatedIngredient.getUuid());
                RecipeIngredientEntity newRecipeIngredient = new RecipeIngredientEntity();
                newRecipeIngredient.setRecipe(recipeEntity);
                newRecipeIngredient.setIngredient(ingredientEntity);
                newRecipeIngredient.setQuantity(updatedIngredient.getQuantity());
                updatedRecipeIngredients.add(newRecipeIngredient);
            }
        }

        recipeEntity.setIngredients(updatedRecipeIngredients);

        RecipeEntity updatedRecipe = recipeRepository.save(recipeEntity);
        return mapper.toModel(updatedRecipe);
    }

    @Override
    @Transactional
    public Recipe addIngredientToRecipe(UUID recipeUUID, RecipeIngredientDataDTO recipeIngredientDataDTO) {
        RecipeEntity recipeEntity = getRecipeByUuid(recipeUUID);
        IngredientEntity ingredient = ingredientService.getIngredientEntityByUuid(recipeIngredientDataDTO.getUuid());

        boolean ingredientAlreadyExists = recipeEntity.getIngredients().stream()
                .anyMatch(recipeIngredient -> recipeIngredient.getIngredient().getUuid().equals(ingredient.getUuid()));
        if (ingredientAlreadyExists) {
            throw new RecipeException("Ingredient already exists in the recipe");
        }

        RecipeIngredientEntity recipeIngredientEntity = new RecipeIngredientEntity();
        recipeIngredientEntity.setRecipe(recipeEntity);
        recipeIngredientEntity.setIngredient(ingredient);
        recipeIngredientEntity.setQuantity(recipeIngredientDataDTO.getQuantity());

        recipeEntity.getIngredients().add(recipeIngredientEntity);
        RecipeEntity updatedRecipeEntity = recipeRepository.save(recipeEntity);
        return mapper.toModel(updatedRecipeEntity);
    }
}
