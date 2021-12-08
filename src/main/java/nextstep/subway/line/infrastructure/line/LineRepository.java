package nextstep.subway.line.infrastructure.line;

import nextstep.subway.line.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

    boolean existsByName(String name);
}
