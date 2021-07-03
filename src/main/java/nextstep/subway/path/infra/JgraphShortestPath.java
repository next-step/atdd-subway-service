package nextstep.subway.path.infra;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JgraphShortestPath implements ShortestPath<Station> {
    private final DijkstraShortestPath dijkstraShortestPath;

    public JgraphShortestPath(final Lines lines) {
        this.dijkstraShortestPath = initDijkstraShortestPath(lines);
    }

    @Override
    public Path<Station> getPath(final Station source, final Station target) {
        GraphPath path = dijkstraShortestPath.getPath(source, target);

        validation(path);

        //TODO
        return new Path<Station>(path.getVertexList(), path.getWeight(), 0);
    }

    private DijkstraShortestPath initDijkstraShortestPath(final Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        //정점 등록
        lines.getStationAll().stream().forEach(graph::addVertex);

        //간선 등록
        lines.getSectionsAll().stream().forEach(it ->
                graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance())
        );

        return new DijkstraShortestPath(graph);
    }

    private void validation(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }
}
