package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder() {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public void addPathForLines(List<Line> lines) {
        for(Line line : lines) {
            Sections sections = line.getSections();
            addStationsToVertex(sections.getStations());
            addEdgeWeight(sections.getSections());
        }
    }

    private void addStationsToVertex(List<Station> stations) {
        for(Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeWeight(List<Section> sections) {
        for(Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public Path getShortestPath(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new PathException(PathException.SAME_SOURCE_TARGET_STATION_MSG);
        }

        GraphPath<Station, DefaultWeightedEdge> shortestPath;

        try {
            shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new PathException(PathException.NO_CONTAIN_STATION_MSG);
        }

        if(shortestPath == null) {
            throw new PathException(PathException.PATH_FIND_NO_SEARCH_MSG);
        }

        return Path.of(shortestPath.getVertexList(), (int)shortestPath.getWeight());
    }
}
