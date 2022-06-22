package nextstep.subway.line.infrastructure;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaLineRepository extends JpaRepository<Line, Long>, LineRepository {
    @Override
    List<Line> saveAll(Iterable lines);
}
