package be.one16.cerbainterview.db.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe_ingredient")
public class RecipeIngredientEntity extends TrackedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private String quantity;
}
