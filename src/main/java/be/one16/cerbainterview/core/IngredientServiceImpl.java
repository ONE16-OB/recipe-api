package be.one16.cerbainterview.core;

import be.one16.cerbainterview.core.exceptions.IngredientNotFoundException;
import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.db.mapper.IngredientDbMapper;
import be.one16.cerbainterview.db.repositories.IngredientRepository;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.domain.ValidatedIngredient;
import be.one16.cerbainterview.model.IngredientDataDTO;
import be.one16.cerbainterview.model.RecipeIngredientDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService{

    private final IngredientRepository ingredientRepository;
    private final IngredientDbMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Ingredient getIngredient(UUID uuid) {
        IngredientEntity ingredientEntity = getIngredientEntityByUuid(uuid);
        return mapper.toModel(ingredientEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ingredient> getPagedIngredients(PageRequest pageRequest) {
        return ingredientRepository.findAll(pageRequest)
                .map(mapper::toModel);
    }

    @Override
    @Transactional
    public Ingredient createIngredient(IngredientDataDTO ingredient) {
        IngredientEntity ingredientEntity = mapper.toEntity(ingredient);
        ingredientEntity.setUuid(UUID.randomUUID());
        IngredientEntity save = ingredientRepository.save(ingredientEntity);
        return mapper.toModel(save);
    }

    @Override
    @Transactional
    public Ingredient updateIngredient(UUID toUpdate, IngredientDataDTO ingredientDataDTO) {
        IngredientEntity ingredientEntity = getIngredientEntityByUuid(toUpdate);
        ingredientEntity.setName(ingredientDataDTO.getName());
        IngredientEntity updatedIngredient = ingredientRepository.save(ingredientEntity);
        return mapper.toModel(updatedIngredient);
    }

    @Override
    @Transactional
    public void deleteIngredient(UUID uuid) {
        IngredientEntity ingredientEntity = getIngredientEntityByUuid(uuid);
        ingredientRepository.delete(ingredientEntity);
    }

    @Override
    public List<ValidatedIngredient> validateIngredients(List<RecipeIngredientDataDTO> recipeIngredients) {
        return recipeIngredients.stream().map(recipeIngredient -> {
            IngredientEntity ingredientEntity = getIngredientEntityByUuid(recipeIngredient.getUuid());
            return new ValidatedIngredient(ingredientEntity, recipeIngredient.getQuantity());
        }).toList();
    }

    public IngredientEntity getIngredientEntityByUuid(UUID uuid) {
        return ingredientRepository.findByUuid(uuid)
                .orElseThrow(() -> new IngredientNotFoundException(
                        "No ingredient found for the provided uuid: %s".formatted(uuid)));
    }
}
