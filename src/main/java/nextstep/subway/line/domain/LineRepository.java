package nextstep.subway.line.domain;

import java.util.List;

import java.util.Optional;

public interface LineRepository {
    Optional<Line> findById(Long id);

    List<Line> findAll();

    Line save(Line line);

    List<Line> saveAll(Iterable<Line> lines);

    void deleteById(Long id);
}
