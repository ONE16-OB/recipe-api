package be.one16.cerbainterview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TrackingData {
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
