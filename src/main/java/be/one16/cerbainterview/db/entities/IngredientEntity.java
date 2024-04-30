package be.one16.cerbainterview.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class IngredientEntity extends TrackedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<RecipeIngredientEntity> recipeIngredients;
}
