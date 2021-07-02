package nextstep.subway.component.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {

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
        GraphPath<Station, SectionWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        List<Station> stations = path.getVertexList();
        List<SectionWeightedEdge> edges = path.getEdgeList();
        return new SubwayPath(edges, stations);
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

    public WeightedMultigraph<Station, SectionWeightedEdge> getGraph() {
        return graph;
    }
}
