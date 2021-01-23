package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class PathFinder {

    private LineRepository lineRepository;

    public PathFinder() {
    }

    List<Station> shortestPath = new ArrayList<>();
    int distance;

    public void findRouteSearch(Station station1, Station station2, List<Line> lineList) {
        if (station1.equals(station2)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다!");
        }

        Set<Station> vertex = new HashSet<>();
        List<Section> edge = new ArrayList<>();
        List<Line> lines = lineList;
        for(Line line: lines) {
            extractVertex(vertex, line);
            extracteEdge(edge, line);
        }

        if (!vertex.contains(station1) || !vertex.contains(station2)) {
            throw new IllegalArgumentException("존재하지 않은 출발역이나 도착역입니다!");
        }

        //최단경로 구하기
        distance = getDijkstraShortestPath(station1.getId(), station2.getId(), vertex, edge);
        if (distance == 0) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되지 않았습니다!");
        }
    }

    private void extracteEdge(List<Section> edge, Line line) {
        for(Section section: line.getSections()) {
            edge.add(section);
        }
    }

    private void extractVertex(Set<Station> vertex, Line line) {
        for(Station station: line.getStations(line)) {
            vertex.add(station);
        }
    }

    public Integer getDijkstraShortestPath(Long source, Long target, Set<Station> vertex, List<Section> edge) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        for(Station station: vertex) {
            graph.addVertex(String.valueOf(station.getId()));
        }
        for(Section section: edge) {
            graph.setEdgeWeight(graph.addEdge(String.valueOf(section.getUpStation().getId()),
                    String.valueOf(section.getDownStation().getId())), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        shortestPath = dijkstraShortestPath.getPath(String.valueOf(source), String.valueOf(target)).getVertexList();

        double shortDistance
                = dijkstraShortestPath.getPath(String.valueOf(source), String.valueOf(target)).getWeight();

        return Integer.parseInt(String.valueOf(Math.round(shortDistance)));
    }

    public List<Station> getStation() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }
}

