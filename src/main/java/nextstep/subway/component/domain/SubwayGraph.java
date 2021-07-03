package nextstep.subway.component.domain;

import nextstep.subway.exception.SubwayPatchException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class SubwayGraph {

    private static final String SAME_STATION_ERROR_MESSAGE = "출발역과 도착역이 일치하면 경로를 찾을 수 없습니다.";
    private static final String NOT_FOUND_PATH_ERROR_MESSAGE = "경로를 찾을 수 없습니다. 출발역과 도착역이 노선으로 연결되어 있는지 확인해주세요.";

    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;

    public SubwayGraph(Class<SectionWeightedEdge> sectionWeightedEdge) {
        graph = new WeightedMultigraph<>(sectionWeightedEdge);
    }


    public void addVertexesAndEdge(List<Line> lines) {
        for (Line line : lines) {
            addVertexesAndEdge(line);
        }
    }

    public SubwayPath calcShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        checkValidGraphPath(graphPath);
        List<Station> stations = graphPath.getVertexList();
        List<SectionWeightedEdge> edges = graphPath.getEdgeList();
        return new SubwayPath(edges, stations);
    }

    public WeightedMultigraph<Station, SectionWeightedEdge> getGraph() {
        return graph;
    }

    public void checkValidSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayPatchException(SAME_STATION_ERROR_MESSAGE);
        }
    }

    private void addVertexesAndEdge(Line line) {
        for (Section section : line.getSections().getSections()) {
            SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section, line.getId());
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionWeightedEdge);
            graph.setEdgeWeight(sectionWeightedEdge, section.getDistance().distance());
        }
    }

    private void checkValidGraphPath(GraphPath<Station, SectionWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new SubwayPatchException(NOT_FOUND_PATH_ERROR_MESSAGE);
        }
    }
}
