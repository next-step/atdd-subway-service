package nextstep.subway.path.finder;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Component
public class DijkstraShortestPathAlgorithm implements ShortestPathAlgorithm {

    @Override
    public ShortestPath findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createWeightedMultiGraph(lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return ShortestPath.of(path.getVertexList(), (int) path.getWeight());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createWeightedMultiGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStationVertexes(lines, graph);
        alignEdgeWeight(lines, graph);

        return graph;
    }

    private void addStationVertexes(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = StreamUtils.flatMapToList(lines, Line::getStations, Collection::stream);

        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void alignEdgeWeight(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Sections> sections = StreamUtils.mapToList(lines, Line::getSections);
        List<Section> allSections = StreamUtils.flatMapToList(sections, Sections::getValues, Collection::stream);

        for (Section section : allSections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                                section.getDistance().getValue());
        }
    }
}
