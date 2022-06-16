package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements StationGraphStrategy {
    @Override
    public Path findShortestPath(List<Section> sections, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        setGraph(sections, graph);

        GraphPath graphPath = Optional.ofNullable(path.getPath(source, target))
            .orElseThrow(PathException::new);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int)graphPath.getWeight();

        return Path.of(stations, distance);
    }

    private void setGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for(Section section: sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }
}
