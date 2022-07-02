package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> stationGraph = new WeightedMultigraph<>(SectionEdge.class);

    public PathResponse findShortestPath(List<Section> allSection, Station sourceStation, Station targetStation) {
        List<Station> allStations = findAllStations(allSection);
        validate(allStations, sourceStation, targetStation);
        DijkstraShortestPath dijkstraShortestPath = makeDijkstraShortestPath(allSection, allStations);
        GraphPath shortestPath = getShortestPath(sourceStation, targetStation, dijkstraShortestPath);
        Fare maxFare = findMaxLineFare(shortestPath);
        return new PathResponse(shortestPath.getVertexList(), (long) shortestPath.getWeight(), maxFare.calculateFare((long) shortestPath.getWeight()));
    }

    private List<Station> findAllStations(List<Section> allSection) {
        Set<Station> stations = new HashSet<>();
        allSection.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return new ArrayList<>(stations);
    }

    private void validate(List<Station> allStations, Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일하면 최단 경로를 조회할 수 없습니다.");
        }
        if (!allStations.contains(sourceStation) || !allStations.contains(targetStation)) {
            throw new IllegalArgumentException("지하철 역이 존재하지 않습니다.");
        }
    }

    private DijkstraShortestPath makeDijkstraShortestPath(List<Section> allSection, List<Station> allStations) {
        initGraph(allSection, allStations);
        return new DijkstraShortestPath(stationGraph);
    }

    private void initGraph(List<Section> allSection, List<Station> allStations) {
        allStations.forEach(station -> stationGraph.addVertex(station));

        allSection.forEach(section -> {
            SectionEdge sectionEdge = new SectionEdge(section);
            stationGraph.addEdge(sectionEdge.getSource(), sectionEdge.getTarget(), sectionEdge);
            stationGraph.setEdgeWeight(sectionEdge, sectionEdge.getDistance());
        });
    }

    private GraphPath getShortestPath(Station sourceStation, Station targetStation, DijkstraShortestPath dijkstraShortestPath) {
        GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (shortestPath == null) {
            throw new IllegalArgumentException("지하철 역이 연결되어 있지 않습니다.");
        }
        return shortestPath;
    }

    private Fare findMaxLineFare(GraphPath shortestPath) {
        List<SectionEdge> edgeList = shortestPath.getEdgeList();
        List<Fare> fares = edgeList.stream()
            .map(edge -> edge.getFare())
            .collect(Collectors.toList());
        Comparator<Fare> comparatorByFare = Comparator.comparingLong(Fare::value);
        return fares.stream().max(comparatorByFare).get();
    }

}
