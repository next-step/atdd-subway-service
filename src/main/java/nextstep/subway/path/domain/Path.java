package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface Path {
    double getWeight(final Station source, final Station target);
    void createEdge(final List<Line> lines);
    void createVertex(final List<Line> lines);
    List<Station> getVertexes(final Station source, final Station target);
}
