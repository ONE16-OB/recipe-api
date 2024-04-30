package be.one16.cerbainterview.api;

import be.one16.cerbainterview.AbstractBaseTest;
import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.db.entities.RecipeEntity;
import be.one16.cerbainterview.db.entities.RecipeIngredientEntity;
import be.one16.cerbainterview.db.mapper.IngredientDbMapper;
import be.one16.cerbainterview.db.mapper.RecipeDbMapper;
import be.one16.cerbainterview.db.repositories.IngredientRepository;
import be.one16.cerbainterview.db.repositories.RecipeRepository;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import be.one16.cerbainterview.util.RecipeTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.one16.cerbainterview.util.IngredientEntityTestBuilder.defaultIngredientEntityBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeControllerTest extends AbstractBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientDbMapper ingredientDbMapper;
    @Autowired
    private RecipeDbMapper recipeDbMapper;

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    private List<IngredientEntity> baseKnownIngredients;

    @BeforeEach
    void setUp() {
        IngredientEntity ingredient1 = defaultIngredientEntityBuilder().build();
        IngredientEntity ingredient2 = defaultIngredientEntityBuilder().build();
        IngredientEntity ingredient3 = defaultIngredientEntityBuilder().build();
        baseKnownIngredients = ingredientRepository.saveAll(List.of(ingredient1, ingredient2, ingredient3));
    }

    @Test
    void testCreateRecipe() throws Exception {
        Recipe recipe = RecipeTestBuilder.buildDefaultRecipe();
        recipe.setIngredients(
                baseKnownIngredients.stream()
                        .map(ingredientEntity -> {
                            Ingredient ingredient = ingredientDbMapper.toModel(ingredientEntity);
                            ingredient.setQuantity("a bunch");
                            return ingredient;
                        })
                        .toList()
        );

        mockMvc.perform(post("/recipes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.description").value(recipe.getDescription()))
                .andExpect(jsonPath("$.cookingInstructions").value(recipe.getCookingInstructions()))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients", hasSize(baseKnownIngredients.size())))
                .andExpect(jsonPath("$.ingredients[*].uuid", containsInAnyOrder(baseKnownIngredients.stream().map(IngredientEntity::getUuid).map(java.util.UUID::toString).toArray())));
    }

    @Test
    void testDeleteRecipeByUUID() throws Exception {
        // Create a recipe to delete
        Recipe recipeToDelete = RecipeTestBuilder.buildDefaultRecipe();
        recipeRepository.save(recipeDbMapper.toEntity(recipeToDelete));

        // Perform the DELETE request to delete the recipe
        mockMvc.perform(delete("/recipes/{recipeUUID}", recipeToDelete.getUuid().toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify that the recipe is deleted from the database
        assertThat(recipeRepository.findByUuid(recipeToDelete.getUuid())).isEmpty();
    }

    @Test
    void testUpdateRecipeByUUID() throws Exception {
        // Create a recipe to update
        Recipe originalRecipe = RecipeTestBuilder.buildDefaultRecipe();
        recipeRepository.save(recipeDbMapper.toEntity(originalRecipe));

        // Modify the recipe data
        Recipe updatedRecipe = RecipeTestBuilder.buildDefaultRecipe();
        updatedRecipe.setName("Updated Recipe Name");
        updatedRecipe.setDescription("Updated Recipe Description");
        updatedRecipe.setCookingInstructions("Updated Cooking Instructions");

        // Update ingredients information in the updated recipe
        List<Ingredient> updatedIngredients = new ArrayList<>();
        for (Ingredient originalIngredient : originalRecipe.getIngredients()) {
            // Modify ingredient data
            Ingredient updatedIngredient = Ingredient.builder()
                    .name(originalIngredient.getName())
                    .quantity("Updated Quantity")
                    .build();

            updatedIngredients.add(updatedIngredient);
        }
        updatedRecipe.setIngredients(updatedIngredients);

        // Perform the PUT request to update the recipe
        mockMvc.perform(put("/recipes/{recipeUUID}", originalRecipe.getUuid().toString())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(originalRecipe.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(updatedRecipe.getName()))
                .andExpect(jsonPath("$.description").value(updatedRecipe.getDescription()))
                .andExpect(jsonPath("$.cookingInstructions").value(updatedRecipe.getCookingInstructions()))
                .andExpect(jsonPath("$.ingredients", hasSize(updatedRecipe.getIngredients().size())))
                .andExpect(jsonPath("$.ingredients[*].quantity", everyItem(equalTo("Updated Quantity"))));
    }

    @Test
    void testGetRecipeByUUID() throws Exception {
        // Create a recipe to retrieve
        Recipe recipeToRetrieve = RecipeTestBuilder.buildDefaultRecipe();
        recipeRepository.save(recipeDbMapper.toEntity(recipeToRetrieve));

        // Perform the GET request to retrieve the recipe
        mockMvc.perform(get("/recipes/{recipeUUID}", recipeToRetrieve.getUuid().toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(recipeToRetrieve.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(recipeToRetrieve.getName()))
                .andExpect(jsonPath("$.description").value(recipeToRetrieve.getDescription()))
                .andExpect(jsonPath("$.cookingInstructions").value(recipeToRetrieve.getCookingInstructions()));

        Optional<RecipeEntity> retrievedRecipeOptional = recipeRepository.findByUuid(recipeToRetrieve.getUuid());
        assertThat(retrievedRecipeOptional).isPresent();
        RecipeEntity retrievedRecipe = retrievedRecipeOptional.get();
        assertThat(retrievedRecipe.getName()).isEqualTo(recipeToRetrieve.getName());
        assertThat(retrievedRecipe.getDescription()).isEqualTo(recipeToRetrieve.getDescription());
        assertThat(retrievedRecipe.getCookingInstructions()).isEqualTo(recipeToRetrieve.getCookingInstructions());
    }

    @Test
    void testGetAllRecipesPaged() throws Exception {
        // Create some recipes in the database
        Recipe recipe1 = RecipeTestBuilder.buildDefaultRecipe();
        Recipe recipe2 = RecipeTestBuilder.buildDefaultRecipe();
        Recipe recipe3 = RecipeTestBuilder.buildDefaultRecipe();
        recipeRepository.saveAll(List.of(recipeDbMapper.toEntity(recipe1), recipeDbMapper.toEntity(recipe2), recipeDbMapper.toEntity(recipe3)));

        // Perform the GET request to retrieve paged recipes
        mockMvc.perform(get("/recipes")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[*].uuid").exists())
                .andExpect(jsonPath("$.content[*].name").exists())
                .andExpect(jsonPath("$.content[*].description").exists())
                .andExpect(jsonPath("$.content[*].cookingInstructions").exists());
    }

    @Test
    void testRemoveIngredientFromRecipe() throws Exception {
        // Create a recipe with ingredient in the database
        Recipe recipe = RecipeTestBuilder.buildDefaultRecipe();
        RecipeEntity entity = recipeDbMapper.toEntity(recipe);
        RecipeEntity saved = recipeRepository.save(entity);
        IngredientEntity first = baseKnownIngredients.getFirst();
        saved.setIngredients(List.of(RecipeIngredientEntity.builder()
                        .ingredient(first)
                        .recipe(saved)
                        .quantity("a lot")
                .build()));
        recipeRepository.save(entity);

        // Perform the DELETE request to remove the ingredient from the recipe
        mockMvc.perform(delete("/recipes/{recipeUUID}/ingredients/{ingredientUUID}",
                        recipe.getUuid(), baseKnownIngredients.getFirst().getUuid())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddIngredientToRecipe() throws Exception {
        // Create a recipe in the database
        Recipe recipe = RecipeTestBuilder.buildDefaultRecipe();
        RecipeEntity entity = recipeDbMapper.toEntity(recipe);
        RecipeEntity savedRecipeEntity = recipeRepository.save(entity);

        // Create an ingredient
        IngredientEntity ingredientEntity = defaultIngredientEntityBuilder().build();
        IngredientEntity savedIngredientEntity = ingredientRepository.save(ingredientEntity);

        // Prepare the request body with the ingredient data
        RecipeIngredientDataDTO recipeIngredientDataDTO = new RecipeIngredientDataDTO();
        recipeIngredientDataDTO.setUuid(savedIngredientEntity.getUuid());
        recipeIngredientDataDTO.setQuantity("some quantity");

        // Perform the POST request to add the ingredient to the recipe
        mockMvc.perform(post("/recipes/{recipeUUID}/ingredients", savedRecipeEntity.getUuid())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeIngredientDataDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.description").value(recipe.getDescription()))
                .andExpect(jsonPath("$.cookingInstructions").value(recipe.getCookingInstructions()))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].uuid").value(savedIngredientEntity.getUuid().toString()))
                .andExpect(jsonPath("$.ingredients[0].name").value(savedIngredientEntity.getName()))
                .andExpect(jsonPath("$.ingredients[0].quantity").value(recipeIngredientDataDTO.getQuantity()));
    }
}
