package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);

    private DijkstraPathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            addEdgeAndSetEdgeWeight(line.getSections());
        });
    }

    public static DijkstraPathFinder createGraph(List<Line> lines) {
        return new DijkstraPathFinder(lines);
    }

    private void addVertex(Line line) {
        line.findStations().forEach(graph::addVertex);
    }

    private void addEdgeAndSetEdgeWeight(Sections sections) {
        sections.getSections().forEach(
                section -> {
                    graph.addEdge(section.getUpStation(), section.getDownStation(), section);
                    graph.setEdgeWeight(section, section.distanceValue());
                });
    }

    @Override
    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateEqualStations(sourceStation, targetStation);
        DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, Section> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validatePathExists(shortestPath);
        List<Station> shortestPathVertexes = shortestPath.getVertexList();
        Distance shortestPathDistance = Distance.from((int) shortestPath.getWeight());
        Fare maxLineFare = Fare.findMaxLineFare(findLineInPath(shortestPathVertexes));
        return Path.of(shortestPathVertexes, shortestPathDistance, Fare.createFare(maxLineFare, shortestPathDistance));
    }

    private void validateEqualStations(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorCode.출발역과_도착역이_서로_같음.getErrorMessage());
        }
    }

    private void validatePathExists(GraphPath<Station, Section> shortestPath) {
        if(shortestPath == null) {
            throw new IllegalArgumentException(ErrorCode.출발역과_도착역은_연결되지_않음.getErrorMessage());
        }
    }

    private Set<Line> findLineInPath(List<Station> stations) {
        Set<Line> lines = new HashSet<>();
        for(int idx = 0; idx < stations.size() - 1; idx++) {
            Section section = findSectionByUpStationAndDownStation(stations.get(idx),
                    stations.get(idx + 1));
            lines.add(section.getLine());
        }
        return lines;
    }

    private Section findSectionByUpStationAndDownStation(Station upStation, Station downStation) {
        Set<Section> findSections = graph.getAllEdges(upStation, downStation);
        return Section.findMinDistanceSection(findSections);
    }
}
