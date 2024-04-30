package be.one16.cerbainterview.db.repositories;

import be.one16.cerbainterview.db.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findByUuid(UUID uuid);
}
