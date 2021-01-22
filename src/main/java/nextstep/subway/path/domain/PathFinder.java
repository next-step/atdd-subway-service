package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;

public class PathFinder {

    private LineRepository lineRepository;

    public PathFinder() {
    }

    public PathFinder(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    List<Station> shortestPath = new ArrayList<>();
    int distance;

    public void findRouteSearch(Station station1, Station station2) {
        if (station1.equals(station2)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다!");
        }

        Set<Station> vertex = new HashSet<>();
        List<Section> edge = new ArrayList<>();
        List<Line> lines = lineRepository.findAll();
        for(Line line: lines) {
            extractVertex(vertex, line);
            extracteEdge(edge, line);
        }

        if (!vertex.contains(station1) || !vertex.contains(station2)) {
            throw new IllegalArgumentException("존재하지 않은 출발역이나 도착역입니다!");
        }

        //최단경로 구하기
        if (getDijkstraShortestPath(station1, station2, vertex, edge) == 0) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되지 않았습니다!");
        }

        distance = getDijkstraShortestPath(station1, station2, vertex, edge);
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

    public Integer getDijkstraShortestPath(Station source, Station target, Set<Station> vertex, List<Section> edge) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        for(Station station: vertex) {
            graph.addVertex(String.valueOf(station.getId()));
        }
        for(Section section: edge) {
            //graph.setEdgeWeight(graph.addEdge(section.getUpStation().toString(), section.getDownStation().toString()), section.getDistance());
            graph.setEdgeWeight(graph.addEdge(String.valueOf(section.getUpStation().getId()),
                    String.valueOf(section.getDownStation().getId())), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        shortestPath = dijkstraShortestPath.getPath(String.valueOf(source.getId()), String.valueOf(target.toString())).getVertexList();

        double shortDistance
                = dijkstraShortestPath.getPath(String.valueOf(source.getId()), String.valueOf(target.toString())).getWeight();

        return Integer.parseInt(String.valueOf(Math.round(shortDistance)));
    }

    public List<Station> getStation() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }
}

