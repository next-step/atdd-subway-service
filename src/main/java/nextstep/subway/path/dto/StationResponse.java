package nextstep.subway.path.dto;

import java.time.LocalDateTime;

public class Station {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Station(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
