package nextstep.subway.path.application;

import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public interface PathFindAlgorithm {
    List<Station> findShortestPathStations(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation);

    GraphPath<Station, SectionEdge> getShortestPathGraph(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation);
}
