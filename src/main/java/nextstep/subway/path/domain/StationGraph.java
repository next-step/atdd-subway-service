package nextstep.subway.path.domain;

import nextstep.subway.exception.PathCannotFindException;
import nextstep.subway.exception.StationNotIncludedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StationGraph {
    private final WeightedMultigraph<Station, Section> stationGraph = new WeightedMultigraph<>(Section.class);

    public StationGraph(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            addEdge(line.getSections());
        });
    }

    private void validate(List<Section> sections) {
        if (CollectionUtils.isEmpty(sections)) {
            throw new IllegalArgumentException("빈 구간 목록으로 그래프를 생성할 수 없습니다.");
        }
    }

    private void addVertex(Line line) {
        line.getStations().forEach(stationGraph::addVertex);
    }

    private void addEdge(List<Section> sections) {
        sections.forEach(
                section -> {
                    stationGraph.addEdge(section.getUpStation(), section.getDownStation(), section);
                    stationGraph.setEdgeWeight(section, section.distanceValue());
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
        DijkstraShortestPath<Station, Section> dijkstra = new DijkstraShortestPath<>(stationGraph);
        return convertToPath(dijkstra.getPath(source, target));
    }

    private Path convertToPath(GraphPath<Station, Section> graphPath) {
        validateGraphPath(graphPath);
        List<Station> shortestPathVertexes = graphPath.getVertexList();
        Distance shortestPathDistance = new Distance((int) graphPath.getWeight());
        int maxLineFare = findMaxLineFare(findLineInPath(shortestPathVertexes));
        return new Path(shortestPathVertexes, shortestPathDistance, maxLineFare);
    }

    private void validateGraphPath(GraphPath<Station, Section> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new PathCannotFindException();
        }
    }

    private Set<Line> findLineInPath(List<Station> stations) {
        Set<Line> lines = new HashSet<>();
        for(int idx = 0; idx < stations.size() - 1; idx++) {
            Section section = findSectionByUpStationAndDownStation(stations.get(idx), stations.get(idx + 1));
            lines.add(section.getLine());
        }
        return lines;
    }

    private Section findSectionByUpStationAndDownStation(Station upStation, Station downStation) {
        Set<Section> findSections = stationGraph.getAllEdges(upStation, downStation);
        return Section.findShortestDistanceSection(findSections);
    }

    private int findMaxLineFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }
}
