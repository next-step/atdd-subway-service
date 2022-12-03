package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public interface PathStrategy {

    GraphPath getGraphPath(Station source, Station target);

    void createGraphPath(Graph graph);
}
