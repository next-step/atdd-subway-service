package nextstep.subway.line.domain.collections;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse findShortestPath(Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap = makeSubwayMap();

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayMap);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        List<Station> routes = shortestPath.getVertexList();
        int distance = (int) shortestPath.getWeight();

        return PathResponse.of(routes, distance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeSubwayMap() {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertexByStations(subwayMap, line.getStations());
            addEdgeBySections(subwayMap, line.getSections());
        }

        return subwayMap;
    }

    private void addEdgeBySections(WeightedMultigraph<Station, DefaultWeightedEdge> map, Sections sections) {
        for (Section section : sections.getSections()) {
            map.setEdgeWeight(map.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void addVertexByStations(WeightedMultigraph<Station, DefaultWeightedEdge> map, List<Station> stations) {
        for (Station station : stations) {
            map.addVertex(station);
        }
    }
}
