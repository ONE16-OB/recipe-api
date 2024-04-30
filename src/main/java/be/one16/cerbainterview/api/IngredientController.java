package be.one16.cerbainterview.api;

import be.one16.cerbainterview.core.IngredientService;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.model.GetAllIngredients200ResponseDTO;
import be.one16.cerbainterview.model.IngredientDTO;
import be.one16.cerbainterview.model.IngredientDataDTO;
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
public class IngredientController implements IngredientsApi{

    private final IngredientService ingredientService;
    private final ApiMapper mapper;

    @Override
    public ResponseEntity<IngredientDTO> createIngredient(IngredientDataDTO ingredientDataDTO) {
        Ingredient createdIngredient = ingredientService.createIngredient(ingredientDataDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(createdIngredient));
    }

    @Override
    public ResponseEntity<IngredientDTO> getIngredientByUUID(UUID ingredientUUID) {
        Ingredient ingredient = ingredientService.getIngredient(ingredientUUID);
        return ResponseEntity.ok(mapper.toDTO(ingredient));
    }

    @Override
    public ResponseEntity<IngredientDTO> updateIngredientByUUID(UUID ingredientUUID, IngredientDataDTO ingredientData) {
        Ingredient ingredient = ingredientService.updateIngredient(ingredientUUID, ingredientData);
        return ResponseEntity.ok(mapper.toDTO(ingredient));
    }

    @Override
    public ResponseEntity<GetAllIngredients200ResponseDTO> getAllIngredients(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));
        Page<Ingredient> pagedIngredients = ingredientService.getPagedIngredients(pageRequest);
        return ResponseEntity.ok(mapper.toGetPagedIngredientsDTO(pagedIngredients));
    }

    @Override
    public ResponseEntity<Void> deleteIngredientByUUID(UUID ingredientUUID) {
        ingredientService.deleteIngredient(ingredientUUID);
        return ResponseEntity.noContent().build();
    }
}
