package nextstep.subway.path.domain;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class JgraphShortestPathFinder implements ShortestPathFinder {
    private WeightedMultigraph<Station, PathGraphEdge> graph = new WeightedMultigraph<>(PathGraphEdge.class);

    public JgraphShortestPathFinder() {
    }

    private void setGraph(List<Section> sections) {
        sections.forEach((section -> {
            PathGraphEdge pathGraphEdge = new PathGraphEdge(section);
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            Distance distance = section.getDistance();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.addEdge(upStation, downStation, pathGraphEdge);

            graph.setEdgeWeight(pathGraphEdge, distance.value());
        }));
    }

    @Override
    public Path findShortestPath(List<Section> sections, Station startStation, Station targetStation) {
        findShortestPathValid(startStation, targetStation);
        setGraph(sections);
        DijkstraShortestPath<Station, PathGraphEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, PathGraphEdge> graphPath = Optional.ofNullable(dijkstraShortestPath.getPath(startStation, targetStation))
                .orElseThrow(() -> new IllegalArgumentException("찾을 경로가 존재하지 않습니다."));

        Fare maxLineFare = getMaxLineFare(graphPath);

        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), maxLineFare);
    }

    private void findShortestPathValid(Station startStation, Station targetStation) {
        if (startStation.equals(targetStation)) {
            throw new IllegalArgumentException("시작역과 찾을역이 같으면 안됩니다.");
        }
    }
    private Fare getMaxLineFare(GraphPath<Station, PathGraphEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                .map(PathGraphEdge::getLineFare)
                .max(Comparator.comparing(Fare::value))
                .orElseThrow(() -> new NoSuchElementException("최대 요금을 찾을 수 없습니다."));
    }

}
