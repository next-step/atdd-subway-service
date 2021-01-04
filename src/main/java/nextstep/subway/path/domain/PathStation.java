package nextstep.subway.path.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id","name"})
@Getter
public class PathStation {

    private final Long id;

    private final String name;

    private final LocalDateTime createdAt;

    public PathStation(final Long id, final String name, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
