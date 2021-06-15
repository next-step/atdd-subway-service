package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DijkstraShortestDistance implements ShortestDistance {
    @Override
    public Distance shortestDistance(List<Section> sections, Station source, Station target) {
        GraphPath path = getShortestGraph(sections, source, target);

        return new Distance(path.getWeight());
    }

    @Override
    public Stations shortestRoute(List<Section> sections, Station source, Station target) {
        GraphPath path = getShortestGraph(sections, source, target);

        GraphPath shortestGraph = path;

        return new Stations(shortestGraph.getVertexList());
    }

    private GraphPath getShortestGraph(List<Section> sections, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        sections.stream()
                .forEach(item -> item.prepareShortestDistance(graph));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return path;
    }

}
