package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;

import java.util.List;

public interface Path {

    Distance getWeight(final Station source, final Station target);
    void createEdge(final List<Line> lines);
    void createVertex(final List<Line> lines);
    List<Station> getVertex(final List<Station> stations, final Station source, final Station target);
}
