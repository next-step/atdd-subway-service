package nextstep.subway.navigation.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Navigation {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private GraphPath<Station, DefaultWeightedEdge> path;

    private Navigation(List<Line> persistLines) {
        persistLines.forEach(this::settingGraph);

    }

    public static Navigation of(List<Line> persistLines) {
        return new Navigation(persistLines);
    }

    public NavigationResponse findShortest(Station sourceStation, Station targetStation) {
        path = new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        List<Station> vertexList = path.getVertexList();
        int distance = (int) path.getWeight();

        return NavigationResponse.of(vertexList, distance);
    }

    // graph 등록해야함.
    private void settingGraph(Line line) {
        line.getSections().getSections().forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        });
    }
}
