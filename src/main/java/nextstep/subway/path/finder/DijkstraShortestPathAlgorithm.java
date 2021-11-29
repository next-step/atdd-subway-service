package nextstep.subway.path.finder;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

public class DijkstraShortestPathAlgorithm implements ShortestPathAlgorithm {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private DijkstraShortestPathAlgorithm(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static DijkstraShortestPathAlgorithm from(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createWeightedMultiGraph(lines);
        return new DijkstraShortestPathAlgorithm(new DijkstraShortestPath<>(graph));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createWeightedMultiGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStationVertexes(lines, graph);
        alignEdgeWeight(lines, graph);

        return graph;
    }

    private static void addStationVertexes(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = StreamUtils.flatMapToList(lines, Line::getStations, Collection::stream);

        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void alignEdgeWeight(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Sections> sections = StreamUtils.mapToList(lines, Line::getSections);
        List<Section> allSections = StreamUtils.flatMapToList(sections, Sections::getValues, Collection::stream);

        for (Section section : allSections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                                section.getDistance().getValue());
        }
    }

    @Override
    public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return ShortestPath.of(path.getVertexList(), (int) path.getWeight());
    }
}
