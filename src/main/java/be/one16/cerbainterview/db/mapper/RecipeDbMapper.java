package be.one16.cerbainterview.db.mapper;

import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.db.entities.RecipeEntity;
import be.one16.cerbainterview.db.entities.RecipeIngredientEntity;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.domain.TrackingData;
import be.one16.cerbainterview.model.RecipeDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeDbMapper {
    RecipeEntity toEntity(RecipeDataDTO recipeData);
    RecipeEntity toEntity(Recipe recipeData);
    RecipeIngredientEntity toEntity(RecipeIngredientDataDTO recipeData);

    @Mapping(source = "createdAt", target = "trackingData.createdAt")
    @Mapping(source = "updatedAt", target = "trackingData.updatedAt")
    Recipe toModel(RecipeEntity savedRecipe);

    default Ingredient toIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        IngredientEntity ingredient = recipeIngredientEntity.getIngredient();
        return Ingredient.builder()
                .uuid(ingredient.getUuid())
                .name(ingredient.getName())
                .quantity(recipeIngredientEntity.getQuantity())
                .trackingData(TrackingData.builder()
                        .createdAt(ingredient.getCreatedAt())
                        .updatedAt(ingredient.getUpdatedAt())
                        .build())
                .build();
    }
}
