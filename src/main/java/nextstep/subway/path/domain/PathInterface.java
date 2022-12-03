package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface PathInterface {
    GraphPath<Station, DefaultWeightedEdge> getShortPath(Station source, Station target);
}
