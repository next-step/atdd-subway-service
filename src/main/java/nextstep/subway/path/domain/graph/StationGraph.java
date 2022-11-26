package nextstep.subway.path.domain.graph;

import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.exception.CannotGenerateStationGraphException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.exception.PathExceptionCode;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class StationGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph =
            new WeightedMultigraph(DefaultWeightedEdge.class);

    public StationGraph(List<Section> sections) {
        validate(sections);
        generateAll(sections);
    }

    private void validate(List<Section> sections) {
        if(CollectionUtils.isEmpty(sections)) {
            throw new CannotGenerateStationGraphException(
                    PathExceptionCode.CANNOT_GENERATE_STATION_GRAPH.getMessage());
        }
    }

    private void generateAll(List<Section> sections) {
        sections.forEach(section -> {
            addVertex(section.getUpStation());
            addVertex(section.getDownStation());
            addEdge(section);
        });
    }

    private void addVertex(Station station) {
        if(stationGraph.containsVertex(station)) {
            return;
        }

        stationGraph.addVertex(station);
    }

    private void addEdge(Section section) {
        stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(),
                section.getDownStation()), section.getDistance());
    }

    public boolean containsStation(Station station) {
        return stationGraph.containsVertex(station);
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstra = new DijkstraShortestPath(stationGraph);
        return convertToPath(dijkstra.getPath(source, target));
    }

    private Path convertToPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        validateGraphPath(graphPath);
        return new Path(graphPath.getVertexList(), graphPath.getWeight());
    }

    private void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if(Objects.isNull(graphPath)) {
            throw new CannotFindPathException(PathExceptionCode.NOT_CONNECTED_SOURCE_AND_TARGET.getMessage());
        }
    }
}
