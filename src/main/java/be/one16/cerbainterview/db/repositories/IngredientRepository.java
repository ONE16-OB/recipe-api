package be.one16.cerbainterview.db.repositories;

import be.one16.cerbainterview.db.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    Optional<IngredientEntity> findByUuid(UUID uuid);
}
