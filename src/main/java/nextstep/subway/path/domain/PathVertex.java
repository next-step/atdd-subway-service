package nextstep.subway.path.domain;

import java.time.LocalDateTime;

public interface PathVertex {
    Long getId();
    String getName();
    LocalDateTime getCreatedDate();
}
