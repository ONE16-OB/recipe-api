package be.one16.cerbainterview.api;

import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.Recipe;
import be.one16.cerbainterview.model.*;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    IngredientDTO toDTO(Ingredient createdIngredient);

    RecipeDTO toDTO(Recipe recipe);

    default GetAllIngredients200ResponseDTO toGetPagedIngredientsDTO(Page<Ingredient> pagedIngredients) {
        GetAllIngredients200ResponseDTO responseDTO = new GetAllIngredients200ResponseDTO();
        if (pagedIngredients != null) {
            responseDTO.setContent(pagedIngredients.getContent().stream().map(this::toDTO).toList());
            responseDTO.setPageable(pageableToPageableDTO(pagedIngredients));
        }
        return responseDTO;
    }
    default GetAllRecipes200ResponseDTO toGetPagedRecipesDTO(Page<Recipe> pagedRecipes) {
        GetAllRecipes200ResponseDTO responseDTO = new GetAllRecipes200ResponseDTO();
        if (pagedRecipes != null) {
            responseDTO.setContent(pagedRecipes.getContent().stream().map(this::toDTO).toList());
            responseDTO.setPageable(pageableToPageableDTO(pagedRecipes));
        }
        return responseDTO;
    }

    default PageableDTO pageableToPageableDTO(Page<?> pageable) {
        if ( pageable == null ) {
            return null;
        }

        PageableDTO pageableDTO = new PageableDTO();

        pageableDTO.setPageNumber( pageable.getNumber());
        pageableDTO.setPageSize( pageable.getSize() );
        pageableDTO.setTotalElements(pageable.getTotalElements());
        pageableDTO.setHasNext( pageable.hasNext());
        pageableDTO.setHasPrevious( pageable.hasPrevious());

        return pageableDTO;
    }
}
