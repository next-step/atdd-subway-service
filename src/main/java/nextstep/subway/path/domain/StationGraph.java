package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class StationGraph{
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final PathStrategy pathStrategy;

    public StationGraph(PathStrategy pathStrategy, List<Section> sections) {
        sections.forEach(this::addGraphEdge);
        pathStrategy.createGraphPath(graph);
        this.pathStrategy = pathStrategy;
    }
    public void addGraphEdge(Section  section){
        addVertex(section.getUpStation());
        addVertex(section.getDownStation());
        addEdge(section);
    }
    public boolean containsStation(Station station) {
        return graph.containsVertex(station);
    }

    public Path findPath(Station source, Station target) {
        validateContains(source, target);
        GraphPath graphPath = pathStrategy.getGraphPath(source, target);
        validateConnected(graphPath);
        List<Station> shortestPath = Collections.unmodifiableList(graphPath.getVertexList());
        Double distance = pathStrategy.getGraphPath(source, target).getWeight();
        return Path.of(shortestPath, distance.intValue());
    }

    private void validateContains(Station source, Station target) {
        if (!containsStation(source) || !containsStation(target)){
            throw new IllegalStateException(ErrorMessage.FIND_PATH_OF_STATION_NOT_ON_GRAPH);
        }
    }
    private void validateConnected(GraphPath graphPath) {
        if (Objects.isNull(graphPath)){
            throw new IllegalStateException(ErrorMessage.FIND_PATH_NOT_CONNECTED);
        }
    }

    private void addEdge(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value());
    };
    private void addVertex(Station station){
        graph.addVertex(station);
    }


}
