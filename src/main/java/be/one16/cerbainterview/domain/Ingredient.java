package be.one16.cerbainterview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Ingredient {
    private UUID uuid;
    private String name;
    private String quantity;
    private TrackingData trackingData;
}
