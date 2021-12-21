package nextstep.subway.path.domain;

import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    private PathFinder(WeightedMultigraph<Station, SectionEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<Station, SectionEdge>(graph);
    }

    public static PathFinder of(Lines lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        lines.getLines().forEach(line -> {
            addVertex(graph, line);
            setEdgeWeight(graph, line.getSections());
        });
        return new PathFinder(graph);
    }
    
    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validationSameStation(sourceStation, targetStation);
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validationConnectedStation(graphPath);
        
        return Path.of(graphPath.getVertexList(), graphPath.getEdgeList(), (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation));
    }

    private static void addVertex(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private static void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Sections sections) {
        sections.getSections()
        .forEach(section -> {
                SectionEdge sectionEdge = SectionEdge.from(section);
                graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, section.getDistance().getDistance());
        });
    }
    
    private void validationSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }
    
    private void validationConnectedStation(GraphPath<Station, SectionEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않습니다");
        }
    }

}
