package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.PathCannotFindException;
import nextstep.subway.exception.StationNotIncludedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StationGraph {
    private final WeightedMultigraph<Station, SectionWeigthedEdge> stationGraph = new WeightedMultigraph<>(SectionWeigthedEdge.class);
    private final LoginMember loginMember;

    public StationGraph(List<Line> lines, LoginMember loginMember) {
        lines.forEach(line -> {
            addVertex(line);
            addEdge(line.getSections().stream().map(SectionWeigthedEdge::new).collect(Collectors.toList()));
        });
        this.loginMember = loginMember;
    }

    private void addVertex(Line line) {
        line.getStations().forEach(stationGraph::addVertex);
    }

    private void addEdge(List<SectionWeigthedEdge> sectionWeigthedEdges) {
        sectionWeigthedEdges.forEach(
                sectionWeigthedEdge -> {
                    stationGraph.addEdge(sectionWeigthedEdge.getUpStation(), sectionWeigthedEdge.getDownStation(), sectionWeigthedEdge);
                    stationGraph.setEdgeWeight(sectionWeigthedEdge, sectionWeigthedEdge.distanceValue());
                });
    }

    public boolean notContainsStation(Station station) {
        return !stationGraph.containsVertex(station);
    }

    public Path findShortestPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathCannotFindException();
        }
        if (notContainsStation(source) || notContainsStation(target)) {
            throw new StationNotIncludedException();
        }
        DijkstraShortestPath<Station, SectionWeigthedEdge> dijkstra = new DijkstraShortestPath<>(stationGraph);
        return convertToPath(dijkstra.getPath(source, target));
    }

    private Path convertToPath(GraphPath<Station, SectionWeigthedEdge> graphPath) {
        validateGraphPath(graphPath);
        List<Station> shortestPathVertexes = graphPath.getVertexList();
        Distance shortestPathDistance = new Distance((int) graphPath.getWeight());
        int maxLineFare = getMaxLineFare(graphPath.getEdgeList());
        Fare fare = Fare.of(shortestPathDistance, maxLineFare, loginMember);
        return new Path(shortestPathVertexes, shortestPathDistance, fare);
    }

    private void validateGraphPath(GraphPath<Station, SectionWeigthedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new PathCannotFindException();
        }
    }

    private int getMaxLineFare(List<SectionWeigthedEdge> edgeList) {
        return edgeList.stream()
                .mapToInt(SectionWeigthedEdge::getSurcharge)
                .max()
                .orElse(0);
    }
}
