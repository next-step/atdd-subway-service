package nextstep.subway.path.domain.strategy;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class Dijkstra implements PathStrategy {
    private final Station source;
    private final Station target;
    private final List<Station> stations;
    private final Sections sections;

    public Dijkstra(Station source, Station target, Sections sections) {
        if (source == target) {
            throw new IllegalArgumentException("시작역과 종료역은 같을 수 없습니다.");
        }
        this.source = source;
        this.target = target;
        this.stations = sections.allStations();
        this.sections = sections;
    }

    @Override
    public List<Station> find() {
        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertex(stations);
        subwayGraph.setEdgeWeight(sections);

        return new DijkstraShortestPath(subwayGraph)
                .getPath(source, target)
                .getVertexList();
    }


}
