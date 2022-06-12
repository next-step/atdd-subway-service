package nextstep.subway.path.domain;

import nextstep.subway.path.application.SectionEdge;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public interface PathFindService {
    PathFindResult findShortestPath(WeightedMultigraph<Station, SectionEdge> graph, Station startStation,
                                    Station endStation) throws NotExistPathException;
}
