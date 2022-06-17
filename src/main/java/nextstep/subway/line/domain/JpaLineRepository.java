package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaLineRepository extends JpaRepository<Line, Long>, LineRepository {
    @Override
    List<Line> saveAll(Iterable lines);
}
