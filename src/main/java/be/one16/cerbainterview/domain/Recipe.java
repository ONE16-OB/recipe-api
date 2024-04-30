package be.one16.cerbainterview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Recipe {
    private UUID uuid;
    private String name;
    private String description;
    private String cookingInstructions;
    private List<Ingredient> ingredients;
    private TrackingData trackingData;
}
