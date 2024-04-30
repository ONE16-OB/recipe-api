package be.one16.cerbainterview.db.mapper;

import be.one16.cerbainterview.db.entities.IngredientEntity;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.model.IngredientDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientDbMapper {
    IngredientEntity toEntity(IngredientDataDTO ingredient);

    @Mapping(source = "createdAt", target = "trackingData.createdAt")
    @Mapping(source = "updatedAt", target = "trackingData.updatedAt")
    Ingredient toModel(IngredientEntity entity);

}
