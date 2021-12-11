package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;

import java.util.List;
import java.util.Optional;

public interface Path {

    int getWeight(final Long source, final Long target);
    void createEdge(final List<Line> lines);
    void createVertex(final List<Line> lines);
    Optional<List<Long>> getVertex(final Long source, final Long target);
}
