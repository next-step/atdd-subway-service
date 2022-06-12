package nextstep.subway.line.domain.collections;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.vo.SectionEdge;
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

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target) {
        validateEqualStation(source, target);
        return getShortestPath(source, target);
    }

    public GraphPath<Station, SectionEdge> findShortestPathV2(Station source, Station target) {
        validateEqualStation(source, target);
        return getShortestPathV2(source, target);
    }

    private GraphPath<Station, SectionEdge> getShortestPathV2(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(makeSubwayMapV2());
        GraphPath<Station, SectionEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validatePathV2(shortestPath);
        return shortestPath;
    }

    private WeightedMultigraph<Station, SectionEdge> makeSubwayMapV2() {
        WeightedMultigraph<Station, SectionEdge> subwayMap
                = new WeightedMultigraph<>(SectionEdge.class);

        for (Line line : lines) {
            line.makeVertexByStationsToV2(subwayMap);
            line.makeEdgeBySectionsToV2(subwayMap);
        }

        return subwayMap;
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(makeSubwayMap());
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validatePath(shortestPath);
        return shortestPath;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeSubwayMap() {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            line.makeVertexByStationsTo(subwayMap);
            line.makedgeBySectionsTo(subwayMap);
        }

        return subwayMap;
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalStateException("[ERROR] 최단경로를 찾을 수 없습니다.");
        }
    }

    private void validatePathV2(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalStateException("[ERROR] 최단경로를 찾을 수 없습니다.");
        }
    }

    private void validateEqualStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("[ERROR] 출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    public int calcFare(List<Station> routes, int distance, Member member) {

        return 0;
    }
}
