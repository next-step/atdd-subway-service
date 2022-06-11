package nextstep.subway.path.application;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    void initGraph(Set<Line> lines);

    List<Station> shortestPathVertexList(Station source, Station target);

    int shortestPathWeight(Station source, Station target);
}
