package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;

import java.util.List;
import java.util.Optional;

public interface Path {

    Distance getWeight(final Station source, final Station target);
    void createEdge(final List<Line> lines);
    void createVertex(final List<Line> lines);
    Optional<List<Long>> getVertex(final Station source, final Station target);
}
