package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
    private List<Section> sections;
    private List<Station> stations;
    private WeightedMultigraph<Long, DefaultWeightedEdge> subwayGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public SubwayGraph() {
    }

    public SubwayGraph(final List<Section> sections, final List<Station> stations) {
        this.sections = sections;
        this.stations = stations;
        setVertexes();
        setEdges();
    }

    private void setVertexes() {
        stations.stream().forEach(station -> {
            subwayGraph.addVertex(station.getId());
        });
    }

    private void setEdges() {
        sections.stream().forEach(section -> {
            subwayGraph.setEdgeWeight(
                    subwayGraph.addEdge(
                            section.getUpStation().getId(),
                            section.getDownStation().getId()),
                    section.getDistance());
        });
    }

    public Path findShortestPath(final Station source, final Station target) {
        if (source == target) {
            throw new RuntimeException("출발역과 도착역은 같을 수 없습니다.");
        }
        final GraphPath graphPath = getGraphPath(source.getId(), target.getId());
        return new Path(getStationsOfGraphPath(graphPath), getDistanceOfGraphPath(graphPath));
    }

    private GraphPath getGraphPath(final Long sourceVertex, final Long targetVertex) {
        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        final GraphPath graphPath = dijkstraShortestPath.getPath(sourceVertex, targetVertex);
        if (null == graphPath) {
            throw new RuntimeException("경로가 존재하지 않습니다.");
        }
        return graphPath;
    }

    private List<Station> getStationsOfGraphPath(final GraphPath graphPath) {
        final List<Long> stationIds = graphPath.getVertexList();
        return stations
                .stream()
                .filter(station -> stationIds.contains(station.getId()))
                .collect(Collectors.toList());
    }

    private int getDistanceOfGraphPath(final GraphPath graphPath) {
        return (int) graphPath.getWeight();
    }
}
